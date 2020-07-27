package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Catalog
import io.hilo.Layout
import io.hilo.Product
import io.hilo.Specification

import hilo.common.DomainMockHelper

class SpecificationSpec extends spock.lang.Specification implements DataTest {

	void setupSpec(){
        mockDomain io.hilo.Specification
	}

	void "test basic persistence mocking"() {
	    setup:
		def specification = DomainMockHelper.getMockSpecification()
		specification.save(flush:true)

	    expect:
	    Specification.count() == 1	
	}
	
}