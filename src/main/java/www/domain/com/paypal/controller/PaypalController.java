package www.domain.com.paypal.controller;



import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.paypal.http.exceptions.HttpException;
import www.domain.com.paypal.authorization.AuthorizeOrderResult;
import www.domain.com.paypal.authorization.PaypalAhthorizeRequestBuilder;
import www.domain.com.paypal.createorder.CreatedOrderResult;
import www.domain.com.paypal.service.PaymentInstractionDataService;
import www.domain.com.paypal.service.PaypalService;

@RestController
public class PaypalController {
	
	@Autowired
	private PaypalService paypalService;
	
	@RequestMapping ("/PaypalPayment")
	public RedirectView startPaypalPayment() throws HttpException {
		CreatedOrderResult  createdOrderResult =  paypalService.startPayment();
		if (createdOrderResult != null) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(createdOrderResult.getApproveLink());
			return redirectView;
		}
		return null;
	}
	
	@RequestMapping ({"/", "/OrderReview"})
	public ModelAndView redirectToOrdereview() throws HttpException {
	    ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orderReview.html");
        return modelAndView;
	}
	
	@RequestMapping ("/OrderConfirmation")
	public ModelAndView changeOrderState(@RequestParam(value="token", required = true) String token, @RequestParam(value="orderId", required = true) String orderId) throws HttpException {
		 String requestId = UUID.randomUUID().toString();
		 AuthorizeOrderResult authorizeOrderResult = paypalService.AuthrizePaypalOrder(PaymentInstractionDataService.paymentData.get(orderId), requestId, new PaypalAhthorizeRequestBuilder());
		 /* deactivate capture payment
		 	paypalService.captureOrder(authorizeOrderResult.getAuthorizationId(), new PaypalCaptureRequestBuilder());
		  * 
		  */
		 ModelAndView modelAndView = new ModelAndView();
	     modelAndView.setViewName("orderConfirmation.html");
	     return modelAndView;
		 
	}
	
 
	
	 
}
