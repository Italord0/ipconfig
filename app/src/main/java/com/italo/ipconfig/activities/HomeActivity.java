package com.italo.ipconfig.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.italo.ipconfig.R;
import com.italo.ipconfig.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private AdView adView;
    private TextView tvIp;
    private WebView webView;
    private Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        initViews();
        setListeners();
        MobileAds.initialize(this, initializationStatus -> {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        });
    }

    private void setListeners() {

            try  {
                StringBuilder stringBuilder = new StringBuilder();
                webView = findViewById(R.id.web_view);
                doc = Jsoup.connect("https://www.find-ip-address.org/").get();
                Elements ele = doc.select("td.slova");

                for (Element element : ele) {
                    for (Element allElement : element.getAllElements()) {
                        if (allElement.tagName().equals("img")) {
                            Log.e("IMAGEFOUND",element.tagName());
                            allElement.attr("src","https://www.find-ip-address.org/"+allElement.attr("src"));
                        }
                    }
                    System.out.println(element);
                }

                String finalString = stringBuilder.toString();

                Log.e("HTML",finalString);
                String base64 = android.util.Base64.encodeToString(finalString.getBytes("UTF-8"), android.util.Base64.DEFAULT);
                webView.loadData(base64, "text/html; charset=utf-8", "base64");
            } catch (Exception e) {
                e.printStackTrace();
            }

        tvIp.setOnClickListener(v -> {
            copyIp();
        });
    }

    private void initViews() {
        adView = findViewById(R.id.adView);
        tvIp = findViewById(R.id.tv_ip);
        tvIp.setText(Util.getIPAddress(true));
    }

    private void copyIp(){
        Util.copyToClipboard(this,Util.getIPAddress(true));
    }
}