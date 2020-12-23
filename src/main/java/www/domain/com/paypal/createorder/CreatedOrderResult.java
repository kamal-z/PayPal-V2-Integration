package www.domain.com.paypal.createorder;

import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;

public class CreatedOrderResult {
	
	private Order order;
	
	public CreatedOrderResult(Order order) {
		super();
		this.order = order;
	}
	
	public String getApproveLink() {
		for(LinkDescription link : order.links()) {
			if(("approve".equals(link.rel()))) {
				return link.href();
			}
		}
		return null;
	}
	
	public String getPaypalOrderId() {
		return order.id();
	}
	
	public String getAuthorizeUrl() {
		for(LinkDescription link :order.links()) {
			if("authorize".equals(link.rel())) {
				return link.href();
			}
		}
		return null;
	}

}
