import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.i18n.FixedLocaleResolver
import io.hilo.handlers.HiloAuthenticationSuccessHandler
import io.hilo.exception.BaseException
import io.hilo.common.CommonUtilities


beans = {
	commonUtilities(CommonUtilities)
	exceptionHandler(io.hilo.exception.BaseException) {
	    exceptionMappings = ['java.lang.Exception': '/error']
	}
	authenticationSuccessHandler(HiloAuthenticationSuccessHandler) {
		//https://groggyman.com/2015/04/05/custom-authentication-success-handler-with-grails-and-spring-security/
        requestCache = ref('requestCache')
        redirectStrategy = ref('redirectStrategy')
	}
    localeResolver(SessionLocaleResolver) {
        defaultLocale= new Locale('en')
    }
}
