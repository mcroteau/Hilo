package hilo.interceptors

import hilo.common.ControllerConstants

class AccountsActiveInterceptor {

	AccountsActiveInterceptor(){
		match(controller:"account", action: ~/(admin_list|admin_create|admin_edit|admin_show)/)
	}

    boolean before() { 
    	request.accountsActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
