package com.ptit.pthlong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ptit.pthlong.Model.Note;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton btn_add;
    private RecyclerView list_note;
    private ListAdapter listAdapter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private SearchView searchView;

    ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        notes = new ArrayList<Note>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(firebaseUser.getEmail().replace("@gmail.com", ""));
        btn_add   = findViewById(R.id.btn_add);
        list_note = findViewById(R.id.rc_listNote);
        searchView = findViewById(R.id.searchView);
        String userID = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes");
        reference.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Note note = snapshot.getValue(Note.class);
                notes.add(note);
                note.setId(snapshot.getKey());
                listAdapter = new ListAdapter(notes, DashboardActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardActivity.this);
                list_note.setAdapter(listAdapter);
                list_note.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AddNewActivity.class));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Note> note2s;
                note2s = new ArrayList<Note>();
                for(Note note: notes){
                    if(note.getTitle().toLowerCase().contains(query.toLowerCase())){
                        note2s.add(note);
                    }
                }
                listAdapter = new ListAdapter(note2s, DashboardActivity.this);
                list_note.setAdapter(listAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Note> note2s;
                note2s = new ArrayList<Note>();
                for(Note note: notes){
                    if(note.getTitle().toLowerCase().contains(newText.toLowerCase())){
                        note2s.add(note);
                    }
                }
                listAdapter = new ListAdapter(note2s, DashboardActivity.this);
                list_note.setAdapter(listAdapter);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_logout): {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                finish();
                return true;
            }
            case (R.id.menu_edit_Pasword):{
                startActivity(new Intent(DashboardActivity.this, ChangePasswordActivity.class));
            }
        }
        return false;
    }
}