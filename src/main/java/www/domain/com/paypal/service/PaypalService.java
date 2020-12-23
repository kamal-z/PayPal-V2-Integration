package www.domain.com.paypal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.paypal.http.exceptions.HttpException;

import www.domain.com.paypal.authorization.AuthorizeOrderResult;
import www.domain.com.paypal.authorization.PaypalAhthorizeRequestBuilder;
import www.domain.com.paypal.capture.PaypalCaptureRequestBuilder;
import www.domain.com.paypal.createorder.CreatedOrderResult;

@Service
public class PaypalService {
	
	 private static final String CLASSNAME = PaypalService.class.getName();

	  private static final Logger LOGGER = Logger.getLogger(CLASSNAME);
	
	@Autowired
	private Paypalplugin paypalplugin;
	
	@Autowired
	private OrderService orderService;
	
	
	public CreatedOrderResult startPayment() throws HttpException {
		int orderId = orderService.getOrderId();
		CreatedOrderResult createdOrderResult =  paypalplugin.createPaypalPaymentSession(orderId);
		if (createdOrderResult != null ) {
			PaymentInstractionDataService.paymentData.put(String.valueOf(orderId),createdOrderResult.getPaypalOrderId());
		}
		return  createdOrderResult;
	
	}
	
	public AuthorizeOrderResult AuthrizePaypalOrder(String paypalOrderId, String paypalRequestId, PaypalAhthorizeRequestBuilder orderAhthorizeRequestBuilder) throws HttpException {
		return paypalplugin.authorizePaypalOrder(paypalOrderId, paypalRequestId, orderAhthorizeRequestBuilder);
	}
	
	
	public void captureOrder(String authorizationId, PaypalCaptureRequestBuilder captureRequestBuilder) throws HttpException {
		paypalplugin.captureOrder(authorizationId, captureRequestBuilder);
	}
	
	
	 

}
