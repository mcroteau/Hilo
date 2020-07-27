package hilo

import org.springframework.dao.DataIntegrityViolationException
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import io.hilo.BaseController
import java.awt.Graphics2D
import grails.util.Environment
import grails.converters.*

import io.hilo.Account
import io.hilo.log.ProductViewLog
import io.hilo.log.SearchLog

import io.hilo.Product
import io.hilo.Catalog
import io.hilo.AdditionalPhoto
import io.hilo.ProductOption
import io.hilo.ProductSpecification 
import io.hilo.Variant
import io.hilo.Layout


import grails.plugin.springsecurity.annotation.Secured

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder


@Mixin(BaseController)
class ProductController {

    static allowedMethods = [ save: "POST", update: "POST", delete: "POST", save_option: 'POST' ]
	
    def applicationService
	def currencyService
	
    MessageSource messageSource

	def index() {
    	redirect(controller: 'store', action: "index")
    }
	
 	@Secured(['permitAll'])
	def search(){		
		if(params.query && params.query.length() >= 4){
			def max = 12
			def offset = params.offset ? params.offset : 0
			
			def productNameCriteria = Product.createCriteria()
			def products = productNameCriteria.list(max: max, offset: offset, sort: "name", order: "desc"){
				or {
					ilike("name", "%${params.query}%")
					ilike("description", "%${params.query}%")
				}
			}
				
			def searchLog = new SearchLog()
			searchLog.query = params.query.toLowerCase()
			
			def accountInstance
			if(principal?.username){
				accountInstance = Account.findByUsername(principal?.username)
				searchLog.account = accountInstance
			}
			searchLog.save(flush:true)
			
			if(accountInstance){
				accountInstance.searches = SearchLog.countByAccount(accountInstance)
				accountInstance.save(flush:true)
			}			
			
			[ products : products, offset : offset, max : max, query : params.query ]

		}else{
			flash.message = messageSource.getMessage("search.query.minimum.message", null, LocaleContextHolder.locale)
			[products : [], offset : 0, max : 10, query : "" ]
		}
	}
	
	
	
	
	@Secured(['permitAll'])
	def details(Long id){    
		def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = messageSource.getMessage("product.not.found", null, LocaleContextHolder.locale)
            redirect(controller: 'store', action: "index")
            return
        }
		
		def baseProductOptions = ProductOption.findAllByProduct(productInstance)
		def productOptions = []
		
		baseProductOptions.each(){ productOption ->
			
			def option = [:]
			option.id = productOption.id
			option.name = productOption.name
			
			def variants = Variant.findAllByProductOption(productOption)
			option.variants = variants
			
			productOptions.add(option)
			
		}
		
		def productViewLog = new ProductViewLog()
		productViewLog.product = productInstance
		
		def accountInstance
		if(principal?.username){
			accountInstance = Account.findByUsername(principal?.username)
			productViewLog.account = accountInstance
		}
		productViewLog.save(flush:true)
		
		if(accountInstance){
			accountInstance.productViews = ProductViewLog.countByAccount(accountInstance)
			accountInstance.save(flush:true)
		}


		def url = request.getRequestURL()
		
		def split = url.toString().split("/${applicationService.getContextName()}/")
		def httpSection = split[0]
		def urlPre = "${httpSection}/${applicationService.getContextName()}/"
		
		def shareUrl = urlPre + "product/details/${productInstance.id}"
		def shareImageUrl = productInstance.imageUrl ? (urlPre + productInstance?.imageUrl) : urlPre + "images/app/no-image.jpg"
		def title = productInstance?.name
		def description = productInstance?.description ? productInstance.description : productInstance?.name + messageSource.getMessage("product.details", null, LocaleContextHolder.locale)


