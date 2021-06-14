package com.ptit.pthlong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ptit.pthlong.Model.Account;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText txt_curPassword, txt_newPassword, txt_confirmPassword;
    TextView lbl_changePassword, lbl_cancelPassword;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        txt_confirmPassword = findViewById(R.id.txt_confirmPassword);
        txt_newPassword = findViewById(R.id.txt_newPassword);
        txt_curPassword = findViewById(R.id.txt_curPassword);
        lbl_changePassword = findViewById(R.id.btn_changePassword);
        lbl_cancelPassword = findViewById(R.id.btn_cancelPassword);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        lbl_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        lbl_cancelPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this, DashboardActivity.class));
            }
        });
    }
    public void changePassword(){
        if (txt_curPassword.getText().equals("") || txt_newPassword.getText().equals("") || txt_confirmPassword.getText().equals("")) {
            Toast.makeText(ChangePasswordActivity.this, "Fill out both email and password",
                    Toast.LENGTH_SHORT).show();
        }
        else if (txt_curPassword.getText().length() < 6 || txt_newPassword.getText().length() < 6 || txt_confirmPassword.getText().length() < 6) {
            Toast.makeText(ChangePasswordActivity.this, "Password must be at least 6 chars",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!(txt_newPassword.getText().toString().equalsIgnoreCase(txt_confirmPassword.getText().toString()))) {
            Toast.makeText(ChangePasswordActivity.this, "Confirm new password not correct",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            String email = firebaseUser.getEmail();
            String pass = txt_curPassword.getText().toString();
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(ChangePasswordActivity.this, DashboardActivity.class);
                        firebaseUser.updatePassword(txt_newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ChangePasswordActivity.this, "Change password successful.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(ChangePasswordActivity.this, "Change password failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Current password not correct",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}