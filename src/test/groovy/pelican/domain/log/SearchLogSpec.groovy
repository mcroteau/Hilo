package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Account
import io.hilo.Layout
import io.hilo.log.SearchLog

import hilo.common.DomainMockHelper

class SearchLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain SearchLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def searchlog = DomainMockHelper.getMockSearchLog(account)
		searchlog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    SearchLog.count() == 1
	}

}