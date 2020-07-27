package hilo.interceptors

import hilo.common.ControllerConstants

class OrdersActiveInterceptor {

	OrdersActiveInterceptor(){
		match(controller:"transaction", action: ~/(list|show)/)
	}

    boolean before() { 
    	request.ordersActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
