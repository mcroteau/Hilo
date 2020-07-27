package io.hilo

import hilo.common.ControllerConstants

import io.hilo.ApplicationConstants
import io.hilo.Account
import io.hilo.Catalog
import io.hilo.Product
import io.hilo.ProductOption 
import io.hilo.ProductSpecification
import io.hilo.Specification
import io.hilo.SpecificationOption 
import io.hilo.Transaction
import io.hilo.ShoppingCart
import io.hilo.common.RoleName
import io.hilo.Specification
import io.hilo.Variant
import io.hilo.Page
import io.hilo.State
import io.hilo.Layout

import io.hilo.log.ProductViewLog
import io.hilo.log.PageViewLog
import io.hilo.log.CatalogViewLog
import io.hilo.log.SearchLog

import org.apache.shiro.SecurityUtils


//http://mrpaulwoods.wordpress.com/2011/01/23/a-pattern-to-simplify-grails-controllers/

class BaseController {

	/** internationalization **/ 	
	def continueMessage = "You must log in to continue..."
	def permissionMessage = "You do not have permission..."
	def wrongMessage = "Unable to locate resource requested."


	private def authenticatedAccount(Closure c){

		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		c.call accountInstance
	}


	private def authenticatedCustomer(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_CUSTOMER.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
		c.call accountInstance
	}
	


	private def authenticatedAdmin(Closure c){

		//def principal = SecurityUtils.getSubject().getPrincipal();
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'admin_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'admin_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
		c.call accountInstance
	}
	


	private def authenticatedAdminProduct(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
    	def productInstance = Product.get(params.id)
		
        if (!productInstance) {
            flash.message = wrongMessage
            forward(controller: 'product', action: "list")
			return
        }
		
		c.call accountInstance, productInstance		
	}
	

	
	private def authenticatedAdminCatalog(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
    	def catalogInstance = Catalog.get(params.id)
		
        if (!catalogInstance) {
            flash.message = wrongMessage
            forward(controller: 'catalog', action: "list")
			return
        }
		
		c.call accountInstance, catalogInstance		
	}
	

	
	
	private def authenticatedAdminPage(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
    	def pageInstance = Page.get(params.id)
		
        if (!pageInstance) {
            flash.message = wrongMessage
            forward(controller: 'page', action: "list")
			return
        }
		
		c.call accountInstance, pageInstance
	}
    
	
	
	
	private def authenticatedAdminShoppingCart(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
    	def shoppingCartInstance = ShoppingCart.get(params.id)
		
        if (!shoppingCartInstance) {
            flash.message = wrongMessage
            forward(controller: 'page', action: "list")
			return
        }
		
		c.call accountInstance, shoppingCartInstance
	}
	
	
	
	
	private def authenticatedAdminTransaction(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
    	def transactionInstance = Transaction.get(params.id)
	
		
        if (!transactionInstance) {
            flash.message = wrongMessage
            forward(controller: 'transaction', action: "list")
			return
        }
		
		c.call accountInstance, transactionInstance
	}
	

	
	
	private def authenticatedAdminProductOption(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
		def productOption = ProductOption.get(params.id)
    	
		
    	if (!productOption) {
    	    flash.message = wrongMessage
    	    forward(controller: 'product', action: "list")
			return
    	}
		
		c.call accountInstance, productOption
	}




	private def authenticatedAdminSpecification(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
		def specificationInstance = Specification.get(params.id)

		if (!specificationInstance) {
			flash.message = wrongMessage
			forward(controller: 'catalog', action: "list")
			return
		}

		c.call accountInstance, specificationInstance
	}




	private def authenticatedAdminSpecificationOption(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}
		
		def specificationOption = SpecificationOption.get(params.id)

		if (!specificationOption) {
			flash.message = wrongMessage
			forward(controller: 'catalog', action: "list")
			return
		}

		c.call accountInstance, specificationOption
	}





	private def authenticatedPermittedCustomer(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_CUSTOMER.description() 
		}

		if(!accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return	
		}

		def permission = accountInstance.permissions.find { 
			//TODO:Remove cleanup
			//it.permission == "account:customer_profile:${accountInstance.id}" 
			it.permission == ControllerConstants.ACCOUNT_PERMISSION + accountInstance.id
		}

		if (!permission){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return
		}
		
		c.call accountInstance
	}
	

private def authenticatedPermittedShoppingCart(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def shoppingCartInstance = ShoppingCart.get(params.id)
		
		if(!shoppingCartInstance){
			flash.message = wrongMessage
			forward(controller:'store', action:'index')
			return
		}

		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		def permission = accountInstance.permissions.find { 
			it.permission == ControllerConstants.SHOPPING_CART_PERMISSION + shoppingCartInstance.id
		}

		if(!permission && !accountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return
		}
		
		c.call accountInstance, shoppingCartInstance
		
	}
	

	

	private def authenticatedPermittedOrderDetails(Closure c){
		if(!principal?.username){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = continueMessage
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def adminAccountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		def transactionInstance = Transaction.get(params.id)
		
		if(!transactionInstance){
			flash.message = wrongMessage
			forward(controller:'store', action:'index')
			return
		}
		
		def permission = accountInstance.permissions.find { 
			it.permission == ControllerConstants.TRANSACTION_PERMISSION + transactionInstance.id
		}

		if(!permission && !adminAccountRole){
			flash.message = permissionMessage
			forward(controller:'store', action:'index')
			return
		}
		
		c.call accountInstance, transactionInstance
		
	}
	


}