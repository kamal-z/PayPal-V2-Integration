package www.domain.com.paypal.service;


import com.paypal.http.exceptions.HttpException;
import www.domain.com.paypal.authorization.AuthorizeOrderResult;
import www.domain.com.paypal.authorization.PaypalAhthorizeRequestBuilder;
import www.domain.com.paypal.capture.PaypalCaptureRequestBuilder;
import www.domain.com.paypal.createorder.CreatedOrderResult;
import www.domain.com.paypal.createorder.PaypalOrderRequestBuilder;

public interface PaypalClient {
	
	
	CreatedOrderResult sendCreatedOrderRequest(PaypalOrderRequestBuilder PaypalRequestBuilder) throws HttpException;
	
	AuthorizeOrderResult sendOrderAuthorizeRequest(String paypalOrderId, String paypalRequestId, PaypalAhthorizeRequestBuilder orderAhthorizeRequestBuilder) throws HttpException;
 
	void sendOrderCaptureRequest(String authorizationId, PaypalCaptureRequestBuilder  CaptureRequestBuilder) throws HttpException;
}
