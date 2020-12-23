package www.domain.com.paypal.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaypalClientProvider {
	
	private static final Map<String, PaypalClient> CLIENT_INSTANZMAP = new ConcurrentHashMap<>();
	
	public static  PaypalClient providePaypalClient(String environment, String clientId, String secredId) {
		return CLIENT_INSTANZMAP.computeIfAbsent(clientId, k -> createEnvironmentSpecificClient(environment,clientId,secredId));
	}

	private static PaypalClient createEnvironmentSpecificClient(
			String environment,
			String clientId, 
			String secretId) {
		
		if (PaypalClientEnvironment.SANDBOX == PaypalClientEnvironment.getByName(environment)) {
			return new PaypalSandboxClient(clientId, secretId);
		}
		
		if (PaypalClientEnvironment.LIVE == PaypalClientEnvironment.getByName(environment)) {
		return new PaypalLiveClient(clientId, secretId);
		}
		
		return null;
	}

}
