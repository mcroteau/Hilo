package io.hilo

class BlogPost {
	
	BlogPost(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	String title
	String content
	String meta
	
	Layout layout

	Date dateCreated
	Date lastUpdated

    static constraints = {
		uuid(nullable:true)
		title(unique:true, nullable:false)
		content(nullable:false, size:0..65535)	
		meta(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_BLOG_POST_PK_SEQ']
    }

}