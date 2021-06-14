package com.ptit.pthlong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ptit.pthlong.Model.Account;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username_reg, password_reg;
    TextView register;

    private FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        username_reg = findViewById(R.id.txt_regEmail);
        password_reg = findViewById(R.id.txt_regPassword);
        register     = findViewById(R.id.btn_regRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username_reg.getText().toString().trim();
                String pass  = password_reg.getText().toString().trim();

                if (email.equals("") || pass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Fill out both email and password",
                            Toast.LENGTH_SHORT).show();
                }
                else if (pass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 chars",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    register(email, pass);
                }
            }
        });
    }

    private void checkUserExisted(final String email, final String password) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    register(email, password);
                }else {
                    Toast.makeText(RegisterActivity.this, "This email already exists.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            reference = FirebaseDatabase.getInstance().getReference();
                            Account account = new Account(email, password);
                            reference.child("User").push().setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Create account successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}