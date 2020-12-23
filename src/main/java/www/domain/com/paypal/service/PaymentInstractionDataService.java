package www.domain.com.paypal.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 
public class PaymentInstractionDataService {
	
	/*
	 * Quick solution instead of Data base
	 * 
	 */
	
	public static Map<String, String> paymentData = new HashMap<>();

}
