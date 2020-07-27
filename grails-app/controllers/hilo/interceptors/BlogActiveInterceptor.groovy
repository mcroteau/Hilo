package hilo.interceptors

import hilo.common.ControllerConstants

class BlogActiveInterceptor {

	BlogActiveInterceptor(){
		match(controller:"blog", action: ~/(list|create|edit)/)
	}

    boolean before() { 
    	request.blogActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {}
}