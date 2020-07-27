
<%@ page import="io.hilo.Transaction" %>
<%@ page import="io.hilo.ApplicationService" %>
<%@ page import="io.hilo.common.OrderStatus" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('io.hilo.CurrencyService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="show.transaction"/></title>
	</head>
	<body>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:20px;">${flash.message}</div>
	</g:if>
	
	

	
	<h1><g:message code="order"/> # : ${transactionInstance.id}</h1>
	
	<g:form>
		<g:hiddenField name="id" value="${transactionInstance?.id}" />

		<g:actionSubmit class="btn btn-danger pull-right" action="delete" value="${message(code:'delete')}" onclick="return confirm('Are you sure?');" />

		<div style="height:20px; width:10px; display:inline-block" class="pull-right"></div>

		<g:link controller="transaction" action="list" class="btn btn-default pull-right"><g:message code="order.history"/></g:link>
		
		
		<g:if test="${transactionInstance.shoppingCart?.shipmentId &&
				transactionInstance.shoppingCart?.shipmentRateId &&
				transactionInstance.status != 'REFUNDED'}">
			<g:if test="${transactionInstance.postageId}">
				<g:link action="print_shipping_label" id="${transactionInstance.id}" class="btn btn-default pull-right" style="margin-right:10px;" target="_blank"><g:message code="print.shipping.label"/></g:link>
			</g:if>
			<g:else>
				<g:link action="confirm_purchase_shipping_label" id="${transactionInstance.id}" class="btn btn-default pull-right" style="margin-right:10px;"><g:message code="buy.shipping.label"/></g:link>
			</g:else>
		</g:if>
		
	</g:form>
	
	
	
	
	
	<div class="form-row">
		<span class="form-label minimum hint"><g:message code="order.date"/>:&nbsp;</span>
		<span class="input-container">
			<g:formatDate format="dd MMM yyyy hh:mm z" date="${transactionInstance.orderDate}"/>
		</span>
		<br class="clear"/>
	</div>
	
	
	<g:form controller="transaction" action="update_status" id="${transactionInstance.id}" method="post">
		<input type="hidden" id="current-status" value="${transactionInstance.status}" />
		<div class="form-row">

			<span class="form-label minimum hint"><g:message code="status"/>:&nbsp;</span>
		
			<span class="input-container">
				<g:if test="${transactionInstance.status == 'REFUNDED'}">
					${transactionInstance.status}
				</g:if>
				<g:else>
					<g:select from="${OrderStatus.values()}" 
						optionKey="description" 
						name="status" value="${transactionInstance.status}" 
						class="form-control" 
						style="width:150px; display:inline-block" 
						id="status-select"/>
					<input type="submit" value="${message(code:'update.status')}" id="update-status" class="btn btn-primary"> 
				</g:else>
			</span>
			<br class="clear"/>
		</div>

		<div class="form-row">
			<span class="form-label minimum hint"><g:message code="gateway"/>:&nbsp;</span>
			<span class="input-container">
				${transactionInstance.gateway}
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label minimum hint"><g:message code="transaction.id"/>:&nbsp;</span>
			<span class="input-container">
				${transactionInstance.chargeId}
			</span>
			<br class="clear"/>
		</div>

		<g:if test="${transactionInstance.status == 'REFUNDED'}">
			<div class="form-row">
				<span class="form-label minimum hint"><g:message code="refund.id"/>:&nbsp;</span>
				<span class="input-container">
					${transactionInstance.refundChargeId}
				</span>
				<br class="clear"/>
			</div>
		</g:if>

		<br class="clear"/>
	</g:form>
	
	
	
	<table class="table table-condensed">
		<thead>
			<tr>
				<th style="text-align:center"><g:message code="item"/>#</th>
				<th><g:message code="name"/></th>
				<th style="text-align:center"><g:message code="price"/></th>
				<th style="text-align:center"><g:message code="quantity"/></th>
				<th style="text-align:center"><g:message code="extended.price"/></th>
			</tr>
		</thead>		
		<tbody>
			<% total = 0 %>
			<g:each in="${transactionInstance.shoppingCart?.shoppingCartItems}" status="i" var="item">
				<%
					def optionsTotal = 0
					if(item.shoppingCartItemOptions?.size() > 0){
						item.shoppingCartItemOptions.each(){ option ->
							optionsTotal += option.checkoutPrice
						}	
					}
					def productTotal = item.checkoutPrice + optionsTotal
					def extendedPrice = productTotal * item.quantity
				%>
				
				<tr>
					<td style="text-align:center"><g:link controller="product" action="edit" id="${item.product.id}">${item.product.id}</g:link></td>
					<td><g:link controller="product" action="edit" id="${item.product.id}">${item.product.name}</g:link>
						<br/>
						<g:if test="${item.shoppingCartItemOptions?.size() > 0}">
							<div style="font-size:11px; color:#777">
								<strong>options :&nbsp;</strong>
								<g:each in="${item.shoppingCartItemOptions}" var="option">
									<span class="option" style="font-size: 11px;color: #777;">${option.variant.name}
										(${currencyService.format(applicationService.formatPrice(option.checkoutPrice))})
									</span>
									<br/>
								</g:each>
							</div>
						</g:if>
					</td>
					<td style="text-align:center">
                        <g:if test="${item.regularPrice != item.checkoutPrice}">
							${currencyService.format(applicationService.formatPrice(item.checkoutPrice))}
                            <span class="regular-price">${currencyService.format(applicationService.formatPrice(item.regularPrice))}</span>
                        </g:if>
						<g:else>
							${currencyService.format(applicationService.formatPrice(item.checkoutPrice))}
						</g:else>
					</td>
					<td style="text-align:center">${item.quantity}</td>
					<td id="extended_price" style="text-align:center">${currencyService.format(applicationService.formatPrice(extendedPrice))}</td>
				</tr>
			</g:each>
			<tr>
				<td colspan="4" style="text-align:right;">Subtotal</td>
				<td style="text-align:center; ">${currencyService.format(applicationService.formatPrice(transactionInstance.subtotal))}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right; font-size:12px">Taxes</td>
				<td style="text-align:center; font-size:12px;">${currencyService.format(applicationService.formatPrice(transactionInstance.taxes))}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-size:12px">Shipping</td>
				<td  style="text-align:center;font-size:12px">${currencyService.format(applicationService.formatPrice(transactionInstance.shipping))}</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align:right;font-weight:bold;">Total</td>
				<td style="font-weight:bold; font-size:17px;text-align:center">${currencyService.format(applicationService.formatPrice(transactionInstance.total))}</td>
			</tr>
		</tbody>
	</table>
	
	<style type="text/css">
		.billing-address,
		.shipping-address{
			float:left;
			margin:20px 50px 20px 0px ;
		}
	</style>
	
	<div class="shipping-address">
		<h3><g:message code="shipping"/></h3>
		<address>
			<strong>${transactionInstance?.shipName}</strong><br/>
			${transactionInstance?.shipAddress1}<br/>
			<g:if test="${transactionInstance.shipAddress2}">
				${transactionInstance.shipAddress2}<br/>
			</g:if>
			${transactionInstance?.shipCity}
			<g:if test="${transactionInstance.shipState}">
				, ${transactionInstance.shipState.name}<br/>
			</g:if>
			<g:if test="${transactionInstance.shipCountry}">
				<br/>${transactionInstance.shipCountry.name}<br/>
			</g:if>
			${transactionInstance?.shipZip}
			<br/>
			<br/>
			<strong><g:message code="email"/>:</strong>&nbsp;${transactionInstance?.account.email}<br/>
			<g:if test="${transactionInstance.account.phone}">
				<strong>phone</strong>:&nbsp;${transactionInstance?.account.phone}
			</g:if>
		</address>
	</div>
	
	<g:if test="${transactionInstance.status != 'REFUNDED'}">
		<g:link controller="transaction" action="confirm_refund" class="btn btn-default pull-right" id="${transactionInstance.id}">Refund Order</g:link>
	</g:if>
	
	
	<g:form controller="transaction" action="send_confirmation" id="${transactionInstance.id}" method="POST">
		<input type="submit" class="btn btn-default pull-right" value="${message(code:'send.email.confirmation')}" />
	</g:form>
	
	
	<br style="clear:both"/>
	
<script type="text/javascript">
$(document).ready(function(){
	var $statusSel = $('#status-select'),
		$updateBtn = $('#update-status');
	
	var status = $('#current-status').val();
		
	$updateBtn.attr('disabled', 'disabled');
	$statusSel.change(checkEnableDisableSelect)


	function checkEnableDisableSelect(event){
		var $target = $(event.target);
		var selectedStatus = $target.val()
		if(selectedStatus == status){
			$updateBtn.attr('disabled', 'disabled');
		}else{
			$updateBtn.removeAttr('disabled');
		}
	}
	
});	
	
</script>

</body>
</html>
