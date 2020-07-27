package hilo

import io.hilo.Account

import static org.springframework.http.HttpStatus.OK
import grails.plugin.springsecurity.annotation.Secured

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class NewsletterController {

    static allowedMethods = [signup: ["GET", "POST"], opt_opt: "POST"]
    
    String csvMimeType
    String encoding
    
    MessageSource messageSource


    @Secured(['permitAll'])
    def index(){}


    @Secured(['permitAll'])
    def signup(){
    	def account = new Account()
    	account.username = params.email
    	account.email = params.email
    	account.emailOptIn = true
    	account.password = "change"

    	def existingAccount = Account.findByEmail(params.email)
    	if(existingAccount && existingAccount.emailOptIn){
    		flash.message = messageSource.getMessage("already.signed.up.updates", null, LocaleContextHolder.locale)
    		render(view : "signup")
    		return
    	}


    	if(existingAccount && !existingAccount.emailOptIn){
    		flash.message = messageSource.getMessage("already.email.found", null, LocaleContextHolder.locale)
    		redirect(action: "found", id: existingAccount.id)
    		return
    	}

    	if(!account.save(flush:true)){
    		flash.message = messageSource.getMessage("please.enter.valid.email", null, LocaleContextHolder.locale)
    	}else{
    		flash.message = messageSource.getMessage("successfully.signedup.newsletter", [account.email] as Object[], "Default", LocaleContextHolder.locale)
    	}


    	account.errors.allErrors.each{ println it }

    	redirect(action: "index")
    }



    @Secured(['permitAll'])
    def found(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
    		redirect(action: "signup")
    		return
    	}
		[account : account]
    }



    @Secured(['ROLE_ADMIN'])
    def opt_in(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
    	}

    	account.emailOptIn = true
    	account.save(flush:true)

    	flash.message = messageSource.getMessage("successfully.opted.in", [ account.email ] as Object[], "Defaut", LocaleContextHolder.locale)
		redirect(action: "index")
    }



    @Secured(['ROLE_ADMIN'])
    def admin_opt_in(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
    	}

    	account.emailOptIn = true
    	account.save(flush:true)

    	flash.message = messageSource.getMessage("successfully.opted.in", [ account.email ] as Object[], "Defaut", LocaleContextHolder.locale)
		redirect(controller:"account", action: "admin_edit", id: account.id)
    }



    @Secured(["permitAll", "ROLE_ADMIN"])
    def opt_out(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
    	}

    	account.emailOptIn = false
    	account.save(flush:true)

    	flash.message = messageSource.getMessage("successfully.opted.out", [ account.email ] as Object[], "Defaut", LocaleContextHolder.locale)

    	if(params.redirect == "true"){
    		redirect(action: "index")
    		return
    	}

		redirect(action: "list")
    }



    @Secured(['ROLE_ADMIN'])
    def admin_opt_out(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
    	}

    	account.emailOptIn = false
    	account.save(flush:true)

    	flash.message = messageSource.getMessage("successfully.opted.out", [ account.email ] as Object[], "Defaut", LocaleContextHolder.locale)
		redirect(controller:"account", action: "admin_edit", id: account.id)
    }



    @Secured(["permitAll"])
    def confirm(){
    	def account = Account.findByEmail(params.email)
    	if(!account){
    		flash.message = messageSource.getMessage("account.not.found", null, LocaleContextHolder.locale)
    		redirect(action: "index")
    		return
    	}

    	[account: account]
    }



    @Secured(['ROLE_ADMIN'])
    def list(){
    	def max = 10
		def offset = params?.offset ? params.offset : 0
		def sort = params?.sort ? params.sort : "id"
		def order = params?.order ? params.order : "asc"

		def accountsList = Account.findAllByEmailOptIn(true, [max: max, offset: offset, sort: sort, order: order ])

		[accountsList: accountsList, accountsTotal: Account.countByEmailOptIn(true)]
    }


    @Secured(['ROLE_ADMIN'])
    def export(){
        def accountsCsvArray = []
        def accounts = Account.findAllByEmailOptIn(true)

        accounts.each { account ->
            accountsCsvArray.add(account.email)
        }

        def filename = "account-emails.csv"
        def outs = response.outputStream
        response.status = OK.value()
        response.contentType = "${csvMimeType};charset=${encoding}";
        response.setHeader "Content-disposition", "attachment; filename=${filename}"
 
        accountsCsvArray.each { String line ->
            outs << "${line}\n"
        }
 
        outs.flush()
        outs.close()
    } 





}