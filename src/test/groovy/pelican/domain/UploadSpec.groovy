package hilo.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.hilo.Upload

import hilo.common.DomainMockHelper

class UploadSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Upload
	}

	void "test basic persistence mocking"() {
	    setup:
	    def upload = DomainMockHelper.getMockUpload()
		upload.save(flush:true)

	    expect:
	    Upload.count() == 1
	}
	
}