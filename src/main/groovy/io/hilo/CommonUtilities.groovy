package io.hilo.common

import grails.io.IOUtils

import java.io.FileOutputStream;
import java.io.FileInputStream

import grails.util.Holders

class CommonUtilities {
	
	def grailsApplication
	def springSecurityService
	
	
	CommonUtilities(){
		if(!grailsApplication){
			grailsApplication = Holders.grailsApplication
		}
		if(!springSecurityService){
		    springSecurityService = grailsApplication.classLoader.loadClass("grails.plugin.springsecurity.SpringSecurityService").newInstance()
		}
	}


	def getAuthenticatedAccount(){
		def username = springSecurityService.principal.username
		def account = Account.findByUsername(username)
		return account
	}
	
	def randomNumber(min, max){
		def random = new Random()
		def n = random.nextInt(max)
		if( n + min > max){
			return max
		}else{
			return n + min
		}
	}

	def generateRandomNumber(min, max){
		def random = new Random()
		def n = random.nextInt(max)
		if( n + min > max){
			return max
		}else{
			return n + min
		}
	}

	public randomString(int n){
		def alphabet = (('a'..'z')+('A'..'Z')+('0'..'9')).join()
		new Random().with {
		  	(1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
		}
	}

	public generateRandomString(int n){
		def alphabet = (('a'..'z')+('A'..'Z')+('0'..'9')).join()
		new Random().with {
		  	(1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
		}
	}
	
	def generateFileName(file){
		def fullFileName = file.getOriginalFilename()
		println "fullFileName : ${fullFileName}"
		
		String[] nameSplit = fullFileName.toString().split("\\.")
		def fileName = generateRandomString(9)
			
		println "extension : ${nameSplit[nameSplit.length - 1]}"
		def extension = nameSplit[nameSplit.length - 1]
	
		def newFileName = "${fileName}.${extension}"
		
		println "generateFileName ${newFileName}"
		return newFileName
	}


	def nullToBlankCheck(value){
		if(value == null)return ""
		return value.toString()
	}
	

}