package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Layout

import hilo.common.DomainMockHelper

class LayoutSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Layout
	}

	void "test basic persistence mocking"() {
	    setup:
	    def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)

	    expect:
	    Layout.count() == 1
	}
	
}