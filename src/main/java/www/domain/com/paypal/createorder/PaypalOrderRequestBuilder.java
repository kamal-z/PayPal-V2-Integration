package www.domain.com.paypal.createorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.PurchaseUnitRequest;

public class PaypalOrderRequestBuilder {

	private String orderId;
	private String requestId;
	private AmountWithBreakdown requestedAmount;
	private String successRedirectUrl;
	private String cancelRedirectUrl;

	public PaypalOrderRequestBuilder(String orderId, BigDecimal requestedAmount, String currency) {
		this.orderId = orderId;
		this.requestedAmount = new AmountWithBreakdown().currencyCode(currency).value(requestedAmount.toPlainString());

	}

	public PaypalOrderRequestBuilder withCancelRedirectUrl(String cancelRedirectUrl) {
		this.cancelRedirectUrl = cancelRedirectUrl;
		return this;
	}

	public PaypalOrderRequestBuilder withSuccessRedirectUrl(String successRedirectUrl) {
		this.successRedirectUrl = successRedirectUrl;
		return this;
	}

	public OrderRequest build() {
		OrderRequest orderRequest = new OrderRequest();
		ApplicationContext applicationContext = new ApplicationContext()
				.cancelUrl(this.cancelRedirectUrl)
				.returnUrl(this.successRedirectUrl);

		orderRequest.checkoutPaymentIntent("AUTHORIZE");
		orderRequest.applicationContext(applicationContext);

		List<PurchaseUnitRequest> PurchaseUnitRequests = new ArrayList<>();
		PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
				.amountWithBreakdown(this.requestedAmount)
				.description(this.orderId);
		
		PurchaseUnitRequests.add(purchaseUnitRequest);
		orderRequest.purchaseUnits(PurchaseUnitRequests);
		
		return orderRequest;

	}

}
