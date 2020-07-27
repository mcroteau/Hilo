package io.hilo.log

import io.hilo.Catalog
import io.hilo.Account


class CatalogViewLog {
	
	CatalogViewLog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Catalog catalog
	Account account
	
	Date dateCreated
	Date lastUpdated
	
	
    static constraints = {
		uuid(nullable:true)
		catalog(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_CATALOG_VIEW_LOG_PK_SEQ']
    }
	
}
