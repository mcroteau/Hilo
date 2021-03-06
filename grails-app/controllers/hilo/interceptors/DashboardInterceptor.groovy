package hilo.interceptors

import hilo.common.ControllerConstants

class DashboardActiveInterceptor {

	DashboardActiveInterceptor() {
    	match(controller:"admin", action:"*")
  	}

    boolean before() { 
    	request.dashboardActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
