package com.example.task61d;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpgradeActivity extends AppCompatActivity {

    private Button btnStarter, btnIntermediate, btnAdvanced;
    private PaymentSheet paymentSheet;

    // ✅ 替换成你的 Stripe 测试密钥和后端地址
    private String publishableKey = "pk_test_51Q3EAgFLjaikutdsxDAoY2cpWJfdMUObLvFiDHCxNKUaERC7xnalp0AVNKD0LI0e2JjsmCSLPStVzX6sOgIq2oVG00gML6WE9Z";
    private String backendBaseUrl = "http://10.0.2.2:4242/create-customer";

    private String customerId;
    private String ephemeralKey;
    private String clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade); // 使用美化后的 XML

        btnStarter = findViewById(R.id.btnStarter);
        btnIntermediate = findViewById(R.id.btnIntermediate);
        btnAdvanced = findViewById(R.id.btnAdvanced);

        PaymentConfiguration.init(getApplicationContext(), publishableKey);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        btnStarter.setOnClickListener(v -> launchPaymentForAmount(5000));       // $50
        btnIntermediate.setOnClickListener(v -> launchPaymentForAmount(10000)); // $100
        btnAdvanced.setOnClickListener(v -> launchPaymentForAmount(20000));     // $200
    }

    private void launchPaymentForAmount(int amountInCents) {
        new Thread(() -> {
            try {
                URL url = new URL(backendBaseUrl + "?amount=" + amountInCents);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONObject result = new JSONObject(sb.toString());
                customerId = result.getString("customerId");
                ephemeralKey = result.getString("ephemeralKey");
                clientSecret = result.getString("clientSecret");

                runOnUiThread(() -> paymentSheet.presentWithPaymentIntent(
                        clientSecret,
                        new PaymentSheet.Configuration(
                                "Your App Name",
                                new PaymentSheet.CustomerConfiguration(customerId, ephemeralKey)
                        )
                ));
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e("Stripe", "Exception", e);
            }
        }).start();
    }

    private void onPaymentSheetResult(@NonNull PaymentSheetResult result) {
        if (result instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "✅ Payment complete!", Toast.LENGTH_LONG).show();
        } else if (result instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "❌ Payment canceled", Toast.LENGTH_SHORT).show();
        } else if (result instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "❌ Payment failed", Toast.LENGTH_SHORT).show();
        }
    }
}
