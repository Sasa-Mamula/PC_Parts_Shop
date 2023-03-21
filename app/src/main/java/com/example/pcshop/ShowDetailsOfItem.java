package com.example.pcshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcshop.model.CartModel;
import com.example.pcshop.model.PartsModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ShowDetailsOfItem extends AppCompatActivity {
    private TextView titletxt,descTxt,priceTxt, addToCart,brandTxt;
    private ImageView ItemView;
    private String productID ;
    String image_url;
    RelativeLayout showdetaillayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_showdetails);
        productID = getIntent().getStringExtra("key");
        titletxt = findViewById(R.id.titleTxt);
        descTxt = findViewById(R.id.descriptionTxt);
        priceTxt = findViewById(R.id.priceTxt);
        ItemView = findViewById(R.id.ItemImage);
        addToCart = findViewById(R.id.AddtoCart);
        brandTxt = findViewById(R.id.brandTxt);
        showdetaillayout = findViewById(R.id.Show_details);

        getProductDetailsCPU(productID);
        getProductDetailsGPU(productID);
        getProductDetailsMBO(productID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();

            }
        });
    }

    private void addToCart(){
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");

        userCart.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                    cartModel.setQuantity(cartModel.getQuantity() + 1);
                    HashMap<String, Object> cartMap = new HashMap<>();
                    cartMap.put("quantity", cartModel.getQuantity());
                    cartMap.put("totalPrice", cartModel.getQuantity() * Float.parseFloat(cartModel.getPrice()));
                    userCart.child(productID)
                            .updateChildren(cartMap);
                }
                else{
                    CartModel cartModel = new CartModel();
                    cartModel.setName(titletxt.getText().toString());
                    cartModel.setImage(image_url);
                    cartModel.setKey(productID);
                    cartModel.setPrice(priceTxt.getText().toString());
                    cartModel.setQuantity(1);
                    cartModel.setTotalPrice(Float.parseFloat(priceTxt.getText().toString()));

                    userCart.child(productID)
                            .setValue(cartModel);

                }
                makeSnackBar("Dodano u košaricu");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

            private String getProductDetailsCPU(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Parts").child("CPU");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    PartsModel products = dataSnapshot.getValue(PartsModel.class);
                    brandTxt.setText(products.getBrand());
                    titletxt.setText(products.getName());
                    priceTxt.setText(products.getPrice());
                    descTxt.setText(products.getDescription());
                    image_url = products.getImage();
                    Picasso.get().load(products.getImage()).resize(900,600).centerInside().into(ItemView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
                return productID;
            }

    private String getProductDetailsGPU(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Parts").child("GPU");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    PartsModel products = dataSnapshot.getValue(PartsModel.class);
                    brandTxt.setText(products.getBrand());
                    titletxt.setText(products.getName());
                    priceTxt.setText(products.getPrice());
                    descTxt.setText(products.getDescription());
                    image_url = products.getImage();
                    Picasso.get().load(products.getImage()).resize(900,600).centerInside().into(ItemView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return productID;
    }

    private String getProductDetailsMBO(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Parts").child("MBO");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    PartsModel products = dataSnapshot.getValue(PartsModel.class);
                    brandTxt.setText(products.getBrand());
                    titletxt.setText(products.getName());
                    priceTxt.setText(products.getPrice());
                    descTxt.setText(products.getDescription());
                    image_url = products.getImage();
                    Picasso.get().load(products.getImage()).resize(900,600).centerInside().into(ItemView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return productID;
    }
    private void makeSnackBar(String msg) {
        Snackbar.make(showdetaillayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Otvori košaricu", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ShowDetailsOfItem.this, CartActivity.class));
                    }
                }).show();
    }

}
