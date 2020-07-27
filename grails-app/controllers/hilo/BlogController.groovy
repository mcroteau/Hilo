package hilo

import grails.plugin.springsecurity.annotation.Secured

import io.hilo.BlogPost
import io.hilo.Layout

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class BlogController {

	MessageSource messageSource

 	@Secured(['permitAll'])
	def index(){
		def offset = params.offset ? params.offset : 0
		def posts = BlogPost.list([max: 7, offset: offset, order: 'dateCreated'])
		[ posts: posts, postsTotal: BlogPost.count()  ]
	}


 	@Secured(['ROLE_ADMIN'])
	def list(){
    	params.max = 10
    	[ posts: BlogPost.list(params), postsTotal: BlogPost.count() ]
	}


 	@Secured(['ROLE_ADMIN'])
	def create(){
		def layouts = Layout.list()
		//def categories = BlogCategory.list()
		// if(!categories){
		// 	flash.message = "No blog categories found, you must create at least one category in order to continue."
		// 	redirect(action:"create_category")
		// 	return
		// }

		[ layouts: layouts ]
	}


 	@Secured(['ROLE_ADMIN'])
	def save(){
    	def postInstance = new BlogPost(params)
    	if (!postInstance.save(flush: true)) {
			def layouts = Layout.list()
			//def categories = BlogCategory.list()
			flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
    	    render(view: "create", model: [postInstance: postInstance, layouts: layouts ])
    	    return
    	}
    	
    	flash.message = messageSource.getMessage("successfully.saved", null, LocaleContextHolder.locale)
    	redirect(action: "list")
	}


 	@Secured(['ROLE_ADMIN'])
	def edit(Long id){
		def postInstance = BlogPost.get(id)
		if(!postInstance){
			flash.message = messageSource.getMessage("blog.post.not.found", null, LocaleContextHolder.locale)
			redirect(action: "list")
			return
		}
		def layouts = Layout.list()
		//def categories = BlogCategory.list()
		// if(!categories){
		// 	flash.message = "No blog categories found, you must create at least one category in order to continue."
		// 	redirect(action:"manage")
		// 	return
		// }

		[ postInstance: postInstance, layouts: layouts ]
	}


 	@Secured(['ROLE_ADMIN'])
	def update(Long id){

		def postInstance = BlogPost.get(id)
		if(!postInstance){
			flash.message = messageSource.getMessage("blog.post.not.found", null, LocaleContextHolder.locale)
			redirect(action: "list")
			return
		}

		postInstance.properties = params
        	
    	if (!postInstance.save(flush: true)) {
			flash.message = messageSource.getMessage("something.went.wrong.message", null, LocaleContextHolder.locale)
    	    render(view: "edit", model: [postInstance: postInstance])
    	    return
    	}
    	
    	flash.message = messageSource.getMessage("successfully.updated", null, LocaleContextHolder.locale)
    	redirect(action: "list")
	}


 	@Secured(['ROLE_ADMIN'])
	def delete(Long id){
		def postInstance = BlogPost.get(id)
		if(!postInstance){
			flash.message = messageSource.getMessage("blog.post.not.found", null, LocaleContextHolder.locale)
			redirect(action: "list")
			return
		}

		postInstance.delete(flush:true)
		flash.message = messageSource.getMessage("successfully.deleted", null, LocaleContextHolder.locale)
		redirect(action: "list")
	}


 	@Secured(['permitAll'])
	def entries(){
    	params.max = 7
    	[ posts: BlogPost.list(params), postsTotal: BlogPost.count() ]
	}


 	@Secured(['permitAll'])
	def view(Long id){
		def postInstance = BlogPost.get(id)
		if(!postInstance){
			flash.message = messageSource.getMessage("blog.post.not.found", null, LocaleContextHolder.locale)
			redirect(action: "list")
			return
		}
		[ postInstance: postInstance ]
	}




}