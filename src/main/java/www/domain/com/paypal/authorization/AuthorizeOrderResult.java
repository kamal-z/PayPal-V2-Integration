package www.domain.com.paypal.authorization;

import java.math.BigDecimal;
import java.util.List;

import com.paypal.orders.Authorization;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.PurchaseUnit;

public class AuthorizeOrderResult {

	private final Order order;
	private final int httpStatusCode;
	private final String errMessage;

	private static final AuthorizeOrderResult UNEXCEPTED_FAILURE = new AuthorizeOrderResult(0, "Unexpected Error");

	public enum PaymentState {

		SUCCESS("successful"), Failure("failed");

		private String value;

		PaymentState(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	}

	public AuthorizeOrderResult(Order order, int httpStatusCode) {
		this(order, httpStatusCode, null);
	}

	public AuthorizeOrderResult(Order order, int httpStatusCode, String errMessage) {
		this.order = order;
		this.httpStatusCode = httpStatusCode;
		this.errMessage = errMessage;
	}

	public AuthorizeOrderResult(int httpStatusCode, String errMessage) {
		this.order = null;
		this.httpStatusCode = httpStatusCode;
		this.errMessage = errMessage;
	}

	public String getAuthorizationStatus() {
		if (order == null)
			return null;
		List<PurchaseUnit> purchaseUnits = order.purchaseUnits();
		if (purchaseUnits != null) {
			for (PurchaseUnit purchaseUnit : purchaseUnits) {
				List<Authorization> authorizations = purchaseUnit.payments().authorizations();
				for (Authorization authorization : authorizations) {
					String status = authorization.status();
					if (status != null) {
						return status;
					}
				}
			}
		}
		return null;
	}

	public String getCatureUrl() {
		List<PurchaseUnit> purchaseUnits = order.purchaseUnits();
		if (purchaseUnits != null) {
			for (PurchaseUnit purchaseUnit : purchaseUnits) {
				List<Authorization> authorizations = purchaseUnit.payments().authorizations();
				for (Authorization authorization : authorizations) {
					List<LinkDescription> linkDescriptionLinks = authorization.links();
					for (LinkDescription link : linkDescriptionLinks) {
						if ("capture".equals(link.rel())) {
							return link.href();
						}
					}
				}
			}
		}
		return null;
	}

	public BigDecimal getAmount() {
		List<PurchaseUnit> purchaseUnits = order.purchaseUnits();
		if (purchaseUnits != null) {
			for (PurchaseUnit purchaseUnit : purchaseUnits) {
				List<Authorization> authorizations = purchaseUnit.payments().authorizations();
				for (Authorization authorization : authorizations) {
					if (authorization.amount() != null) {
						String amount = authorization.amount().value();
						if (amount != null) {
							return new BigDecimal(amount);
						}
					}

				}
			}
		}
		return null;
	}
	
	  public String getAuthorizationId() {
		    List<PurchaseUnit> purchaseUnits = order.purchaseUnits();
		    for(PurchaseUnit purchaseUnit : purchaseUnits) {
		      List<Authorization> authorizations = purchaseUnit.payments().authorizations();
		      for(Authorization authorization : authorizations) {
		        String id = authorization.id();
		        if(authorization.id() != null) {
		          return id;
		        }
		      }
		    }
		    return null;
		  }

	public boolean isSuccess() {
		String status = getAuthorizationStatus();
		return "CREATED".equals(status);
	}

	public PaymentState getState() {
		if (isSuccess()) {
			return PaymentState.SUCCESS;
		} else {
			return PaymentState.Failure;
		}
	}

	public Order getOrder() {
		return order;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

}
