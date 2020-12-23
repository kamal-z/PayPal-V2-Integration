package www.domain.com.paypal.service;

public enum PaypalClientEnvironment {
	SANDBOX,
	LIVE;
	
	public static PaypalClientEnvironment getByName(String name) {
		if(LIVE.name().equalsIgnoreCase(name)) {
			return LIVE;
		}
		return SANDBOX;
	}

}
