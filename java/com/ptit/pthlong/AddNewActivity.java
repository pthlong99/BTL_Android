package com.ptit.pthlong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ptit.pthlong.Model.Note;

import java.util.Calendar;
import java.util.HashMap;

public class AddNewActivity extends AppCompatActivity {

    EditText add_title, add_description, add_date;
    TextView add_note;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        add_title       = findViewById(R.id.add_title);
        add_date    = findViewById(R.id.add_date);
        add_description = findViewById(R.id.add_description);
        add_note        = findViewById(R.id.lbl_addNote);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        add_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title       = add_title.getText().toString().trim();
                String date    = add_date.getText().toString().trim();
                String description = add_description.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notes");
                Note note = new Note(title, date, description, false);
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("userid", firebaseUser.getUid());
//                hashMap.put("title", add_date.getText().toString().trim());
//                hashMap.put("date", add_date.getText().toString().trim());
//                hashMap.put("done", false);
//                hashMap.put("description", description);
                reference.child(firebaseUser.getUid()).push().setValue(note);
                finish();
            }
        });
    }
}