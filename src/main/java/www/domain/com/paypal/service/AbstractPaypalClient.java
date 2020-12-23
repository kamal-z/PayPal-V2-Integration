package www.domain.com.paypal.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.paypal.core.PayPalEnvironment;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersAuthorizeRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.payments.AuthorizationsCaptureRequest;
import com.paypal.payments.Capture;
import com.paypal.http.HttpResponse;
import www.domain.com.paypal.authorization.AuthorizeOrderResult;
import www.domain.com.paypal.authorization.PaypalAhthorizeRequestBuilder;
import www.domain.com.paypal.capture.PaypalCaptureRequestBuilder;
import www.domain.com.paypal.createorder.CreatedOrderResult;
import www.domain.com.paypal.createorder.PaypalOrderRequestBuilder;

abstract class AbstractPaypalClient implements PaypalClient {

	private static final String CLASSNAME = AbstractPaypalClient.class.getName();

	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	private final String clientId;
	private final String secretId;
	private final PaypalProxyHttpClient httpClient;

	public AbstractPaypalClient(String clientId, String secretId) {
		this.clientId = clientId;
		this.secretId = secretId;
		PayPalEnvironment environment = createEnvironment();
		this.httpClient = new PaypalProxyHttpClient(environment);

	}

	protected abstract PayPalEnvironment createEnvironment();

	@Override
	public CreatedOrderResult sendCreatedOrderRequest(PaypalOrderRequestBuilder paypalOrderRequestBuilder)
			throws HttpException {
		final String methodName = "sendCreatedOrderResult";

		OrdersCreateRequest orderCreateRequest = new OrdersCreateRequest()
				.requestBody(paypalOrderRequestBuilder.build());

		try {
			HttpResponse<Order> response = httpClient.execute(orderCreateRequest);
			for (LinkDescription link : response.result().links()) {
				LOGGER.log(Level.INFO, "ReponseLink: rel:{0} => method:{1} = {2}",
						new Object[] { link.rel(), link.method(), link.href() });
			}
		return new CreatedOrderResult(response.result());
			 
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "Create Order Request could not be executed - Stacktrace",
					e);
		}

		return null;
	}


	public String getClientId() {
		return clientId;
	}

	public String getSecretId() {
		return secretId;
	}

	@Override
	public AuthorizeOrderResult sendOrderAuthorizeRequest(String paypalOrderId, String paypalRequestId, PaypalAhthorizeRequestBuilder orderAhthorizeRequestBuilder) throws HttpException {
		String methodName = "sendOrderAuthorizeRequest(String paypalOrderId, String paypalRequestId, PaypalAhthorizeRequestBuilder orderAhthorizeRequestBuilder)";

		OrdersAuthorizeRequest orderAuthorizeRequest = orderAhthorizeRequestBuilder.withPaypalOrderId(paypalOrderId)
				.withPaypalRequestId(paypalRequestId).build();

		LOGGER.logp(Level.INFO, CLASSNAME, methodName, "Execution order authorize {0}", orderAuthorizeRequest);

		try {
			HttpResponse<Order> response = httpClient.execute(orderAuthorizeRequest);
			AuthorizeOrderResult AuthorizeOrderResult =  new AuthorizeOrderResult(response.result(), response.statusCode());
			return  AuthorizeOrderResult;
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "Order Ahrorize Request could not be executed - Stacktrace",e);
			return null;
		}
	}
	
	@Override
	public void sendOrderCaptureRequest(String authorizationId, PaypalCaptureRequestBuilder orderCaptureRequestBuilder) throws HttpException   {
		String methodName = "sendOrderCaptureRequest(String authorizationId, PaypalCaptureRequestBuilder orderCaptureRequestBuilder)";
		
		AuthorizationsCaptureRequest captureRequest =  orderCaptureRequestBuilder.withAuthorizationId(authorizationId).build();
		
		LOGGER.logp(Level.INFO, CLASSNAME, methodName, "Execution order capture {0}", captureRequest);
		
		try {
			HttpResponse<Capture> response = httpClient.execute(captureRequest);
		} catch (HttpException e) {
			throw e;
		} catch (IOException e) {
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "Order Ahrorize Request could not be executed - Stacktrace",e);
		}
		
	}

}
