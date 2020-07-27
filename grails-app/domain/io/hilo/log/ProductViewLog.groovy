package io.hilo.log

import io.hilo.Product
import io.hilo.Account


class ProductViewLog {
	
	ProductViewLog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Product product
	Account account
	
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		uuid(nullable:true)
		product(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_VIEW_LOG_PK_SEQ']
    }
	
}
