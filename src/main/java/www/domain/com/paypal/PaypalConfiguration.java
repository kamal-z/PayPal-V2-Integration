package www.domain.com.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import www.domain.com.paypal.service.PaypalClientEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@ConfigurationProperties(prefix = "paypal")
public class PaypalConfiguration {
	@Autowired
	private AppConfiguration appConfiguration;
	
	private String mode;
	
	private String sandboxClientId;
	
	private String sandboxSecretId;
	
	private String liveClientId;
	
	private String liveSecretId;
	
	private String successRedirectUrl;
	
	private String cancelRedirectUrl;
	
	public String getMode() {
		return mode;
	}
	
	public void setMode(String mode) throws UnknownHostException {
		this.mode = mode;
	}
	
	public String getSandboxClientId() {
		return sandboxClientId;
	}
	
	public void setSandboxClientId(String sandboxClientId) {
		this.sandboxClientId = sandboxClientId;
	}
	
	public String getSandboxSecretId() {
		return sandboxSecretId;
	}
	
	public void setSandboxSecretId(String sandboxSecretId) {
		this.sandboxSecretId = sandboxSecretId;
	}
	
	public String getLiveClientId() {
		return liveClientId;
	}
	
	public void setLiveClientId(String liveClientId) {
		this.liveClientId = liveClientId;
	}
 
	public String getLiveSecretId() {
		return liveSecretId;
	}
	
	public void setLiveSecretId(String liveSecretId) {
		this.liveSecretId = liveSecretId;
	}
	
	public String getSuccessRedirectUrl() {
		return appConfiguration.getDomainName() + successRedirectUrl;
	}

	public void setSuccessRedirectUrl(String successRedirectUrl) {
		this.successRedirectUrl = successRedirectUrl;
	}

	public String getCancelRedirectUrl() {
		return appConfiguration.getDomainName() + cancelRedirectUrl;
	}

	public void setCancelRedirectUrl(String cancelRedirectUrl) {
		this.cancelRedirectUrl = cancelRedirectUrl;
	}

	public String getCurrentCleintId() {
		return PaypalClientEnvironment.SANDBOX == PaypalClientEnvironment.getByName(mode) ? getSandboxClientId() : getLiveClientId();
	}
	
	public String getCurrentSecrerId() {
		return PaypalClientEnvironment.SANDBOX == PaypalClientEnvironment.getByName(mode) ? getSandboxSecretId() : getLiveSecretId();
	}
	
	
	

}
