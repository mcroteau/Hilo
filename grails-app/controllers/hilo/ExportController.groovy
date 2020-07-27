package hilo

import io.hilo.BaseController
import grails.plugin.springsecurity.annotation.Secured

import io.hilo.Account
import io.hilo.Permission
import io.hilo.Catalog
import io.hilo.Product
import io.hilo.ProductOption
import io.hilo.Variant

import io.hilo.Specification
import io.hilo.SpecificationOption
import io.hilo.ProductSpecification

import io.hilo.AdditionalPhoto

import io.hilo.ShoppingCart
import io.hilo.ShoppingCartItem
import io.hilo.ShoppingCartItemOption

import io.hilo.Transaction
import io.hilo.Page
import io.hilo.Upload
import io.hilo.Layout

import io.hilo.log.CatalogViewLog
import io.hilo.log.PageViewLog
import io.hilo.log.ProductViewLog
import io.hilo.log.SearchLog
import io.hilo.log.LoginLog

import groovy.json.*
import groovy.json.JsonOutput
import grails.converters.JSON
import groovy.json.JsonOutput

import java.io.InputStream
import java.io.ByteArrayInputStream

import io.hilo.ExportDataService

@Mixin(BaseController)
class ExportController {
	
	def exportDataService
	def missingUuidHelperService
	
	@Secured(['permitAll'])
	def shopping_carts(){
		def shoppingCarts = ShoppingCart.list()
		render shoppingCarts as JSON
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def resolve_missing_uuids(){
		println "resolve missing uuids..."
		missingUuidHelperService.correctMissingUuids()
		def json = [:]
		render json as JSON
	}
	
 	@Secured(['ROLE_ADMIN'])
	def view_export(){
		//TODO:add numbers of data to be exported
	}

 	@Secured(['ROLE_ADMIN'])
	def export_data(){

		def json = exportDataService.export(params)
		InputStream is = new ByteArrayInputStream(json.getBytes());

		render(file: is, fileName: "hilo-data.json")	
	}
	
}