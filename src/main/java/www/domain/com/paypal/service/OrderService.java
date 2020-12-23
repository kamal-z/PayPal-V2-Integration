package www.domain.com.paypal.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
	
 
	private static int orderId = 0;
	
	public int getOrderId() {
		 orderId = orderId+1;
		 return orderId;
	}

}
