package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Layout
import io.hilo.Catalog
import io.hilo.Product
import io.hilo.ProductOption

import hilo.common.DomainMockHelper

class ProductOptionSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain ProductOption
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
	    
		def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)
	    
		def productOption = DomainMockHelper.getMockProductOption(product)
		productOption.save(flush:true)

	    expect:
		Layout.count() == 1
	    Catalog.count() == 1
	    Product.count() == 1
	    ProductOption.count() == 1
	}
	
}