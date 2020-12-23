package www.domain.com.paypal.capture;

import com.paypal.orders.OrderRequest;
import com.paypal.payments.AuthorizationsCaptureRequest;
import com.paypal.payments.CaptureRequest;

public class PaypalCaptureRequestBuilder {
	
	private String authorizationId;
	
	public PaypalCaptureRequestBuilder withAuthorizationId(String authorizationId) {
		this.authorizationId = authorizationId;
		return this;
	}
	
	public CaptureRequest buildRequestBody() {
		return new CaptureRequest();
	}
	
	public AuthorizationsCaptureRequest build() {
		return new AuthorizationsCaptureRequest(authorizationId)
					.requestBody(buildRequestBody());
	}

}
