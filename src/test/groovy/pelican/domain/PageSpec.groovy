package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Layout
import io.hilo.Page

import hilo.common.DomainMockHelper

class PageSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Page
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
	    
		def page = DomainMockHelper.getMockPage(layout)
		page.save(flush:true)

	    expect:
	    Layout.count() == 1
	    Page.count() == 1
	}
	
}