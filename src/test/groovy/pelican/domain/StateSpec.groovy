package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Country
import io.hilo.State

import hilo.common.DomainMockHelper

class StateSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain State
	}

	void "test basic persistence mocking"() {
	    setup:
		def country = DomainMockHelper.getMockCountry()
		country.save(flush:true)
		
	    def state = DomainMockHelper.getMockState(country)
		state.save(flush:true)

	    expect:
	    State.count() == 1
	}
	
}