		[ productInstance: productInstance, productOptions: productOptions, 
		title: title, description: description, shareUrl: shareUrl, shareImageUrl: shareImageUrl ]
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def list(Integer max) {
		authenticatedAdmin { adminAccount ->
			params.max = Math.min(max ?: 10, 100)
			params.sort = params.sort ? params.sort : "id"
			params.order = params.order ? params.order : "asc"

			def productInstanceList = Product.list(params)

			def containsImports = false
			productInstanceList.each { 
				if(it.importUuid){
					containsImports =  true
				}
			}
			
    		[productInstanceList: productInstanceList, productInstanceTotal: Product.count(), catalogCount: Catalog.count(), containsImports: containsImports]
		}
    }



    @Secured(['ROLE_ADMIN'])
    def outofstock(Integer max) {
		authenticatedAdmin { adminAccount ->
			params.max = Math.min(max ?: 10, 100)
    		request.productInstanceList = Product.findAllByQuantity(0)
			request.productInstanceTotal = Product.countByQuantity(0)
			render(view : 'list')
		}
    }
	

    @Secured(['ROLE_ADMIN'])
	def admin_search(){
		authenticatedAdmin { adminAccount ->

			if(params.query && params.query.length() >= 3){
				/**def p1 = Product.findAll("from Product as p where UPPER(p.name) like UPPER('%${params.query}%') AND p.disabled = false")
				def p2 = Product.findAll("from Product as p where UPPER(p.description) like UPPER('%${params.query}%') AND p.disabled = false")
				
				
				p1.collect( { productA ->
				    def productB = p2.find { it.name == productA.name }
					p2.remove(productB)
				})
				**/
				def max = params.max ? params.max : 10
				def offset = params.offset ? params.offset : 0
				
				def productNameCriteria = Product.createCriteria()
				def productInstanceList = productNameCriteria.list(max: max, offset: offset, sort: "id", order: "asc"){
					or {
						ilike("name", "%${params.query}%")
						ilike("description", "%${params.query}%")
					}
				}
				
				def productNameCriteriaTotal = Product.createCriteria()
				def productInstanceListTotal = productNameCriteriaTotal.list(){
					or {
						ilike("name", "%${params.query}%")
						ilike("description", "%${params.query}%")
					}
				}
        	
				//def productInstanceList = p1 + p2
				request.productInstanceList = productInstanceList
				request.productInstanceTotal = productInstanceListTotal.size()
				request.query = params.query
				
				render(view:'list')
			}else{
				flash.message = messageSource.getMessage("admin.search.query.minimum.message", null, LocaleContextHolder.locale)
				redirect(action:'list')
			}
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
    def create() {
		authenticatedAdmin { adminAccount ->
			
			def layouts = Layout.list()
			
			if(Catalog.count() == 0){
				flash.message = messageSource.getMessage("minimum.catalog.message", null, LocaleContextHolder.locale)
				redirect(controller:'product', action: 'list')
				return
			}
			
			def productInstance = new Product(params)
			
			def catalogIdsArray = []
			if(productInstance?.catalogs){
				catalogIdsArray = productInstance?.catalogs.collect { it.id }
			}
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
			
			[ productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList, layouts: layouts ]
    	}
	}	
	
	
	def resizeImage(originalImage, type, height, width){
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
 
		return resizedImage;
	}


	@Secured(['ROLE_ADMIN'])
    def save() {
		authenticatedAdmin { adminAccount ->
		    
			resolveSalesPrice(params)

			def productInstance = new Product(params)
			
			try{
				
				
				productInstance.price = new BigDecimal(params.price)
				if(params.salesPrice && params.salesPrice != "-"){
					productInstance.salesPrice = new BigDecimal(params.salesPrice)
				}
				
//				println "product sale price : " + productInstance.salesPrice
            	
				def catalogIdsArray = []
				def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
				
				
	    		if(!productInstance.validate()){
					println "**************************"
					println "***      INVALID       ***"
					println "**************************"
					
					flash.message = messageSource.getMessage("product.information.invalid.message", null, LocaleContextHolder.locale)
					render(view:"create",  model: [productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList])
					return
				}	
					
				def imageFile = request.getFile('image')
				
				BufferedImage originalImage = null;
				def fullFileName = imageFile.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				def productName = productInstance.name
				
				productName = productName.replaceAll("[^\\w\\s]",""); 
				productName = productName.replaceAll(" ", "_")
		    	
				try {
	        	
					
					originalImage = ImageIO.read(imageFile.getInputStream());
		 		   	
					if(originalImage){
					
		 		    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            	
						
						//def baseUrl = "images/products/${productName}/"
						def baseUrl = "images/"
						
						def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
						absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
						//def baseDirectory = "${absolutePath}products/${productName}/"
						def baseDirectory = "${absolutePath}"
						
						
						new File(baseDirectory).mkdirs();
						
						productInstance.detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
 						BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
					
						def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
						ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
						
						
						def imageUrl = "${baseUrl}${fileName}.jpg"
						productInstance.imageUrl = imageUrl
				
						def imageLocation = "${baseDirectory}${fileName}.jpg"
						ImageIO.write(originalImage, "jpg",new File(imageLocation));
					
					}
					
					
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
				
		    	if (!productInstance.save(flush: true)) {
		    	    render(view: "create", model: [productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList])
		    	    return
		    	}
		    	
		    	
				if(!params.catalogIds){
					flash.error = messageSource.getMessage("product.no.catalogs.defined", null, LocaleContextHolder.locale)
    	    	    redirect(action: "edit", id: productInstance.id )
    	    	    return
				}
				
				def catalogSelectedIdsArray = params.catalogIds.split(',').collect{it as int}
				
				if(!catalogSelectedIdsArray){
					flash.error = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
    	    	    redirect(action: "edit", id: productInstance.id )
					return
				}    	  
				
				productInstance.catalogs = null
				catalogSelectedIdsArray.each{ catalogId ->
					def catalog = Catalog.get(catalogId)
					if(catalog){
						productInstance.addToCatalogs(catalog)
						productInstance.save(flush:true)
					}
				}
				
		    	
		    	flash.message = messageSource.getMessage("successfully.saved", null, LocaleContextHolder.locale)
		    	redirect(action: "edit", id: productInstance.id)	
			
			}catch(Exception e){
				e.printStackTrace()
				flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
				redirect(action:"create", model: [productInstance: productInstance])
			}	
		}	
    }

	


	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
			
			def layouts = Layout.list()
			
			if(Catalog.count() == 0){
				flash.error = messageSource.getMessage("product.visibility.error.message", null, LocaleContextHolder.locale)
			}
			
			def catalogIdsArray = []
			if(productInstance?.catalogs){
				catalogIdsArray = productInstance?.catalogs.collect { it.id }
			}
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
    	    [ productInstance: productInstance, catalogIdsArray: catalogIdsArray, catalogIdSelectionList: catalogIdSelectionList, layouts: layouts ]
		}
    }
	
	
	

	def getCatalogIdSelectionList(catalogIdsArray){	
		def catalogMenuString = "<ul class=\"catalog_list admin-catalog-selection\">"
		def toplevelCatalogs = Catalog.findAllByToplevel(true)
		toplevelCatalogs.each{ catalog ->
			def checked = ""
			if(catalogIdsArray.contains(catalog.id)){
				checked = "checked"
			}
			catalogMenuString += "<li><input type=\"checkbox\" id=\"checkbox_${catalog.id}\" class=\"catalog_checkbox\" data-id=\"${catalog.id}\" data-name=\"${catalog.name}\" ${checked}>&nbsp;${catalog.name}"
			if(catalog.subcatalogs){
				def subcatalogMenuString = getAllSubcatalogLists(catalog, catalogIdsArray)
				catalogMenuString += subcatalogMenuString
			}
			catalogMenuString += "</li>"
		}
		catalogMenuString += "</ul>"
	}
	
	
	def getAllSubcatalogLists(catalog, catalogIdsArray){
		def subcatalogsMenu = "<ul class=\"catalog_list admin-subcatalog-selection\">"
		catalog.subcatalogs.sort { it.id }
		catalog.subcatalogs.each{ subcatalog ->
			def checked = ""
			if(catalogIdsArray.contains(subcatalog.id)){
				checked = "checked"
			}
			subcatalogsMenu += "<li><input type=\"checkbox\" id=\"checkbox_${subcatalog.id}\" class=\"catalog_checkbox\" data-id=\"${subcatalog.id}\" data-name=\"${subcatalog.name}\" ${checked}>&nbsp;${subcatalog.name}"
			if(subcatalog.subcatalogs){
				subcatalogsMenu += getAllSubcatalogLists(subcatalog, catalogIdsArray)
			}
			subcatalogsMenu += "</li>"
		}
		subcatalogsMenu += "</ul>"
		
		return subcatalogsMenu
	}
	
	
	
	


	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		authenticatedAdminProduct { adminAccount, productInstance ->
			
			try{

				def productCatalogIdsArray = []
				if(productInstance?.catalogs){
					productCatalogIdsArray = productInstance?.catalogs.collect { it.id }
				}
				def catalogIdSelectionList = getCatalogIdSelectionList(productCatalogIdsArray)
				
				
				if(!params.catalogIds){
					flash.error = messageSource.getMessage("product.no.catalogs.defined", null, LocaleContextHolder.locale)
    	    	    render(view: "edit", model: [productInstance: productInstance, catalogIdSelectionList: catalogIdSelectionList ])
    	    	    return
				}
				
				def catalogIdsArray = params.catalogIds.split(',').collect{it as int}
				
				if(!catalogIdsArray){
					flash.error = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
					render(view: "edit", model: [productInstance: productInstance])
					return
				}    	  
            	
            	
				productInstance.catalogs = null
				catalogIdsArray.each{ catalogId ->
					def catalog = Catalog.get(catalogId)
					if(catalog){
						productInstance.addToCatalogs(catalog)
						productInstance.save(flush:true)
					}
				}
            	
				
				def imageFile = request.getFile('image')
				BufferedImage originalImage = null;
				def fullFileName = imageFile.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				def productName = productInstance.name
				
				productName = productName.replaceAll("[^\\w\\s]",""); 
				productName = productName.replaceAll(" ", "_")
				
				
				try {
	        	
					originalImage = ImageIO.read(imageFile.getInputStream());
		 		   	
					if(originalImage){
					
		 		    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            	
						//def baseUrl = "images/products/${productName}/"
						def baseUrl = "images/"
						
						def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
						absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
						//def baseDirectory = "${absolutePath}products/${productName}/"
						def baseDirectory = "${absolutePath}"
						
						
						new File(baseDirectory).mkdirs();
						
						productInstance.detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
 						BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
					
						def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
						ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
						
						
						def imageUrl = "${baseUrl}${fileName}.jpg"
						productInstance.imageUrl = imageUrl
				
						def imageLocation = "${baseDirectory}${fileName}.jpg"
						ImageIO.write(originalImage, "jpg",new File(imageLocation));
					
					}
					
					
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
				
				resolveSalesPrice(params)
            	
				productInstance.properties = params
				productInstance.price = new BigDecimal(params.price)
				
				if(params.salesPrice && params.salesPrice != "-"){
					productInstance.salesPrice = new BigDecimal(params.salesPrice)
				}
		    	
				
    	    	if (!productInstance.save(flush: true)) {
					flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
    	    	    render(view: "edit", model: [productInstance: productInstance])
    	    	    return
    	    	}
    	    	
    	    	flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
    	    	redirect(action: "edit", id: productInstance.id)
				
			}catch(Exception e){
				e.printStackTrace()
				flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
				redirect(action:"edit", id: productInstance.id)
			}
    	}
	}




	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		authenticatedAdminProduct { adminAccount, productInstance ->
       		try {
				//Delete all ProductViewLogs
				ProductViewLog.executeUpdate("delete ProductViewLog p where p.product = :product", [product : productInstance])
                
                def productSpecifications = ProductSpecification.findAllByProduct(productInstance)
                productSpecifications.each { productSpecification ->
                    productInstance.removeFromProductSpecifications(productSpecification)
                    productSpecification.delete(flush:true)
                }
                
       		    productInstance.delete(flush: true)
       		    flash.message = messageSource.getMessage("successfully.deleted", null, LocaleContextHolder.locale)
       		    redirect(action: "list")
       		}catch (DataIntegrityViolationException e) {
       		    flash.message = messageSource.getMessage("product.delete.item.error.message", null, LocaleContextHolder.locale)
       		    redirect(action: "edit", id: id)
        	}
		}
    }
	
	
	
	
	
