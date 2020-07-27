package hilo

import org.springframework.dao.DataIntegrityViolationException
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import io.hilo.BaseController
import java.awt.Graphics2D
import grails.util.Environment
import grails.converters.*

import io.hilo.Product
import io.hilo.ProductOption
import io.hilo.Variant

import grails.plugin.springsecurity.annotation.Secured


import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder


@Mixin(BaseController)
class ProductOptionController {

	MessageSource messageSource


    @Secured(['ROLE_ADMIN'])
    def manage_positions(Long id){
        authenticatedAdmin { adminAccount ->
			def product = Product.get(id)
			if(!product){
				flash.message = messageSource.getMessage("product.not.found", null, LocaleContextHolder.locale)
				redirect(controller: 'product', action:'list')
			}
            def productOptions = ProductOption.findAllByProduct(product)
            [ productInstance : product, productOptions: productOptions ]
        }
    }

    
    @Secured(['ROLE_ADMIN'])
    def update_positions(Long id){    
        authenticatedAdmin { adminAccount ->
			if(!params.positions){
				flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
				redirect(action:'manage_positions')
				return
			}
			
			def positions = params.positions.split(',').collect{it as int}
			
			if(!positions){
				flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
				redirect(action:'manage_positions')
				return
			}
			
			positions.eachWithIndex(){ productOptionId, position ->
				def productOption = ProductOption.get(productOptionId)
				productOption.position = position
				productOption.save(flush:true)
			}
			
			flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
			redirect(action : 'manage_positions', id: params.id)
        }
    }
	
	
	
