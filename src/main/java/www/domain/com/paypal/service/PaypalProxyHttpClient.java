package www.domain.com.paypal.service;

import static com.paypal.http.serializer.StreamUtils.writeOutputStream;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.Headers;
import com.paypal.http.HttpClient;
import com.paypal.http.HttpRequest;
import com.paypal.http.HttpResponse;
import com.paypal.http.Injector;
import com.paypal.http.exceptions.HttpException;

public class PaypalProxyHttpClient extends PayPalHttpClient{
	
	  private static final String CLASSNAME = PaypalProxyHttpClient.class.getName();
	  private static final Logger LOGGER = Logger.getLogger(CLASSNAME);


	  List<Injector> mInjectors;

	public PaypalProxyHttpClient(PayPalEnvironment environment) {
		super(environment);
		 try {
		      Field f = HttpClient.class.getDeclaredField("mInjectors");
		      f.setAccessible(true);
		      mInjectors = (List<Injector>) f.get(this);
		    } catch (NoSuchFieldException | SecurityException |
		        IllegalArgumentException | IllegalAccessException e) {
		      LOGGER.logp(Level.SEVERE, CLASSNAME, CLASSNAME,
		          "Reflection Error - Stacktrace", e);
		    }
	}
	
	  @Override
	  public <T> HttpResponse<T> execute(HttpRequest<T> request) throws IOException {
	    HttpRequest<T> requestCopy = request.copy();

	    for (Injector injector : mInjectors) {
	      injector.inject(requestCopy);
	    }

	    HttpURLConnection connection = getConnection(requestCopy);
	    try {
	      return parseResponse(connection, requestCopy.responseClass());
	    } finally {
	      if (connection != null) {
	        connection.disconnect();
	      }
	    }
	  }

	  private void applyHeadersFromRequest(HttpURLConnection connection, HttpRequest request) {
	    for (String key: request.headers()) {
	      connection.setRequestProperty(key, request.headers().header(key));
	    }
	  }
	  
	  HttpURLConnection getConnection(HttpRequest request) throws IOException {
		    Proxy proxy =  Proxy.NO_PROXY;
		    HttpURLConnection connection = (HttpURLConnection) new URL(getEnvironment().baseUrl() + request.path()).openConnection(proxy);

		    if (connection instanceof HttpsURLConnection) {
		      if (getSSLSocketFactory() == null) {
		        String message = "SSLSocketFactory was not set or failed to initialize";
		        System.out.println(message);
		        throw new SSLException(message);
		      }

		      ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLSocketFactory());
		    }

		    connection.setReadTimeout(getReadTimeout());
		    connection.setConnectTimeout(getConnectTimeout());

		    setRequestVerb(request.verb(), connection);
		    if (request.requestBody() != null) {
		      connection.setDoOutput(true);
		      byte[] data = getEncoder().serializeRequest(request);

		      applyHeadersFromRequest(connection, request);
		      writeOutputStream(connection.getOutputStream(), data);
		    } else {
		      applyHeadersFromRequest(connection, request);
		    }

		    return connection;
		  }

		  /**
		   * Workaround for a bug in {@code HttpURLConnection.setRequestMethod(String)}
		   * The implementation of Sun/Oracle is throwing a {@code ProtocolException}
		   * when the method is other than the HTTP/1.1 default methods. So to use {@code PATCH}
		   * and others, we must apply this workaround.
		   *
		   * See issue https://bugs.openjdk.java.net/browse/JDK-7016595
		   */
		  private void setRequestVerb(String verb, HttpURLConnection connection) {
		    try {
		      connection.setRequestMethod(verb.toUpperCase());
		    } catch (ProtocolException ignored) {
		      try {
		        Field delegateField = connection.getClass().getDeclaredField("delegate");
		        delegateField.setAccessible(true);
		        HttpURLConnection delegateConnection = (HttpURLConnection) delegateField.get(connection);

		        setRequestVerb(verb, delegateConnection);
		      } catch (NoSuchFieldException e) {
		        Field methodField = null;
		        Class connectionClass = connection.getClass();
		        while (methodField == null) {
		          try {
		            methodField = connectionClass.getDeclaredField("method");
		            methodField.setAccessible(true);
		            methodField.set(connection, "PATCH");
		          } catch (IllegalAccessException | NoSuchFieldException _ignored) {
		            connectionClass = connectionClass.getSuperclass();
		          }
		        }
		      } catch (IllegalAccessException ignoredIllegalAccess) {}
		    }
		  }

		  Headers parseResponseHeaders(URLConnection connection) {
		    Headers headers = new Headers();
		    for (String key : connection.getHeaderFields().keySet()) {
		      headers.header(key, connection.getHeaderField(key));
		    }

		    return headers;
		  }

		  private <T> HttpResponse<T> parseResponse(HttpURLConnection connection, Class<T> responseClass) throws IOException {
		    Headers responseHeaders = parseResponseHeaders(connection);
		    String responseBody;
		    int statusCode;
		    statusCode = connection.getResponseCode();
		    if (statusCode >= HTTP_OK && statusCode <= HTTP_PARTIAL) {
		      T deserializedResponse = null;

		      if (!Void.class.isAssignableFrom(responseClass)) {
		        deserializedResponse = getEncoder().deserializeResponse(connection.getInputStream(), responseClass, responseHeaders);
		      }

		      return new ProxyHttpResponse<>(responseHeaders, statusCode, deserializedResponse);
		    } else {
		      responseBody = getEncoder().deserializeResponse(connection.getErrorStream(), String.class, responseHeaders);
		      throw new HttpException(responseBody, statusCode, responseHeaders);
		    }
		  }


}
