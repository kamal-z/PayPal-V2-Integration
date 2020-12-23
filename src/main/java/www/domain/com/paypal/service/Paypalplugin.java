package www.domain.com.paypal.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.paypal.http.exceptions.HttpException;

import www.domain.com.paypal.PaypalConfiguration;
import www.domain.com.paypal.authorization.AuthorizeOrderResult;
import www.domain.com.paypal.authorization.PaypalAhthorizeRequestBuilder;
import www.domain.com.paypal.capture.PaypalCaptureRequestBuilder;
import www.domain.com.paypal.createorder.CreatedOrderResult;
import www.domain.com.paypal.createorder.PaypalOrderRequestBuilder;

@Component
public class Paypalplugin {
	
	@Autowired
	private PaypalConfiguration paypalConfiguration;

	private  PaypalClient paypalClient;
	
	private PaypalOrderRequestBuilder paypalOrderRequestBuilder ;
	
	
	 /**
	   * In PayPal v2 protocol this corresponds to CreateOrderRequenst and CreateOrderResponse
	 * @throws HttpException 
	  */
	
	public CreatedOrderResult createPaypalPaymentSession(int orderId) throws HttpException {
		 String successRedirectUrlWithParam = paypalConfiguration.getSuccessRedirectUrl().replaceFirst("value", String.valueOf(orderId));
		 paypalClient = PaypalClientProvider.providePaypalClient(paypalConfiguration.getMode(), paypalConfiguration.getCurrentCleintId(), paypalConfiguration.getCurrentSecrerId());
		
		 paypalOrderRequestBuilder = new PaypalOrderRequestBuilder(String.valueOf(orderId),new BigDecimal("0.02"),"EUR")
																	.withSuccessRedirectUrl(successRedirectUrlWithParam)
																	.withCancelRedirectUrl(paypalConfiguration.getCancelRedirectUrl());
		
		return  paypalClient.sendCreatedOrderRequest(paypalOrderRequestBuilder);
		
	}
	
	public AuthorizeOrderResult authorizePaypalOrder(String paypalOrderId, String paypalRequestId, PaypalAhthorizeRequestBuilder orderAhthorizeRequestBuilder ) throws HttpException {
		return this.paypalClient.sendOrderAuthorizeRequest(paypalOrderId, paypalRequestId, orderAhthorizeRequestBuilder);
	}
	
	public void captureOrder(String authorizationId, PaypalCaptureRequestBuilder captureRequestBuilder) throws HttpException {
		this.paypalClient.sendOrderCaptureRequest(authorizationId, captureRequestBuilder);
	}

}
