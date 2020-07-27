package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Catalog
import io.hilo.Layout
import io.hilo.Product
import io.hilo.Specification
import io.hilo.SpecificationOption

import hilo.common.DomainMockHelper

class SpecificationOptionSpec extends spock.lang.Specification implements DataTest {

	void setupSpec(){
        mockDomain SpecificationOption
	}

	void "test basic persistence mocking"() {
	    setup:
		def specification = DomainMockHelper.getMockSpecification()
		specification.save(flush:true)
		
		def specificationOption = DomainMockHelper.getMockSpecificationOption(specification)
		specificationOption.save(flush:true)

	    expect:
	    Specification.count() == 1	
	    SpecificationOption.count() == 1	
	}
	
}