package www.domain.com.paypal.service;

import com.paypal.http.Headers;
import com.paypal.http.HttpResponse;

public class ProxyHttpResponse<T> extends HttpResponse<T> {

	  protected ProxyHttpResponse(Headers headers, int statusCode, T result) {
	    super(headers, statusCode, result);
	  }
	  
}	  
	  