package com.ptit.pthlong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ptit.pthlong.Model.Note;

import java.util.Calendar;

public class ViewItemActivity extends AppCompatActivity {

    EditText view_title, view_date, view_description;
    TextView delete_note, update_note, cancel_note;
    CheckBox view_done;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(firebaseUser.getUid());
        view_title = findViewById(R.id.view_title);
        view_description = findViewById(R.id.view_description);
        view_date = findViewById(R.id.view_date);
        view_done = findViewById(R.id.view_done);
        delete_note = findViewById(R.id.delete_note);
        update_note = findViewById(R.id.update_note);
        cancel_note = findViewById(R.id.cancel_note);
        Intent intent = getIntent();
        view_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        view_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
        String id = intent.getStringExtra("id");
        view_title.setText(intent.getStringExtra("title"));
        view_date.setText(intent.getStringExtra("date"));
        view_description.setText(intent.getStringExtra("description"));
        Boolean check = Boolean.valueOf(intent.getStringExtra("done"));
        view_done.setChecked(check);
        if(!check){
            delete_note.setEnabled(false);
            delete_note.setVisibility(View.GONE);
        }
        else{
            delete_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child(id).removeValue();
                    Intent intent1 = new Intent(ViewItemActivity.this, DashboardActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
        }
        update_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(view_title.getText().toString(), view_date.getText().toString(), view_description.getText().toString(), view_done.isChecked());
                databaseReference.child(id).setValue(note);
                Intent intent1 = new Intent(ViewItemActivity.this, DashboardActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        cancel_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewItemActivity.this, DashboardActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}