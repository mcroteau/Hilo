package hilo

import static org.springframework.http.HttpStatus.OK

import org.springframework.dao.DataIntegrityViolationException
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.authc.UsernamePasswordToken
import grails.converters.*
import java.util.UUID
import groovy.text.SimpleTemplateEngine

import io.hilo.BaseController
import hilo.common.ControllerConstants

import grails.util.Environment

import io.hilo.Account
import io.hilo.Role
import io.hilo.AccountRole
import io.hilo.Transaction
import io.hilo.common.RoleName
import io.hilo.Permission
import io.hilo.ShoppingCart

import io.hilo.log.ProductViewLog
import io.hilo.log.PageViewLog
import io.hilo.log.CatalogViewLog
import io.hilo.log.SearchLog
import io.hilo.Country

import javax.imageio.ImageIO
import grails.plugin.springsecurity.annotation.Secured

import io.hilo.SimpleCaptchaService

import io.hilo.api.mail.ShipmentAddress
import io.hilo.api.mail.EasyPostShipmentApi

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
	
	
@Mixin(BaseController)
class AccountController {

    static allowedMethods = [customer_profile : "GET", save: "POST", update: "POST", delete: "POST", register: "POST"]

	def emailService
	def applicationService
	def springSecurityService
	def simpleCaptchaService
	def currencyService

    String csvMimeType
    String encoding

    org.springframework.context.MessageSource messageSource
    

	@Secured(['ROLE_CUSTOMER', 'ROLE_ADMIN'])
	def customer_profile(){
		authenticatedAccount { accountInstance ->
			[accountInstance : accountInstance, countries: Country.list()]
		}
	}	
	


	@Secured(['ROLE_CUSTOMER', 'ROLE_ADMIN'])
	def customer_update(){

		authenticatedAccount { customerAccount ->
		
			def accountInstance = Account.get(params.id)
			
   			if (!accountInstance) {
        	    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
   			    redirect(action: "customer_profile")
   			    return
   			}
        	
			accountInstance.properties = params
			accountInstance.addressVerified = false

			def easypostEnabled = applicationService.getEasyPostEnabled()
			
			def shipmentApi 
			if(easypostEnabled == "true"){
				shipmentApi = new EasyPostShipmentApi(applicationService, currencyService)
			}
			//if(shippoEnabled){
				//shipmentApi = new ShippoShipmentApi()
			//}
			
			def address = new ShipmentAddress()
			address.street1 = accountInstance.address1
			address.street2 = accountInstance.address2
			address.city = accountInstance.city
			address.state = accountInstance?.state?.name
			address.country = accountInstance.country.name
			address.zip = accountInstance.zip
			
			if(shipmentApi){
				if(!shipmentApi.validAddress(address)){
   					flash.error = messageSource.getMessage("address.verification.error.message", null, LocaleContextHolder.locale)
   					render(view: "customer_profile", model: [accountInstance: accountInstance, countries: Country.list()])
   					return
				}
				accountInstance.addressVerified = true
			}
			
			
   			if (!accountInstance.save(flush: true)) {
   				flash.message = messageSource.getMessage("update.error.message", null, LocaleContextHolder.locale)
   			    render(view: "customer_profile", model: [accountInstance: accountInstance, countries: Country.list()])
   			    return
   			}
   			
   			flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
   			redirect(action: "customer_profile", id:accountInstance.id)
		}
	}	
	
	@Secured(['permitAll'])	
	def customer_forgot(){}
	

	@Secured(['permitAll'])	
	def customer_send_reset_email(){
	
		if(params.email){

			def accountInstance = Account.findByEmail(params.email)
			
			if(accountInstance){
				
				def resetUUID = UUID.randomUUID()
				accountInstance.resetUUID = resetUUID
				accountInstance.save(flush:true)
				
				def url = request.getRequestURL()
				
				def split = url.toString().split("/${applicationService.getContextName()}/")
				def httpSection = split[0]
				def resetUrl = "${httpSection}/${applicationService.getContextName()}/account/customer_confirm_reset?"
				def params = "username=${accountInstance.username}&uuid=${resetUUID}"
				resetUrl+= params
				
				sendResetEmail(accountInstance, resetUrl)
				
			}else{
				flash.message = messageSource.getMessage("account.not.found.message", [ params.email ] as Object[], "Default", LocaleContextHolder.locale)
				redirect(action:'customer_forgot')
			}
		}else{
			flash.message = messageSource.getMessage("enter.email.continue.message", null, LocaleContextHolder.locale)
			redirect(action:'customer_forgot')
		}
	}
	
	
	
