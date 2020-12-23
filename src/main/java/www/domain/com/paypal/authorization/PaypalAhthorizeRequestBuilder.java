package www.domain.com.paypal.authorization;

import com.paypal.orders.AuthorizeRequest;
import com.paypal.orders.OrdersAuthorizeRequest;

public class PaypalAhthorizeRequestBuilder {
	
	public String paypalOrderId;
	public String paypalRequestId;
	
	public OrdersAuthorizeRequest build() {
		return new OrdersAuthorizeRequest(paypalOrderId)
				.payPalRequestId(paypalRequestId)
				.requestBody(buildBody());
		
	}
	
	public AuthorizeRequest buildBody() {
		return new AuthorizeRequest();
	}
	
	
	public PaypalAhthorizeRequestBuilder withPaypalOrderId(String paypalOrderId) {
		this.paypalOrderId=paypalOrderId;
		return this;
	}
	
	public PaypalAhthorizeRequestBuilder withPaypalRequestId(String paypalRequestId) {
		this.paypalRequestId=paypalRequestId;
		return this;
	}

}
