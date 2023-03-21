package com.example.pcshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText spass,semail;
    Button btnsignup, btnlogin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spass = findViewById(R.id.password);
        semail = findViewById(R.id.email);
        btnsignup = findViewById(R.id.btnsignup);
        btnlogin = findViewById(R.id.btnlogin);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        btnlogin.setOnClickListener(view -> {
            String email = semail.getText().toString().trim();
            String password = spass.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                semail.setError("Molimo unesite e-mail adresu");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                spass.setError("Molimo unesite lozinku");
                return;
            }
            if (password.length() < 6) {
                spass.setError("Lozinka najmanje mora imati 6 znakova!");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            // autenticiranje korisnika
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this, "Uspješno prijavljeno", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        Toast.makeText(Login.this, "Pogrešna registracija " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        });

        btnsignup.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Register.class));
            finish();
        });



    }
}