	def sendResetEmail(Account accountInstance, String resetUrl){
		try { 
		
			def fromAddress = applicationService.getSupportEmailAddress()
			def toAddress = accountInstance.email
			def subject = "${applicationService.getStoreName()} : Reset password"

			
			File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/password_reset.html").getFile();

			def binding = [ "companyName" : applicationService.getStoreName(),
				 			"supportEmail" : applicationService.getSupportEmailAddress(),
							"resetUrl": resetUrl ]
			def engine = new SimpleTemplateEngine()
			def template = engine.createTemplate(templateFile).make(binding)
			def bodyString = template.toString()
			
			
			emailService.send(toAddress, fromAddress, subject, bodyString)
			
		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	@Secured(['permitAll'])	
	def customer_confirm_reset(){
		def accountInstance = Account.findByUsernameAndResetUUID(params.username, params.uuid)
		
		if(!accountInstance){
			flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
			redirect(action: 'customer_forgot')
		}	

		[accountInstance: accountInstance]	
	}
	
	
	
	@Secured(['permitAll'])	
	def customer_reset_password(){
		def username = params.username
		def newPassword = params.password
		def confirmPassword = params.confirmPassword
		
		def accountInstance = Account.findByUsername(username)
		if(accountInstance && newPassword && confirmPassword){	
		
			if(confirmPassword == newPassword){
			
				if(newPassword.length() >= 7){
					
					def password = springSecurityService.encodePassword(newPassword)
					accountInstance.password = password
					
					if(accountInstance.save(flush:true)){
				
						//def authToken = new UsernamePasswordToken(username, newPassword as String)					
						flash.message = messageSource.getMessage("successfully.reset.password.message", null, LocaleContextHolder.locale)
						redirect(controller : "auth", action : "customer_login", params : [username : username, password : newPassword, reset : true])
						return

					}else{
						flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
						redirect(action:'confirmReset', params : [username : username, uuid : accountInstance.resetUUID ])
					}
				}else{
					flash.message = messageSource.getMessage("password.length.message", null, LocaleContextHolder.locale)
					redirect(action: 'customer_confirm_reset', params : [uuid : accountInstance.resetUUID, username : username])
				}

			}else{
				flash.message = messageSource.getMessage("passwords.dont.match", null, LocaleContextHolder.locale)
				redirect(action: 'customer_confirm_reset', params : [uuid : accountInstance.resetUUID, username : username])
				
			}
		}else{
			flash.message = messageSource.getMessage("password.blank.message", null, LocaleContextHolder.locale)
			redirect(action: 'customer_confirm_reset', params : [uuid : accountInstance.resetUUID, username : username])
		}
	}
	

	@Secured(['ROLE_ADMIN'])
	def account_activity(Long id){
		authenticatedAdmin { adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}   
			def productViewStats = getProductViewsStatistics(accountInstance)
			def pageViewStats = getPageViewStatistics(accountInstance)
			def catalogViewStats = getCatalogViewsStatistics(accountInstance)
			def searchQueryStats = getSearchQueryStatistics(accountInstance)

			[accountInstance: accountInstance, productViewStats: productViewStats, 
			pageViewStats: pageViewStats, catalogViewStats: catalogViewStats, 
			searchQueryStats: searchQueryStats]
		}
	}


	@Secured(['ROLE_ADMIN'])
	def product_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}   
			def productViewLogs = ProductViewLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def productViewStats = getProductViewsStatistics(accountInstance)
			[accountInstance: accountInstance, productViewLogs: productViewLogs, productViewStats: productViewStats]
		}
	}


	@Secured(['ROLE_ADMIN'])
	def catalog_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}   
			def catalogViewLogs = CatalogViewLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def catalogViewStats = getCatalogViewsStatistics(accountInstance)
			[accountInstance: accountInstance, catalogViewLogs: catalogViewLogs, catalogViewStats: catalogViewStats]
		}
	}




	@Secured(['ROLE_ADMIN'])
	def page_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}   
			def pageViewLogs = PageViewLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def pageViewStats = getPageViewStatistics(accountInstance)
			[accountInstance: accountInstance, pageViewLogs: pageViewLogs, pageViewStats: pageViewStats]
		}
	}




	@Secured(['ROLE_ADMIN'])
	def search_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}   
			def searchLogs = SearchLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def searchQueryStats = getSearchQueryStatistics(accountInstance)
			[accountInstance: accountInstance, searchLogs: searchLogs, searchQueryStats: searchQueryStats]
		}
	}



	
	def getProductViewsStatistics(accountInstance){
		def stats = [:]
		def productViewLogs = ProductViewLog.findAllByAccount(accountInstance)
		
		
		productViewLogs?.each(){ productLog ->
			if(stats[productLog.product.id]){
				stats[productLog.product.id].count += 1
			}else{
				stats[productLog.product.id] = [:]
				stats[productLog.product.id].count = 1
				stats[productLog.product.id].product = productLog.product.name
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	
	
	def getPageViewStatistics(accountInstance){
		def stats = [:]
		def pageViewLogs = PageViewLog.findAllByAccount(accountInstance)
		
		pageViewLogs?.each(){ pageLog ->
			if(stats[pageLog.page.id]){
				stats[pageLog.page.id].count += 1
			}else{
				stats[pageLog.page.id] = [:]
				stats[pageLog.page.id].count = 1
				stats[pageLog.page.id].page = pageLog.page.title
			}
		}
		
		return stats.sort(){ -it.value.count }			
	}
	
	
	
	
	def getCatalogViewsStatistics(accountInstance){
		def stats = [:]
		def catalogViewLogs = CatalogViewLog.findAllByAccount(accountInstance)
		
		catalogViewLogs?.each(){ catalogLog ->
			if(stats[catalogLog.catalog.id]){
				stats[catalogLog.catalog.id].count += 1
			}else{
				stats[catalogLog.catalog.id] = [:]
				stats[catalogLog.catalog.id].count = 1
				stats[catalogLog.catalog.id].catalog = catalogLog.catalog.name
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	
	
	def getSearchQueryStatistics(accountInstance){
		def stats = [:]
		def searchLogs = SearchLog.findAllByAccount(accountInstance)
		

		searchLogs?.each(){ searchLog ->
			if(stats[searchLog.query]){
				stats[searchLog.query].count += 1
			}else{
				stats[searchLog.query] = [:]
				stats[searchLog.query].count = 1
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	

		
	@Secured(['permitAll'])
	def order_history(){
		authenticatedAccount { customerAccount ->
			def transactions = Transaction.findAllByAccount(customerAccount)
			[transactions : transactions]
		}	
	}
	
	
	@Secured(['permitAll'])
	def complete_signup(){
		def accountInstance = Account.get(params.id)
		if(!accountInstance){
			flash.message = messageSource.getMessage("unable.locate.account.message", null, LocaleContextHolder.locale)
			redirect(action: "customer_registration")
			return
		}
		[accountInstance: accountInstance]
	}
	


	@Secured(['permitAll'])
	def customer_registration(){}
	

	@Secured(['permitAll'])
	def customer_register(){

		def existingAccountEmailOptIn = Account.findByUsernameAndEmailAndEmailOptInAndPassword(params.email, params.email, true, "change")
		if(existingAccountEmailOptIn){
			flash.message = messageSource.getMessage("account.started.message", null, LocaleContextHolder.locale)
			render(view: "complete_signup", model: [accountInstance: existingAccountEmailOptIn])
			return
		}

		def accountInstance = new Account(params)

				
		if(params.username.contains(" ")){
			flash.message = messageSource.getMessage("spaces.username.message", null, LocaleContextHolder.locale)
			redirect(action: "customer_registration")
			return
		}
		
		
		if(containsSpecialCharacters(params.username)){ 
			flash.message = messageSource.getMessage("special.characters.message", null, LocaleContextHolder.locale)
			redirect(action: "customer_registration")
			return
		}  

		
		boolean captchaValid = simpleCaptchaService.validateCaptcha(params.captcha)
		if(!captchaValid){
			flash.message = messageSource.getMessage("captcha.error.message", null, LocaleContextHolder.locale)
			render(view: "customer_registration", model: [accountInstance: accountInstance])
			return
		}
		
		if(params.password && params.passwordRepeat){
			
			if(params.password == params.passwordRepeat){

				if(params.password.length() >= 7){
				
					params.ipAddress = request.getRemoteHost()
					accountInstance.properties = params
		
					def existingAccountEmail = Account.findByEmail(params.email)
					if(existingAccountEmail){
						flash.message = messageSource.getMessage("existing.email.registration.message", [ params.email ] as Object [], "Default", LocaleContextHolder.locale)
						redirect(controller: "auth", action: "customer_login")
						return
					}
					
					def existingAccountUsername = Account.findByUsername(params.username)
					if(existingAccountUsername){
						flash.message = messageSource.getMessage("existing.username.registration.message", [ params.username ] as Object[], "Default", LocaleContextHolder.locale)
						redirect(controller: "auth", action: "customer_login")
						return
					}
					
					def password = springSecurityService.encodePassword(params.password)
			   		accountInstance.password = password
		
					if(accountInstance.save(flush:true)){
					
						accountInstance.hasAdminRole = false//TODO:used for easy searching in admin
						accountInstance.createAccountRoles(false)
						accountInstance.createAccountPermission()

						sendAdminEmail(accountInstance)
						sendThankYouEmail(accountInstance)
			
						flash.message = messageSource.getMessage("successfully.registered", null, LocaleContextHolder.locale)
						redirect(controller : 'auth', action: 'customer_login', params : [ accountInstance: accountInstance, username : params.username, password : params.password, new_account : true])
			
					}else{
						flash.message = messageSource.getMessage("account.error.username.message", null, LocaleContextHolder.locale)
						render(view: "customer_registration", model: [accountInstance: accountInstance])
						return
					}
					
				
				}else{
					flash.message = messageSource.getMessage("password.length", null, LocaleContextHolder.locale)
					render(view: "customer_registration", model: [accountInstance: accountInstance])
				}
	
			}else{
				//passwords don't match
				flash.message = messageSource.getMessage("passwords.dont.match", null, LocaleContextHolder.locale)
				render(view: "customer_registration", model: [accountInstance: accountInstance])
			}
		}else{
			flash.message = messageSource.getMessage("password.blank.message", null, LocaleContextHolder.locale)
			render(view: "customer_registration", model: [accountInstance: accountInstance])
		}
	
	}
	

	def containsSpecialCharacters(String str) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		boolean b = m.find();

		if (b){
			return true
		}
		
		return false
	}
	
	@Secured(['permitAll'])
	def complete(){

		def accountInstance = Account.get(params.id)
		if(!accountInstance){
			flash.message = messageSource.getMessage("unable.locate.account.message", null, LocaleContextHolder.locale)
			redirect(action: "customer_registration")
			return
		}

		if(params.username.contains(" ")){
			flash.message = messageSource.getMessage("spaces.username.message", null, LocaleContextHolder.locale)
			redirect(action: "complete_signup", params: [id: accountInstance.id])
			return
		}
		
		
		if(containsSpecialCharacters(params.username)){ 
			flash.message = messageSource.getMessage("special.characters.message", null, LocaleContextHolder.locale)
			redirect(action: "complete_signup", params: [id: accountInstance.id])
			return
		}  
		
		if(params.password && params.passwordRepeat){
			
			if(params.password == params.passwordRepeat){

				if(params.password.length() >= 7){
				
					params.ipAddress = request.getRemoteHost()
					accountInstance.properties = params
					
					def password = springSecurityService.encodePassword(params.password)
			   		accountInstance.password = password
		
					if(accountInstance.save(flush:true)){
					
						accountInstance.hasAdminRole = false//TODO:used for easy searching in admin
						accountInstance.createAccountRoles(false)
						accountInstance.createAccountPermission()

						sendAdminEmail(accountInstance)
						sendThankYouEmail(accountInstance)
			
						flash.message = messageSource.getMessage("successfully.registered")
						redirect(controller : 'auth', action: 'customer_login', params : [ accountInstance: accountInstance, username : params.username, password : params.password, new_account : true])
			
					}else{
						flash.message = messageSource.getMessage("username.maybe.in.use", null, LocaleContextHolder.locale)
						render(view: "complete_signup", model: [accountInstance: accountInstance])
						return
					}
					
				
				}else{
					flash.message = messageSource.getMessage("password.length.message", null, LocaleContextHolder.locale)
					render(view: "complete_signup", model: [accountInstance: accountInstance])
				}
	
			}else{
				//passwords don't match
				flash.message = messageSource.getMessage("passwords.dont.match", null, LocaleContextHolder.locale)
				render(view: "complete_signup", model: [accountInstance: accountInstance])
			}
		}else{
			flash.message = messageSource.getMessage("password.blank.message", null, LocaleContextHolder.locale)
			render(view: "complete_signup", model: [accountInstance: accountInstance])
		}
	}

	
	def sendAdminEmail(Account accountInstance){
		try { 
			
			def fromAddress = applicationService.getSupportEmailAddress()
			if(fromAddress){

				def customerSubject = "${applicationService.getStoreName()} : " + messageSource.getMessage("new.registration", null, LocaleContextHolder.locale)
			
				File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/registration-notification.html").getFile();
	    	
				def binding = [ "companyName" : applicationService.getStoreName(),
					 			"accountInstance" : accountInstance ]
				def engine = new SimpleTemplateEngine()
				def template = engine.createTemplate(templateFile).make(binding)
				def bodyString = template.toString()
				
				emailService.send(applicationService.getAdminEmailAddress(), fromAddress, customerSubject, bodyString)
				
				
			}

		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	
	
	
	def sendThankYouEmail(Account accountInstance){
		try { 
			def fromAddress = applicationService.getSupportEmailAddress()
			if(fromAddress){
				def customerToAddress = accountInstance.email
				def customerSubject = "${applicationService.getStoreName()} : " + messageSource.getMessage("thank.you.for.registering", null, LocaleContextHolder.locale)
				
				File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/registration.html").getFile();

				def binding = [ "companyName" : applicationService.getStoreName(),
					 			"supportEmail" : applicationService.getSupportEmailAddress()]
				def engine = new SimpleTemplateEngine()
				def template = engine.createTemplate(templateFile).make(binding)
				def bodyString = template.toString()
				
				emailService.send(customerToAddress, fromAddress, customerSubject, bodyString)				
			}
		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	
	/** ADMINISTRATION FUNCTIONS **/
	
		
	@Secured(['ROLE_ADMIN'])
	def admin_create(){
		authenticatedAdmin{ account ->
			if(params.admin == "true"){
				request.admin = "true"
			}			
        	[accountInstance: new Account(params), countries: Country.list()]
		}	
	}	


	@Secured(['ROLE_ADMIN'])
	def admin_show(Long id){
		authenticatedAdmin { adminAccount ->
       		def accountInstance = Account.get(id)
       		if (!accountInstance) {
        	    flash.message = message(code: "account.not.found", null, LocaleContextHolder.locale)
       		    redirect(action: "admin_list")
       		    return
       		}  		
       		[accountInstance: accountInstance, countries: Country.list()]
		}	
	}
	

	@Secured(['ROLE_ADMIN'])
	def admin_edit(Long id){
		authenticatedAdmin { adminAccount ->
        	def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = message(code: "account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}   	

			def admin = false
			if(accountInstance.hasAdminRole)admin = true
			
        	[accountInstance: accountInstance, admin : admin, countries: Country.list()]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def admin_save(){
		authenticatedAdmin { adminAccount ->
			def accountInstance = new Account(params)
			
	   		def password = springSecurityService.encodePassword(params.password)
			accountInstance.password = password

			def includeAdminRole = false
			if(params.admin == "true" ||
					params.admin == "on"){
				includeAdminRole = true
			}
			
			accountInstance.hasAdminRole = includeAdminRole
			
			if(accountInstance.validate()){
				accountInstance.save(flush:true)
				
				accountInstance.createAccountRoles(includeAdminRole)
				accountInstance.createAccountPermission()

        	    flash.message = message(code: "successfully.saved", null, LocaleContextHolder.locale)
		       	redirect(action: "admin_edit", id: accountInstance.id)
				
			}else{

				flash.message = messageSource.getMessage("account.error.username.message", null, LocaleContextHolder.locale)
				
			    accountInstance.errors.allErrors.each {
			        println it
			    }
				redirect(action: 'admin_create', accountInstance: accountInstance, params: params)
			}
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def admin_update(Long id){
		authenticatedAdmin { adminAccount ->
			
			def accountInstance = Account.get(id)
			
       		if (!accountInstance) {
       		    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
       		    redirect(action: "admin_list")
       		    return
       		}

			
			accountInstance.properties = params
			def adminRole = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
			

			if(params.admin == "true" ||
					params.admin == "on"){
				accountInstance.createAccountRole(adminRole)
				accountInstance.hasAdminRole = true
			}else{
				def accountRole = AccountRole.findByRoleAndAccount(adminRole, accountInstance)
				if(accountRole){
					accountRole.delete(flush:true)
					accountInstance.hasAdminRole = false
				}
			}

	   		if (!accountInstance.save(flush: true)) {
	   			flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
       		    render(view: "admin_edit", model: [accountInstance: accountInstance])
       		    return
       		}
       		
       		flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
       		redirect(action: "admin_edit", id: accountInstance.id)
		}
	}
	


	@Secured(['ROLE_ADMIN'])
	def admin_order_history(Long id){
		authenticatedAdmin { adminAccount ->
			def accountInstance = Account.get(id)
			
       		if (!accountInstance) {
       		    flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
       		    redirect(action: "admin_list")
       		    return
       		}

       		def transactions = Transaction.findAllByAccount(accountInstance, [sort: "orderDate", order: "desc"])
       		def transactionTotal = Transaction.countByAccount(accountInstance)

       		[accountInstance : accountInstance, transactionInstanceList: transactions, transactionInstanceTotal: transactionTotal]
		
		}
	}
	


	@Secured(['ROLE_ADMIN'])
	def review_order(Long id){
		authenticatedAdmin { adminAccount ->
			def transactionInstance = Transaction.get(id)
			
       		if (!transactionInstance) {
       		    flash.message = messageSource.getMessage("transaction.not.found", null, LocaleContextHolder.locale)
       		    redirect(action: "admin_list")
       		    return
       		}

       		[ transactionInstance : transactionInstance ]		
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def admin_list(){
		authenticatedAdmin { adminAccount ->

			//TODO:add sorting by columns
			
        	def max = 10
			def offset = params?.offset ? params.offset : 0
			def sort = params?.sort ? params.sort : "id"
			def order = params?.order ? params.order : "asc"
			
			def accountInstanceList = []
			def accountInstanceTotal = 0

			def hasAdminRole = (params?.admin && params?.admin == "true") ? true : false
			
			if(params.query){
				
				def accountCriteria = Account.createCriteria()
				def countCriteria = Account.createCriteria()

				accountInstanceTotal = countCriteria.count(){
					and{
						or {
							ilike("username", "%${params.query}%")
							ilike("email", "%${params.query}%")
							ilike("name", "%${params.query}%")
						}
						eq("hasAdminRole", hasAdminRole)
					}
				}
				
				
				accountInstanceList = accountCriteria.list(max: max, offset: offset, sort: sort, order: order){
					and{
						or {
							ilike("username", "%${params.query}%")
							ilike("email", "%${params.query}%")
							ilike("name", "%${params.query}%")
						}
						eq("hasAdminRole", hasAdminRole)
					}
				}
			
			}else{
				accountInstanceList = Account.findAllByHasAdminRole(hasAdminRole, [max: max, offset: offset, sort: sort, order: order])
				accountInstanceTotal = Account.countByHasAdminRole(hasAdminRole)
			}
			
			[ accountInstanceList: accountInstanceList, accountInstanceTotal: accountInstanceTotal, admin: hasAdminRole, query : params.query ]
		}
	}

	@Secured(['ROLE_ADMIN'])
	def admin_edit_password(Long id){
		authenticatedAdmin { adminAccount ->
	        def accountInstance = Account.get(id)
	        if (!accountInstance) {
	            flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
	            redirect(action: "admin_list")
	            return
	        }
			[ accountInstance : accountInstance ]
		}
	}
	

	@Secured(['ROLE_ADMIN'])
	def admin_update_password(Long id){
		authenticatedAdmin { adminAccount ->
	        def accountInstance = Account.get(id)
	        if (!accountInstance) {
	            flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
	            redirect(action: "admin_list")
	            return
	        }
			
			
	   		def password = springSecurityService.encodePassword(params.password)
	   		accountInstance.password = password

	   		if (!accountInstance.save(flush: true)) {
	   			flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
       		    render(view: "admin_edit", model: [accountInstance: accountInstance])
       		    return
       		}
       		
       		flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
       		redirect(action: "admin_edit", id:accountInstance.id)
			
		}
	}
	
		
	
	@Secured(['ROLE_ADMIN'])
	def admin_delete(Long id){
		authenticatedAdmin { adminAccount ->
	        
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
	            flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
        	    redirect(action: "admin_list")
        	    return
        	}
			
			def transactions = Transaction.findByAccount(accountInstance)
			if(!transactions){
			    try {
			    	
			    	def accountRoles = AccountRole.findAllByAccount(accountInstance)
			    	accountRoles.each(){
			    		it.delete(flush:true)
			    	}

			    	def permissions = Permission.findAllByAccount(accountInstance)
			    	permissions.each(){
			    		it.delete(flush:true)
			    	}

			    	def shoppingCarts = ShoppingCart.findAllByAccount(accountInstance)
			    	shoppingCarts.each(){
			    		it.delete(flush:true)
			    	}

			    	def pageViews = PageViewLog.findAllByAccount(accountInstance)
			    	pageViews.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

			    	def productViews = ProductViewLog.findAllByAccount(accountInstance)
					productViews.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

			    	def catalogViews = CatalogViewLog.findAllByAccount(accountInstance)
					catalogViews.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

			    	def searches = SearchLog.findAllByAccount(accountInstance)
					searches.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

	        	    accountInstance.delete(flush: true)
	        	    flash.message = messageSource.getMessage("successfully.deleted.account.message", null, LocaleContextHolder.locale)
	        	    redirect(action: "admin_list")
	        	
	        	} catch (DataIntegrityViolationException e) {
	        	    flash.message = messageSource.getMessage("delete.account.error.username.message", null, LocaleContextHolder.locale)
	        	    redirect(action: "admin_edit", id: id)
	        	}
			}else{
				flash.message = messageSource.getMessage("delete.account.existing.orders.message", null, LocaleContextHolder.locale)
	        	redirect(action: "admin_edit", id: id)
			}
		}
	}
	

    @Secured(['ROLE_ADMIN'])
    def export(){
        def accountsCsvArray = []
        def accounts = Account.list()

        accounts.eachWithIndex { account, index ->
        	def accountLine = ""
			accountLine+= account?.uuid + ","
        	accountLine+= account?.name + ","
        	accountLine+= account?.email + ","
			accountLine+= account?.address1 + ","
			accountLine+= account?.address2 + ","
			accountLine+= account?.city + ","
			accountLine+= account?.state?.name + ","
			accountLine+= account?.country?.name + ","
			accountLine+= account?.zip + ","
			accountLine+= account?.phone + ","
			accountLine+= account?.emailOptIn
			accountsCsvArray.add(accountLine)
        }

        def filename = "accounts.csv"
        def outs = response.outputStream
        response.status = OK.value()
        response.contentType = "${csvMimeType};charset=${encoding}";
        response.setHeader "Content-disposition", "attachment; filename=${filename}"
 
        accountsCsvArray.each { String line ->
        	println line
            outs << "${line}\n"
        }
 
        outs.flush()
        outs.close()
    } 



	@Secured(['permitAll']) 
    def captcha() {
        def captcha = session[SimpleCaptchaService.CAPTCHA_IMAGE_ATTR] ?: simpleCaptchaService.newCaptcha()
        ImageIO.write(captcha, "PNG", response.outputStream)
    }







}