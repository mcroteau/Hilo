<%@ page import="io.hilo.Catalog" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()%>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Hilo :Payment Settings</title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
		.payment-configured{
			font-size:19px;
			padding:3px 10px;
			margin:0px 0px 10px 0px;
			display:inline-block;
			background:#efefef;
			border:solid 1px #ddd;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
</head>
<body>

	<h2><g:message code="payment.settings"/></h2>
	

	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default"><g:message code="store.settings"/></g:link></li>

		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default"><g:message code="email.settings"/></g:link></li>
		
		<li class="active"><g:link uri="/configuration/payment_settings" class="btn btn-default"><g:message code="payment.settings"/></g:link></li>
		
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default"><g:message code="shipping.settings"/></g:link></li>
	</ul>
	
	
	
	
	<form action="save_payment_settings" class="form-horizontal">

		<%
		def usd = payment_settings?.storeCurrency == "USD" ? "selected" : ""
		def gbp = payment_settings?.storeCurrency == "GBP" ? "selected" : ""
		def nzd = payment_settings?.storeCurrency == "NZD" ? "selected" : ""
		def cad = payment_settings?.storeCurrency == "CAD" ? "selected" : ""
		def eur = payment_settings?.storeCurrency == "EUR" ? "selected" : ""
		def brl = payment_settings?.storeCurrency == "BRL" ? "selected" : ""
		def inr = payment_settings?.storeCurrency == "INR" ? "selected" : ""
		def hkd = payment_settings?.storeCurrency == "HKD" ? "selected" : ""
		%>
	
		<div id="stripePaymentSettings">
	
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block"><g:message code="current.configuration"/></div>
				
				<div class="pull-left" style="display:inline-block; width:500px; margin-left:20px;">
					<a href="http://www.stripe.com" alt="Visit Stripe : Payments API Gateway" style="border:none;text-decoration:none"><img src="${resource(dir:'images/app', file:'stripe-logo.png')}" style="height:20%; width:20%;outline:none"/></a>
		
					<p class="information secondary" style="margin-top:0px !important;"><g:message code="stripe.description"/> <a href="http://stripe.com" target="_blank"><g:message code="visit.stripe"/></a>.</p>
				</div>
				<br class="clear">
			</div>
			


			<g:if test="${applicationService.getMultiCurrencyEnabled() == 'true'}">

				<div class="form-row">
					<div class="form-label twohundred pull-left" style="display:inline-block"><g:message code="base.currency"/></div>
									
					<div class="input-container pull-left" style="width:500px;">
						<select name="storeCurrency" class="form-control" style="width:423px;" id="currencySelectStripe">
							<option value="USD" <%=usd%>>$&nbsp;&nbsp;USD - United States</option>
							<option value="GBP" <%=gbp%>>£&nbsp;&nbsp;GBP - United Kingdom</option>
							<option value="NZD" <%=nzd%>>$&nbsp;&nbsp;NZD - New Zealand</option>
							<option value="CAD" <%=cad%>>C $&nbsp;&nbsp;CAD - Canada</option>
							<option value="EUR" <%=eur%>>€&nbsp;&nbsp;EUR - Germany, France, Netherlands, Ireland</option>
							<option value="HKD" <%=hkd%>>HK$ HKD - Hong Kong</option>
							<option value="BRL" <%=brl%>>(R$&nbsp;&nbsp;BRL - Brazil: Stripe by invite only)</option>
							<option value="INR" <%=inr%>>(₹ INR - India: Stripe by invite only)</option>
						</select>
						<br/>
						
						<br/>
						<p class="information secondary"><g:message code="stripe.description.first"/></p>
						
						<p class="information secondary"><g:message code="stripe.countries"/></p>
						
						<p class="information secondary"><g:message code="stripe.description.second"/></p>
						
						<p class="information">
							<a href="https://stripe.com/br/pricing" target="_blank"><g:message code="stripe.brazil"/></a><br/>
							<a href="https://stripe.com/global#signup-form" target="_blank"><g:message code="stripe.india"/></a><br/>
							<!--<a href="https://stripe.com/global#signup-form" target="_blank">Request an Invite (Mexico)</a>-->
						</p>
	    	
						<p class="information secondary"><g:message code="stripe.atlas.first"/> 
							<a href="https://stripe.com/atlas" target="_blank"><g:message code="atlas"/></a> <g:message code="stripe.atlas.second"/> <a href="https://stripe.com/atlas" target="_blank"><g:message code="request.invite"/></a></p>
				
						<p class="information secondary">Hilo, IOC or Mike Croteau are not liable for your configuration.</p>
					</div>
					<br class="clear"/>			
				</div>
				
				
				<div class="form-row">
					<div class="form-label twohundred pull-left" style="display:inline-block">Store Country</div>
				
					<div class="input-container pull-left" style="width:500px;">
						<select name="storeCountryCode" class="form-control" style="width:230px;" id="countrySelectStripe">
						</select>
						<br/>
						<br/>
						<p class="information secondary"><g:message code="store.currency.description"/></p>
					</div>
					<br class="clear"/>
				</div>
			
			</g:if>

    	
			<div class="form-row">
				<span class="form-label twohundred"><g:message code="development.secret.key"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeDevApiKey" value="${payment_settings.stripeDevApiKey}"/>
				</span>
				<br class="clear"/>
			</div>
    	
			<div class="form-row">
				<span class="form-label twohundred"><g:message code="development.publishable.key"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeDevPublishableKey" value="${payment_settings.stripeDevPublishableKey}"/>
				</span>
				<br class="clear"/>
			</div>
    		
    		<br/>
			
			<div class="form-row">
				<span class="form-label twohundred"><g:message code="live.secret.key"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeProdApiKey" value="${payment_settings.stripeProdApiKey}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<div class="form-row">
				<span class="form-label twohundred"><g:message code="live.publishable.key"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="stripeProdPublishableKey" value="${payment_settings.stripeProdPublishableKey}"/>
				</span>
				<br class="clear"/>
			</div>		
			
			
			
			<div class="buttons-container">
				<g:link controller="configuration" action="index" class="btn btn-default"><g:message code="cancel"/></g:link>
				<g:submitButton value="${message(code:'save.settings')}" name="submit" class="btn btn-primary"/>
			</div>
			
		</div>
		
		
		<div id="braintreePaymentSettings">
			
	
			<div class="form-row">
				<div class="form-label twohundred pull-left" style="display:inline-block"><g:message code="current.configuration"/></div>
				
				<div class="pull-left" style="display:inline-block; width:500px; margin-left:20px;">
					<a href="http://www.braintreepayments.com" alt="Visit Braintree : Payments API Gateway" style="border:none;text-decoration:none"><img src="${resource(dir:'images/app', file:'braintree-logo.png')}" style="height:30%; width:30%;outline:none"/></a>
		
					<p class="information secondary" style="margin-top:0px !important;"><g:message code="braintree.description"/><a href="http://braintreepayments.com" target="_blank"><g:message code="visit.braintree"/></a>.</p>
				</div>
				<br class="clear">
			</div>
			


			<g:if test="${applicationService.getMultiCurrencyEnabled() == 'true'}">
			
				<div class="form-row">
					<div class="form-label twohundred pull-left" style="display:inline-block"><g:message code="base.currency"/></div>

					
					<div class="input-container pull-left" style="width:500px;">
						<select name="storeCurrency" class="form-control" style="width:423px;" id="currencySelectBraintree">
							<option value="USD" <%=usd%>>$&nbsp;&nbsp;USD - United States</option>
							<option value="GBP" <%=gbp%>>£&nbsp;&nbsp;GBP - United Kingdom</option>
							<option value="NZD" <%=nzd%>>$&nbsp;&nbsp;NZD - New Zealand</option>
							<option value="CAD" <%=cad%>>C $&nbsp;&nbsp;CAD - Canada</option>
							<option value="EUR" <%=eur%>>€&nbsp;&nbsp;EUR - Germany, France, Netherlands, Ireland, Greece</option>
							<option value="HKD" <%=hkd%>>HK$ HKD - Hong Kong</option>
							<option value="BRL" <%=brl%>>(R$&nbsp;&nbsp;BRL - Brazil: Stripe by invite only)</option>
							<option value="INR" <%=inr%>>(₹ INR - India: Stripe by invite only)</option>
						</select>
						<br/>
						
						<br/>
						<p class="information secondary"><g:message code="braintree.serves"/></p>
						
						<p class="information secondary"><g:message code="braintree.description2"/></p>
						
						<p class="information secondary">Hilo, DataTundra, MGi Data Source or Mike Croteau are not liable for your configuration.</p>
					</div>
					<br class="clear"/>			
				</div>
				
				
				<div class="form-row">
					<div class="form-label twohundred pull-left" style="display:inline-block">Store Country</div>
				
					<div class="input-container pull-left" style="width:500px;">
						<select name="storeCountryCode" class="form-control" style="width:230px;" id="countrySelectBraintree">
						</select>
						<br/>
						<br/>
						<p class="information secondary"><g:message code="store.currency.description"/></p>
					</div>
					<br class="clear"/>
				</div>
				
			</g:if>

			
			<div class="form-row">
				<span class="form-label twohundred"><g:message code="merchant.id"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="braintreeMerchantId" value="${payment_settings.braintreeMerchantId}"/>
				</span>
				<br class="clear"/>
			</div>
			

			<div class="form-row">
				<span class="form-label twohundred"><g:message code="public.key"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="braintreePublicKey" value="${payment_settings.braintreePublicKey}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<div class="form-row">
				<span class="form-label twohundred"><g:message code="private.key"/></span>
				<span class="input-container">
					<input type="text" class="form-control threehundred" name="braintreePrivateKey" value="${payment_settings.braintreePrivateKey}"/>
				</span>
				<br class="clear"/>
			</div>		
			
			
			<div class="buttons-container">
				<g:link controller="configuration" action="index" class="btn btn-default"><g:message code="cancel"/></g:link>
				<g:submitButton value="${message(code:'save.settings')}" name="submit" class="btn btn-primary"/>
			</div>
			
		</div>
		
		<style type="text/css">
			#multi-currency-maintenance{
				margin-top:10px;
			}

			#multi-currency-maintenance a{
				color:#1b1b1b;
				opacity:0.31;
				margin-right:30px;
			}
			#multi-currency-maintenance a:hover{
				opacity:0.92 !important;
			}

			#select-gateway{
				float:right;
				text-align:right;
				margin-top:70px;
			}
			#select-gateway a{
				opacity:0.3;
			}
			#select-gateway a:hover{
				opacity:0.92 !important;
				text-decoration:none;
			}

			#select-gateway img{
				outline:none;
			}
		</style>
			
		<div id="select-gateway">
			<g:link action="select_gateway">
				<span id="select-gateway-text"><g:message code="set.payment.gateway"/></span>
				<img src="${resource(dir:'images/app', file:'gear.png')}"/>
			</g:link>


			<div id="multi-currency-maintenance">
				<g:if test="${applicationService.getMultiCurrencyEnabled() == 'false'}">
					<g:link action="enable_multicurrency"><g:message code="enable.multi.currency"/></g:link>
				</g:if>
				<g:else>
					<g:link action="disable_multicurrency"><g:message code="disable.multi.currency"/></g:link>
				</g:else>
			</div>
		</div>

		<br class="clear"/>
		
	</form>
	
	<script type="text/javascript">
		
		var currenciesMapStripe = {
			"USD" : [{ "code": "us", "name" : "United States" }],
			"GBP" : [{ "code": "gb", "name" : "United Kingdom" }],
			"NZD" : [{ "code": "nz", "name" : "New Zealand" }],
			"CAD" : [{ "code": "ca", "name" : "Canada" }],
			"EUR" : [
					{ "code": "de", "name" : "Germany" },
					{ "code": "fr", "name" : "France" },
					{ "code": "nl", "name" : "Netherlands" },
					{ "code": "ie", "name" : "Ireland" }
			],
			"BRL" : [{ "code": "br", "name" : "Brazil" }],
			"INR" : [{ "code": "in", "name" : "India" }],
			"HKD" : [{ "code": "hk", "name" : "Hong Kong" }]
		}
		
		var countryMapStripe = {
			"us" : { "code": "us", "name" : "United States" },
			"gb" : { "code": "gb", "name" : "United Kingdom" },
			"nz" : { "code": "nz", "name" : "New Zealand" },
			"ca" : { "code": "ca", "name" : "Canada" },
			"de" : { "code": "de", "name" : "Germany" },
			"fr" : { "code": "fr", "name" : "France" },
			"nl" : { "code": "nl", "name" : "Netherlands" },
			"ie" : { "code": "ie", "name" : "Ireland" },
			"br" : { "code": "br", "name" : "Brazil" },
			"in" : { "code": "in", "name" : "India" },
			"hk" : { "code": "hk", "name" : "Hong Kong" }
		}
		
		
		
		var currenciesMapBraintree = {
			"USD" : [{ "code": "us", "name" : "United States" }],
			"GBP" : [{ "code": "gb", "name" : "United Kingdom" }],
			"NZD" : [{ "code": "nz", "name" : "New Zealand" }],
			"CAD" : [{ "code": "ca", "name" : "Canada" }],
			"EUR" : [
					{ "code": "de", "name" : "Germany" },
					{ "code": "fr", "name" : "France" },
					{ "code": "nl", "name" : "Netherlands" },
					{ "code": "ie", "name" : "Ireland" },
					{ "code": "gr", "name" : "Greece" }
			]
		}
		
		var countryMapBraintree = {
			"us" : { "code": "us", "name" : "United States" },
			"gb" : { "code": "gb", "name" : "United Kingdom" },
			"nz" : { "code": "nz", "name" : "New Zealand" },
			"ca" : { "code": "ca", "name" : "Canada" },
			"de" : { "code": "de", "name" : "Germany" },
			"fr" : { "code": "fr", "name" : "France" },
			"nl" : { "code": "nl", "name" : "Netherlands" },
			"ie" : { "code": "ie", "name" : "Ireland" },
			"gr" : { "code": "gr", "name" : "Greece" }
		}
		
		
		$(document).ready(function(){
			var countryCode = "${payment_settings?.storeCountryCode}"
			//console.log("here...", countryCode)

			var $stripePaymentSettings = $("#stripePaymentSettings");
			var $braintreePaymentSettings = $("#braintreePaymentSettings");

			var $countrySelectStripe = $("#countrySelectStripe"),
				$currencySelectStripe = $("#currencySelectStripe"),
				$countrySelectBraintree = $("#countrySelectBraintree"),
				$currencySelectBraintree = $("#currencySelectBraintree");
			
			$currencySelectStripe.change(updateCountrySelect(currenciesMapStripe, $countrySelectStripe, $currencySelectStripe))
			$currencySelectBraintree.change(updateCountrySelect(currenciesMapBraintree, $countrySelectBraintree, $currencySelectBraintree))
			
			function updateCountrySelect(currenciesMap, $countrySelect, $currencySelect){
				return function(event){
					$countrySelect.find("option").remove()
					
					var currency = $currencySelect.val()
					//console.log(currency)
					var countries = currenciesMap[currency]
					
					$(countries).each(function(o, q){
						//console.log(o, q)
						$countrySelect.append("<option value=\"" + q.code + "\">" + q.name + "</option>");
					})
				}
			}
			
			function setCountryCode($countrySelect, countryCodeMap){
				var selected = countryCodeMap[countryCode]
				$countrySelect.append("<option value=\"" + selected.code + "\">" + selected.name + "</option>");
			}
			
			
			<%if(applicationService.getBraintreeEnabled() == "true"){%>
				setCountryCode($countrySelectBraintree, countryMapBraintree);
				//setCurrencyCode($currencySelectBraintree, currenciesMapBraintree);
				$stripePaymentSettings.hide()
				$braintreePaymentSettings.show()
				$currencySelectStripe.prop("disabled", true)
			<%}else{%>
				setCountryCode($countrySelectStripe, countryMapStripe);
				//setCurrencyCode($currencySelectStripe, currenciesMapStripe);
				$stripePaymentSettings.show()
				$braintreePaymentSettings.hide()
				$currencySelectBraintree.prop("disabled", true)
			<%}%>
		})
	</script>
		
</body>
</html>