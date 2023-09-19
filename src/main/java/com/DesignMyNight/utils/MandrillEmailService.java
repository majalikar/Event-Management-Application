package com.DesignMyNight.utils;

//import com.microtripit.mandrillapp.lutung.MandrillApi;
//import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
//import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
//import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MandrillEmailService implements EmailService {
//
//    private static final String API_KEY = "b5cc469350872c3460d1c0ab77c1eee0-us21";
//    private static final String FROM_EMAIL = "majalikarpravesh6@gmail.com";
//
//    @Override
//    public void sendEmail(String to, String subject, String body) {
//        MandrillApi mandrillApi = new MandrillApi(API_KEY);
//
//        MandrillMessage message = new MandrillMessage();
//        message.setSubject(subject);
//        message.setHtml(body);
//        message.setAutoText(true);
//        message.setFromEmail(FROM_EMAIL);
//
//        List<MandrillMessage.Recipient> recipients = new ArrayList<>();
//        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
//        recipient.setEmail(to);
//        recipient.setName(null);
//        recipients.add(recipient);
//
//        message.setTo(recipients);
//
//        try {
//            MandrillMessageStatus[] messageStatusArray = mandrillApi.messages().send(message, false);
//            if (messageStatusArray.length > 0) {
//                MandrillMessageStatus messageStatus = messageStatusArray[0];
//                if (messageStatus.getStatus().equals("sent")) {
//                    // Email sent successfully
//                } else {
//                    // Handle email sending failure
//                }
//            } else {
//                // Handle email sending failure
//            }
//        } catch (MandrillApiError | IOException e) {
//            // Handle exception
//        }
//    }
//}




