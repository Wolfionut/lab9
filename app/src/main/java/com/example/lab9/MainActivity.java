package com.example.lab9;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        client = new OkHttpClient();

        fetchImageUrl();  // Apelăm metoda pentru a prelua imaginea
    }

    private void fetchImageUrl() {
        String url = "https://dog.ceo/api/breeds/image/random";  // URL-ul API-ului

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("JSON Response", responseData);  // Log pentru a verifica răspunsul primit

                    try {
                        // Parsăm răspunsul JSON
                        JSONObject jsonObject = new JSONObject(responseData);
                        String imageUrl = jsonObject.getString("message");  // Extragem URL-ul imaginii

                        runOnUiThread(() -> {
                            // Folosim Glide pentru a afișa imaginea
                            Glide.with(MainActivity.this)
                                    .load(imageUrl)
                                    .into(imageView);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Response not successful", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
