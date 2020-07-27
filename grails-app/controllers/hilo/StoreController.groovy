package hilo

import grails.plugin.springsecurity.annotation.Secured
import io.hilo.Account

class StoreController {

 	@Secured(['permitAll'])
    def index() {
    	def accounts = Account.list()
    	[ accounts : accounts ]
    }

}
