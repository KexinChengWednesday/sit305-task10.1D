package com.example.task61d;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentsUtil {
    public static JSONObject getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = new JSONObject();
            isReadyToPayRequest.put("apiVersion", 2);
            isReadyToPayRequest.put("apiVersionMinor", 0);

            JSONArray allowedAuthMethods = new JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS");
            JSONArray allowedCardNetworks = new JSONArray().put("AMEX").put("VISA").put("MASTERCARD");

            JSONObject cardParameters = new JSONObject()
                    .put("allowedAuthMethods", allowedAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks);

            JSONObject cardPaymentMethod = new JSONObject()
                    .put("type", "CARD")
                    .put("parameters", cardParameters);

            JSONArray allowedPaymentMethods = new JSONArray().put(cardPaymentMethod);
            isReadyToPayRequest.put("allowedPaymentMethods", allowedPaymentMethods);

            return isReadyToPayRequest;
        } catch (JSONException e) {
            return null;
        }
    }
}
