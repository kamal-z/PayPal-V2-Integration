package www.domain.com.paypal.service;

import com.paypal.core.PayPalEnvironment;

public class PaypalSandboxClient extends AbstractPaypalClient{

	public PaypalSandboxClient(String clientId, String secretId) {
		super(clientId, secretId);
	}

	@Override
	protected PayPalEnvironment createEnvironment() {
		return new PayPalEnvironment.Sandbox(getClientId(), getSecretId());
	}

}
