package www.domain.com.paypal.service;

import com.paypal.core.PayPalEnvironment;

public class PaypalLiveClient  extends AbstractPaypalClient{

	public PaypalLiveClient(String clientId, String secretId) {
		super(clientId, secretId);
	}

	@Override
	protected PayPalEnvironment createEnvironment() {
		 return new PayPalEnvironment.Live(getClientId(),getSecretId());
	}

}
