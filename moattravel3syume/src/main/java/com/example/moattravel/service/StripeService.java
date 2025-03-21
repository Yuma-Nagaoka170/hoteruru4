package com.example.moattravel.service;

 import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.moattravel.form.ReservationRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;
 
 @Service
public class StripeService {
	 @Value("${stripe.api-key}")
	 private String stripeApiKey;
	 
	 private final ReservationService reservationService;
	 
	 public StripeService(ReservationService reservationService) {
		 this.reservationService = reservationService;
	 }
	 
     // セッションを作成し、Stripeに必要な情報を返す
     public String createStripeSession(String houseName, ReservationRegisterForm reservationRegisterForm, HttpServletRequest httpServletRequest) {
         Stripe.apiKey = "sk_test_51QwHjVH6R7DxdL8D0IN5myAXy56gQVCwK9Q2mW4wGYK1dX9PZIVMlwVthHZBL1eayzuvBUwVQZTgvFAM5xZcGA63004erLND2A";
         Stripe.apiKey = stripeApiKey;
         String requestUrl = new String(httpServletRequest.getRequestURL());
         SessionCreateParams params =
             SessionCreateParams.builder()
                 .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                 .addLineItem(
                     SessionCreateParams.LineItem.builder()
                         .setPriceData(
                             SessionCreateParams.LineItem.PriceData.builder()   
                                 .setProductData(
                                     SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                         .setName(houseName)
                                         .build())
                                 .setUnitAmount((long)reservationRegisterForm.getAmount())
                                 .setCurrency("jpy")                                
                                 .build())
                         .setQuantity(1L)
                         .build())
                 .setMode(SessionCreateParams.Mode.PAYMENT)
                 .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
                 .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
                 .setPaymentIntentData(
                     SessionCreateParams.PaymentIntentData.builder()
                         .putMetadata("houseId", reservationRegisterForm.getHouseId().toString())
                         .putMetadata("userId", reservationRegisterForm.getUserId().toString())
                         .putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
                         .putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate())
                         .putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
                         .putMetadata("amount", reservationRegisterForm.getAmount().toString())
                         .build())
                 .build();
         try {
             Session session = Session.create(params);
             return session.getId();
         } catch (StripeException e) {
             e.printStackTrace();
             return "";
         }
     } 
     
     public void processSessionCompleted(Event event) {
         Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
         optionalStripeObject.ifPresent(stripeObject -> {
             Session session = (Session)stripeObject;
             SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();
 
             try {
                 session = Session.retrieve(session.getId(), params, null);
                 Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
                 reservationService.create(paymentIntentObject);
             } catch (StripeException e) {
                 e.printStackTrace();
             }
         });
     }    

}
