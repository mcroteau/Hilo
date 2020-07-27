package hilo

import org.springframework.dao.DataIntegrityViolationException
import io.hilo.BaseController

import io.hilo.Account
import io.hilo.Page
import io.hilo.log.PageViewLog
import io.hilo.Layout

import grails.plugin.springsecurity.annotation.Secured

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder


@Mixin(BaseController)
class PageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    MessageSource messageSource

	
	def view(Long id){   
    	def pageInstance = Page.get(id)
    	if (!pageInstance) {
			flash.message = messageSource.getMessage("page.not.found", null, LocaleContextHolder.locale)
    	    redirect(controller : "store", action: "index")
    	    return
    	} 

    	[pageInstance: pageInstance]
	}
	
	
 	@Secured(['permitAll'])
	def store_view(String title){
		def t = java.net.URLDecoder.decode(params.title, "UTF-8");
		def pageInstance = Page.findByTitle(t)
		if(!pageInstance){
			flash.message = messageSource.getMessage("page.not.found", null, LocaleContextHolder.locale)
    	    redirect(controller : "store", action: "index")
		}
		
		def pageViewLog = new PageViewLog()
		pageViewLog.page = pageInstance

		def accountInstance
		if(principal?.username){
			accountInstance = Account.findByUsername(principal?.username)
			pageViewLog.account = accountInstance
		}
		pageViewLog.save(flush:true)
		
		if(accountInstance){
			accountInstance.pageViews = PageViewLog.countByAccount(accountInstance)
			accountInstance.save(flush:true)
		}
		
		[pageInstance : pageInstance]
	}
	
	
	//TODO:catch if deleted in database
 	@Secured(['permitAll'])
	def home(){
		def pageInstance = Page.findByTitle("Home")
		[pageInstance: pageInstance]
	}
	
	
	
	

	/** ADMINISTRATION METHODS **/
	
 	@Secured(['ROLE_ADMIN'])
    def create() {
		def layouts = Layout.list()
        [pageInstance: new Page(params), layouts: layouts]
    }
	
	
 	@Secured(['ROLE_ADMIN'])
	def index() {
		authenticatedAdmin{ adminAccount ->
        	redirect(action: "list", params: params)
		}
    }

    
	
 	@Secured(['ROLE_ADMIN'])
	def list(Integer max) {
		authenticatedAdmin{ adminAccount ->
        	params.max = Math.min(max ?: 10, 100)
        	[pageInstanceList: Page.list(params), pageInstanceTotal: Page.count()]
    	}
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def show(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
			def layouts = Layout.list()
        	[pageInstance: pageInstance, layouts: layouts]
		}
    }
	
	

 	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
			def layouts = Layout.list()
        	[pageInstance: pageInstance, layouts: layouts]
		}
    }



 	@Secured(['ROLE_ADMIN'])
    def save() {	
		authenticatedAdmin { adminAccount ->

        	def pageInstance = new Page(params)
        	if (!pageInstance.save(flush: true)) {
				def layouts = Layout.list()
				flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
        	    render(view: "create", model: [pageInstance: pageInstance, layouts: layouts])
        	    return
        	}
        	
        	flash.message = messageSource.getMessage("successfully.saved", null, LocaleContextHolder.locale)
        	redirect(action: "edit", id: pageInstance.id)
    	}
	}
	
	
	

 	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        
        	pageInstance.properties = params
        	
        	if (!pageInstance.save(flush: true)) {
				flash.message = messageSource.getMessage("something.went.wrong", null, LocaleContextHolder.locale)
        	    render(view: "edit", model: [pageInstance: pageInstance])
        	    return
        	}
        	
        	flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
        	redirect(action: "edit", id: pageInstance.id)
		}
    }
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        	try {

				//Delete all ProductViewLogs
				PageViewLog.executeUpdate("delete PageViewLog p where p.page = :page", [page : pageInstance])
				
        	    pageInstance.delete(flush: true)

        	    flash.message = messageSource.getMessage("successfully.deleted", null, LocaleContextHolder.locale)
        	    redirect(action: "list")
        	}
        	catch (DataIntegrityViolationException e) {
        	    flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
        	    redirect(action: "edit", id: id)
        	}
		}
    }
		
}
