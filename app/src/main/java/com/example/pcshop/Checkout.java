package com.example.pcshop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class Checkout extends AppCompatActivity {
private CardView cardView;
private TextView textView;
private ImageView imageView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        cardView = findViewById(R.id.cartActivityCardView);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

    }
}
