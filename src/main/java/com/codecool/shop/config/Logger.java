package com.codecool.shop.config;

import com.codecool.shop.dao.OrderDao;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unchecked")
public interface Logger {
     default void adminLog(HttpServletRequest req, OrderDao orderDataStore, String process)
            throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        System.out.println(date.toString());
//        FileWriter log = new FileWriter("src/main/webapp/static/logs/" + orderDataStore.getId() + "-"  +
//                dateFormat.format(date) + ".json");
//        log.write(" ");
        JSONObject obj = new JSONObject();

         System.out.println(req.getRequestURL().toString());

        if (req.getRequestURL().toString().contains("checkout")) {
            obj.put("Cart", true);
            obj.put("Checkout", true);
            obj.put("Payment Method", false);
            obj.put("Payment Details", false);
            obj.put("Payment Confirmation", false);
        } else if (req.getRequestURL().toString().contains("payment-method")) {
            obj.put("Cart", true);
            obj.put("Checkout", true);
            obj.put("Payment Method", true);
            obj.put("Payment Details", false);
            obj.put("Payment Confirmation", false);
        } else if(req.getRequestURL().toString().contains("payment-details")){
            obj.put("Cart", true);
            obj.put("Checkout", true);
            obj.put("Payment Method", true);
            obj.put("Payment Details", true);
            obj.put("Payment Confirmation", false);
        } else if(req.getRequestURL().toString().contains("confirmation")){
            obj.put("Cart", true);
            obj.put("Checkout", true);
            obj.put("Payment Method", true);
            obj.put("Payment Details", true);
            obj.put("Payment Confirmation", true);
        }


//        try {
//            log.write(obj.toJSONString());
//            System.out.println("Successfully copied JSON Object to file.");
//            System.out.println("\nJSON Object: " + obj);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            log.flush();
//            log.close();
//        }
    }
}