	@Secured(['ROLE_ADMIN'])
	def remove_additional_photo(Long id){
		authenticatedAdmin{ adminAccount ->
			def additionalPhoto = AdditionalPhoto.get(id)
			
			if(additionalPhoto){
				def productInstance = additionalPhoto.product
				additionalPhoto.delete(flush:true)
				flash.message = messageSource.getMessage("sucessfully.removed.additional.photo", null, LocaleContextHolder.locale)
				redirect(action:"additional_photos", id: productInstance.id)
			}
		}
	}
	

	

	@Secured(['ROLE_ADMIN'])
	def add_additional_photo(){
		authenticatedAdminProduct { adminAccount, productInstance ->
		
			try {

				def imageFile = request.getFile('image')
				
				def fullFileName = imageFile.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				BufferedImage originalImage = null;
				
				def productName =  productInstance.name
				productName = productName.replaceAll("[^\\w\\s]",""); 
				productName = productName.replaceAll(" ", "_")
				
				
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					//def baseUrl = "images/products/${productName}/"
					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					//def baseDirectory = "${absolutePath}products/${productName}/"
					def baseDirectory = "${absolutePath}"
					
					
					new File(baseDirectory).mkdirs();
					
					def detailsImageUrl = "${baseUrl}${fileName}_details.jpg"
					BufferedImage detailsImageJpg = resizeImage(originalImage, type, 250, 300);
				
					def detailsImageLocation = "${baseDirectory}${fileName}_details.jpg"
					ImageIO.write(detailsImageJpg, "jpg", new File(detailsImageLocation));
					
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg", new File(imageLocation));

	    			def additionalPhoto = new AdditionalPhoto();
					additionalPhoto.name = fileName
					additionalPhoto.imageUrl = imageUrl
					additionalPhoto.detailsImageUrl = detailsImageUrl
					additionalPhoto.product = productInstance
					additionalPhoto.save(flush:true)
					
					flash.message = messageSource.getMessage("successfully.added.photo", null, LocaleContextHolder.locale)
	       	 		redirect(action: "additional_photos", id: productInstance.id)
					
				}else{
					flash.message = messageSource.getMessage("please.provide.product.image", null, LocaleContextHolder.locale)
	       	 		redirect(action: "additional_photos", id: productInstance.id)
				}
				
		    } catch (IOException e) {
		    	e.printStackTrace();
				flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
	       	 	redirect(action: "additional_photos", id: productInstance.id)
		    }
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def additional_photos(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}	
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def product_options(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			def productOptions = ProductOption.findAllByProduct(productInstance)
			[productInstance : productInstance, productOptions: productOptions]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def add_product_option(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			[productInstance : productInstance]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def save_option(Long id){
		authenticatedAdminProduct { adminAccount, productInstance ->
			if(params.name){
				def productOption = new ProductOption()
				productOption.product = productInstance
				productOption.name = params.name
				productOption.save(flush:true)
				
				productInstance.addToProductOptions(productOption)
				productInstance.save(flush:true)

				flash.message = messageSource.getMessage("successfully.created.product.option", null, LocaleContextHolder.locale)
				redirect(controller : 'productOption', action: 'edit', id : productOption.id )
				
			}else{
				flash.message = messageSource.getMessage("product.option.name.blank.message", null, LocaleContextHolder.locale)
				request.productInstance = productInstance
				render(view : 'add_product_option')
			}
		}
	}
	
	def resolveSalesPrice(params){
		if(params.salesPrice == '-')params.salesPrice = null
	}
	
}