	@Secured(['ROLE_ADMIN'])
	def edit(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			def variants = Variant.findAllByProductOption(productOptionInstance)
			[ productInstance : productOptionInstance.product, productOptionInstance : productOptionInstance, variants : variants ]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def update(){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			if(params.name){
				productOptionInstance.name = params.name
				
				if(productOptionInstance.save(flush:true)){
					flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
					redirect(action:'edit', id: productOptionInstance.id)
				}else{
					flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
					redirect(action:'edit', id: productOptionInstance.id)
				}
				
			}else{
				flash.message = messageSource.getMessage("name.cannot.be.blank", null, LocaleContextHolder.locale)
				redirect(action:'edit', id: productOptionInstance.id)
			}
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def add_variant(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			
			if(!params.name){
				flash.variantMessage = messageSource.getMessage("variant.name.specified", null, LocaleContextHolder.locale)
				request.productInstance = productOptionInstance.product
				request.productOptionInstance = productOptionInstance
				request.variants = productOptionInstance.variants
				render(view : 'edit')
				return
			}
			
			if(!params.price || !params.price.isDouble()){
				flash.variantMessage = messageSource.getMessage("variant.price.dollar", null, LocaleContextHolder.locale)
				request.name = params.name
				request.productInstance = productOptionInstance.product
				request.productOptionInstance = productOptionInstance
				request.variants = productOptionInstance.variants
				render(view : 'edit')
				return
			}
				
			
			def variant = new Variant()
			variant.name = params.name
			variant.price = params.price.toDouble()
			variant.productOption = productOptionInstance
			
			def imageFile = request.getFile('image')
			
			def fullFileName = imageFile.getOriginalFilename()
			
			String[] nameSplit = fullFileName.toString().split("\\.")
			def fileName = nameSplit[0]
			
			fileName = fileName.replaceAll("[^\\w\\s]","")
			fileName = fileName.replaceAll(" ", "_")
			
			
			BufferedImage originalImage = null;
			
			try {
				
				originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
				if(originalImage){
				
		 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					def baseUrl = "images/"
					
					def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
					absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
					def baseDirectory = "${absolutePath}"
					new File(baseDirectory).mkdirs();
					
					def imageUrl = "${baseUrl}${fileName}.jpg"
					def imageLocation = "${baseDirectory}${fileName}.jpg"
					ImageIO.write(originalImage, "jpg", new File(imageLocation));
		   
		   			variant.imageUrl = imageUrl
				}
		
		
				if(variant.save(flush:true)){
					productOptionInstance.addToVariants(variant)
					productOptionInstance.save(flush:true)
					flash.variantMessage = messageSource.getMessage("successfully.added", null, LocaleContextHolder.locale)
				}else{
					request.price = params.price
					flash.variantMessage = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
				}
					
				redirect(action : 'edit', id : productOptionInstance.id)
		   
		    } catch (IOException e) {
		    	e.printStackTrace();
				flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
	       	 	redirect(controller:'product', action: "list")
		    }		
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def remove_variant(Long id){
		authenticatedAdmin{ account ->
			if(params.id){
				def variant = Variant.get(params.id)
				if(variant){
					def productOption = variant.productOption
					variant.delete(flush:true)
					productOption.removeFromVariants(variant)
					productOption.save(flush:true)
					
					redirect(action : 'edit', id : productOption.id)
					
				}else{
					flash.message = messageSource.getMessage("Unable to find variant", null, LocaleContextHolder.locale)
					redirect(controller : 'product', action: 'list')
				}
			}else{
				flash.message = messageSource.getMessage("Unabel to find variant", null, LocaleContextHolder.locale)
				redirect(controller: 'product', action : 'list')
			}
		}	
	}
	
	
	
	
	@Secured(['ROLE_ADMIN'])
	def edit_variant(Long id){
		authenticatedAdmin{ account ->
			if(params.id){
				def variant = Variant.get(params.id)
				if(variant){				
					[ productOptionInstance : variant.productOption, variant : variant ]
				}else{
					flash.message = messageSource.getMessage("Unable to find variant", null, LocaleContextHolder.locale)
					redirect(controller : 'product', action: 'list')
				}
			}else{
				flash.message = messageSource.getMessage("Unabel to find variant", null, LocaleContextHolder.locale)
				redirect(controller: 'product', action : 'list')
			}
		}	
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def update_variant(){
		authenticatedAdmin{ account ->
			if(params.id){
				def variant = Variant.get(params.id)
				if(variant){			
						
					if(!params.name){
						flash.message = messageSource.getMessage("Variant Name must be specified", null, LocaleContextHolder.locale)
						request.productOptionInstance = variant.productOption
						request.variant = variant
						render(view : 'edit_variant')
						return
					}
			
					if(!params.price || !params.price.isDouble()){
						flash.message = messageSource.getMessage("Variant Price must be a valid dollar amount", null, LocaleContextHolder.locale)
						request.name = params.name
						request.productOptionInstance = variant.productOption
						request.variant = variant
						render(view : 'edit_variant')
						return
					}
					
					def imageFile = request.getFile('image')
					def fullFileName = imageFile.getOriginalFilename()
			
					String[] nameSplit = fullFileName.toString().split("\\.")
					def fileName = nameSplit[0]
			
					fileName = fileName.replaceAll("[^\\w\\s]","")
					fileName = fileName.replaceAll(" ", "_")
			
					BufferedImage originalImage = null;
			
					try {
				
						originalImage = ImageIO.read(imageFile.getInputStream());
		 	   	
						if(originalImage){
				
				 	    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

							def baseUrl = "images/"
					
							def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('images')
							absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
							def baseDirectory = "${absolutePath}"
							new File(baseDirectory).mkdirs();
					
							def imageUrl = "${baseUrl}${fileName}.jpg"
							def imageLocation = "${baseDirectory}${fileName}.jpg"
							ImageIO.write(originalImage, "jpg", new File(imageLocation));
		   
				   			variant.imageUrl = imageUrl
						}
					
					
						variant.name = params.name
						variant.price = params.price.toDouble()
						
						
						if(variant.save(flush:true)){
							flash.message = messageSource.getMessage("Successfully updated variant", null, LocaleContextHolder.locale)
							request.productOptionInstance = variant.productOption
							request.variant = variant
							render(view : 'edit_variant')
							return
						}else{
							flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
							request.productOptionInstance = variant.productOption
							request.variant = variant
							render(view : 'edit_variant')
							return
						}
					
					
					
				    } catch (IOException e) {
				    	e.printStackTrace();
						flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
						request.productOptionInstance = variant.productOptionInstance
						request.variant = variant
						render(view : 'edit_variant')
						return
				    }
					
				
				}else{
					flash.message = messageSource.getMessage("variant.not.found", null, LocaleContextHolder.locale)
					redirect(controller : 'product', action: 'list')
				}
			}else{
				flash.message = messageSource.getMessage("variant.not.found", null, LocaleContextHolder.locale)
				redirect(controller: 'product', action : 'list')
			}
		}
	}
	

	
	@Secured(['ROLE_ADMIN'])
	def edit_variant_positions(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			def variants = Variant.findAllByProductOption(productOptionInstance)
			[ productOptionInstance : productOptionInstance, variants : variants ]
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def update_variant_positions(Long id){
		authenticatedAdminProductOption { adminAccount, productOptionInstance ->
			if(!params.positions){
				flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
				redirect(action:'edit_variant_positions', id : id)
				return
			}
			
			def positions = params.positions.split(',').collect{it as int}
			
			if(!positions){
				flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
				redirect(action:'edit_variant_positions', id : id)
				return
			}
			
			positions.eachWithIndex(){ variantId, position ->
				def variant = Variant.get(variantId)
				variant.position = position
				variant.save(flush:true)
			}
			
			flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
			redirect(action : 'edit_variant_positions', id : id)
			return
			
		}
	}
	
}