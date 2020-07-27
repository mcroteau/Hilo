package hilo

import io.hilo.BaseController
import io.hilo.Layout
import io.hilo.Page
import io.hilo.Product
import io.hilo.Catalog


import grails.plugin.springsecurity.annotation.Secured

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder


@Mixin(BaseController)
class LayoutController {

	def applicationService

	MessageSource messageSource

	//Grails 2 explicitly set
    //def grailsApplication
	private final String SETTINGS_FILE = "settings.properties"
	
	/** TODO: investigate whether to customize login screen layout & search results layout **/
	private final String CHECKOUT_PREVIEW = "checkout.preview.layout"
	private final String CHECKOUT_SCREEN = "checkout.screen.layout"
	private final String CHECKOUT_SUCCESS = "checkout.success.layout"
	private final String REGISTRATION_SCREEN = "registration.screen.layout"
	

 	@Secured(['ROLE_ADMIN'])	
	def how(){}


 	@Secured(['ROLE_ADMIN'])
	def tags(){}
		
	
 	@Secured(['ROLE_ADMIN'])	
    def index() {	
		authenticatedAdmin{ adminAccount ->
			def offset = params.offset ? params.offset : 0
			def sort = params.sort ? params.sort : "name"
			def order = params.order ? params.order : "asc"
			def layouts = Layout.findAll(max:10, offset:offset, sort: sort, order: order)
		
			/**
			File cssFile = grailsApplication.mainContext.getResource("css/store.css").file
			String css = cssFile.text
			css = css.replace("[[CONTEXT_NAME]]", applicationService.getContextName())
			[layout : Layout.findByName("STORE_LAYOUT").content, css : css]
			**/
			
			[layouts: layouts, layoutInstanceTotal: Layout.count()]
			
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def create(){}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def save(){

		def layoutInstance = new Layout(params)
		println "content" + layoutInstance.content
		println "css" + layoutInstance.css
		println "javascript" + layoutInstance.javascript
		
		
		if(!layoutInstance.name){
			flash.message = messageSource.getMessage("layout.name.empty.message", null, LocaleContextHolder.locale)
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
		}

		
		def existingLayout = Layout.findByName(layoutInstance.name)
		if(existingLayout){
			println "existing..."
			flash.message = messageSource.getMessage("layout.name.unique.message", null, LocaleContextHolder.locale)
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
		}
		
		
		if(layoutInstance.content && !layoutInstance.content.contains("[[CONTENT]]")){
			flash.message = messageSource.getMessage("layout.content.tag.message", null, LocaleContextHolder.locale)
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
		}
		

		def existingDefaultLayouts = Layout.findAllByDefaultLayout(true)
		
		if(layoutInstance.defaultLayout){
			if(existingDefaultLayouts){
				existingDefaultLayouts.each { layout ->
					layout.defaultLayout = false
					layout.save(flush:true)
				}
			}
		}else{
			if(!existingDefaultLayouts){
				layoutInstance.defaultLayout = true
			}
		}
		
		
		if(!layoutInstance.save(flush: true)) {
			flash.message = messageSource.getMessage("layout.content.tag.message", null, LocaleContextHolder.locale)
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
	    }
		
		redirect(action:"show", id: layoutInstance.id)
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def show(Long id){
		def layoutInstance = Layout.get(id)
		if(!layoutInstance){
			flash.message = messageSource.getMessage("layout.not.found", null, LocaleContextHolder.locale)
			redirect(action:"index")
		}
		[layoutInstance: layoutInstance]
	}
	
 	@Secured(['ROLE_ADMIN'])
	def edit(Long id){
		def layoutInstance = Layout.get(id)
		if(!layoutInstance){
			flash.message = messageSource.getMessage("layout.not.found", null, LocaleContextHolder.locale)
			redirect(action:"index")
		}
		[layoutInstance: layoutInstance]
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		def layoutInstance = Layout.get(id)
		if(!layoutInstance){
			flash.message = messageSource.getMessage("layout.not.found", null, LocaleContextHolder.locale)
			redirect(action:"index")
		}
		
    	layoutInstance.properties = params
		
		if(!layoutInstance.name){
			flash.message = messageSource.getMessage("layout.empty.message", null, LocaleContextHolder.locale)
	        render(view: "edit", model: [layoutInstance: layoutInstance])
	        return
		}
		
		
		if(layoutInstance.content && !layoutInstance.content.contains("[[CONTENT]]")){
			flash.message = messageSource.getMessage("layout.content.tag.message", null, LocaleContextHolder.locale)
	        render(view: "edit", model: [layoutInstance: layoutInstance])
	        return
		}


		def existingDefaultLayouts = Layout.findAllByDefaultLayout(true)
		if(!layoutInstance.defaultLayout){
			if(!existingDefaultLayouts)layoutInstance.defaultLayout = true
		}else{
			existingDefaultLayouts.each{ layout ->
				if(layout != layoutInstance){
					layout.defaultLayout = false
					layout.save(flush:true)
				}
			}
		}
		
		
		if(!layoutInstance.save(flush: true)) {
			flash.message = messageSource.getMessage("layout.content.tag.message", null, LocaleContextHolder.locale)
	        render(view: "edit", model: [layoutInstance: layoutInstance])
	        return
	    }
		
		flash.message = messageSource.getMessage("successfully.saved", null, LocaleContextHolder.locale)
		redirect(action:"edit", id: layoutInstance.id)
		
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def delete(Long id){
		def layout = Layout.get(id)
		if(!layout){
			flash.message = messageSource.getMessage("layout.not.found", null, LocaleContextHolder.locale)
			redirect(action: "index")
			return
		}
		if(layout.defaultLayout){
			flash.message = messageSource.getMessage("layout.default.message", null, LocaleContextHolder.locale)
			redirect(action: "index")
			return
		}
		
		try{
		
			layout.delete(flush:true)
			flash.message = messageSource.getMessage("successfully.deleted", null, LocaleContextHolder.locale)
			redirect(action: "index")
			
		}catch(Exception e){
			e.printStackTrace()
			flash.message = messageSource.getMessage("something.wrong.layout.products.catalogs.assigned", request.locale)
			redirect(action: "show", id: id)
		}
		
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def edit_wrapper(){
		File layoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout-wrapper.html").file
		def layoutWrapper = layoutFile.text
		
		[layoutWrapper: layoutWrapper]
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def update_wrapper(){
		File layoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout-wrapper.html").file
		FileWriter fw = new FileWriter(layoutFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		def html = params.layoutWrapper
		bw.write(html)
		bw.close()
		flash.message = messageSource.getMessage("successfully.updated", request.locale)
		redirect(action:"edit_wrapper")
	}
	
 	@Secured(['ROLE_ADMIN'])
	def restore_wrapper(){		
		File backuplayoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout-wrapper.backup").file
		
		File layoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout-wrapper.html").file
		FileWriter fw = new FileWriter(layoutFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(backuplayoutFile.text)
		bw.close()
		flash.message = messageSource.getMessage("successfully.updated", request.locale)
		redirect(action:"edit_wrapper")
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def edit_support_layouts(){
		
		Properties prop = new Properties();
		try{
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			request.checkout_preview = prop.getProperty(CHECKOUT_PREVIEW)
			request.checkout_screen = prop.getProperty(CHECKOUT_SCREEN)
			request.checkout_success = prop.getProperty(CHECKOUT_SUCCESS)
			request.registration_screen = prop.getProperty(REGISTRATION_SCREEN)
			/**
			private final String CHECKOUT_PREVIEW = "checkout.preview.layout"
			private final String CHECKOUT_SCREEN = "checkout.screen.layout"
			private final String CHECKOUT_SUCCESS = "checkout.success.layout"
			private final String REGISTRATION_SCREEN = "registration.screen.layout"
			**/
			
			def layouts = Layout.list()
			
			[layouts: layouts]
			
		}catch(Exception e){
			flash.message = messageSource.getMessage("something.went.wrong.settings.properties", null, LocaleContextHolder.locale)
			redirect(controller:"layout", action:"index")
		}
	}
	
 	@Secured(['ROLE_ADMIN'])
	def save_support_layouts(){
		
		Properties prop = new Properties();
		try{
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
		
			prop.load(inputStream);
			prop.setProperty(CHECKOUT_PREVIEW, params.checkout_preview);
			prop.setProperty(CHECKOUT_SCREEN, params.checkout_screen);
			prop.setProperty(CHECKOUT_SUCCESS, params.checkout_success);
			prop.setProperty(REGISTRATION_SCREEN, params.registration_screen)
			def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
			absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
			def filePath = absolutePath + SETTINGS_FILE
			
		    prop.store(new FileOutputStream(filePath), null);

			applicationService.setProperties()
			
		}catch(Exception e){
			flash.message = messageSource.getMessage("something.went.wrong.settings.properties", null, LocaleContextHolder.locale)
			redirect(controller:"layout", action:"index")
		}
		
		flash.message = messageSource.getMessage("successfully.updated", request.locale)
		redirect(action: "edit_support_layouts")
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def apply_pages(){
		def layout = Layout.get(params.id)
		if(!layout){
			flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
			redirect(action:"list")
		}
		def pages = Page.list()
		pages.each(){ page ->
			page.layout = layout
			page.save(flush:true)
		}

		flash.message = messageSource.getMessage("successfully.applied.pages", request.locale)
		redirect(action:"show", id: params.id)
	}

	
	
 	@Secured(['ROLE_ADMIN'])	
	def apply_products(){
		def layout = Layout.get(params.id)
		if(!layout){
			flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
			redirect(action:"list")
		}
		def products = Product.list()
		products.each(){ product ->
			product.layout = layout
			product.save(flush:true)
		}

		flash.message = messageSource.getMessage("successfully.applied.products", request.locale)
		redirect(action:"show", id: params.id)
	}

	
	
 	@Secured(['ROLE_ADMIN'])	
	def apply_catalogs(){
		def layout = Layout.get(params.id)
		if(!layout){
			flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
			redirect(action:"list")
		}
		def catalogs = Catalog.list()
		catalogs.each(){ catalog ->
			catalog.layout = layout
			catalog.save(flush:true)
		}

		flash.message = messageSource.getMessage("successfully.applied.catalogs", request.locale)
		redirect(action:"show", id: params.id)
	}
	
}
