package com.DesignMyNight.utils;

import com.DesignMyNight.entities.Event;
import com.DesignMyNight.entities.Ticket;
import com.DesignMyNight.payload.PaymentRequest;
import com.DesignMyNight.payload.PaymentResponse;
import com.DesignMyNight.payload.TokenResponse;
import com.DesignMyNight.repository.EventRepository;
import com.DesignMyNight.repository.TicketRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.model.Price;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private GmailService gmailService;

    private final String apiKey = "sk_test_51NQmnOBsBLtmyBceeYGPml4VYNKRbMns3V4aY2nvi137ssyr9yFzsoByV2sqobKHelmO2GyDFfkLeddGgPrWNZI7003yiPgNXf";
    public PaymentController() {
        Stripe.apiKey = apiKey;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Validate and extract payment details from the request
            String paymentMethodId = paymentRequest.getPaymentMethodId();
            String priceId = paymentRequest.getPriceId();

            // Retrieve the price from Stripe using the price ID
            Price price = Price.retrieve(priceId);

            // Get the amount from the price
            Long amount = price.getUnitAmount();

            // Create PaymentIntent parameters
            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setCurrency(price.getCurrency())
                    .setAmount(amount)
                    .setPaymentMethod(paymentMethodId)
                    .build();

            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(createParams);

            // Confirm the PaymentIntent
            PaymentIntent confirmPaymentIntent = paymentIntent.confirm();

            // Retrieve the ticket details using the price ID
            Ticket ticket = ticketRepository.findByPriceId(priceId);
            if (ticket == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Retrieve the event details from the ticket
            Event event = ticket.getEvent();
            if (event == null) {
                // Event not found for the ticket
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Create the payment response
            PaymentResponse paymentResponse = new PaymentResponse("Payment successful", event, ticket);

            // Check if the payment was successful
            if (confirmPaymentIntent.getStatus().equals("succeeded")) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

                gmailService.sendEmail(userEmail, "Ticket Confirmation email", "Dear User,\n\n"
                        + "Thank you for booking the ticket for the event.\n"
                        + "We are excited to have you join us!\n\n"
                        + "Event Details:\n"
                        + "Event Name: " + event.getEventName() + "\n"
                        + "Event Date: " + event.getEventDate() + "\n"
                        + "Venue: " + event.getVenueName() + "\n"
                        + "Address: " + event.getStreet() + ", " + event.getCityTown() + ", " + event.getPostalCode() + "\n\n"
                        + "Ticket Details:\n"
                        + "Ticket Name: " + ticket.getTicketName() + "\n"
                        + "Ticket Price: " + ticket.getPrice() + "\n"
                        + "If you have any questions or need further assistance, please feel free to contact us.\n\n"
                        + "Thank you and enjoy the event!\n"
                        + "Best regards,\n"
                        + "Pravesh Majalikar");

                return ResponseEntity.ok(paymentResponse);
            } else {
                // Payment failed or requires further action

                // Return an appropriate error response to the client-side
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Payment failed or requires further action");
            }
        } catch (StripeException e) {
            e.printStackTrace();
            // Handle Stripe exception and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment processing failed");
        }
    }
    @PostMapping("/generate-token")
    public ResponseEntity<TokenResponse> generateToken() {
        try {
            // Create card parameters
            PaymentMethodCreateParams.Builder builder = PaymentMethodCreateParams.builder()
                    .setType(PaymentMethodCreateParams.Type.CARD)
                    .putExtraParam("card[number]", "4242424242424242")
                    .putExtraParam("card[exp_month]", 12)
                    .putExtraParam("card[exp_year]", 2024)
                    .putExtraParam("card[cvc]", "123");

            PaymentMethodCreateParams params = builder.build();

            PaymentMethod paymentMethod = PaymentMethod.create(params);
            String paymentMethodId = paymentMethod.getId();

            // Create the token response
            TokenResponse tokenResponse = new TokenResponse(paymentMethodId);

            // Return the token response to the client-side
            return ResponseEntity.ok(tokenResponse);
        } catch (StripeException e) {
            e.printStackTrace();
            // Handle Stripe exception and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TokenResponse("PaymentMethod creation failed"));
        }
    }
}









