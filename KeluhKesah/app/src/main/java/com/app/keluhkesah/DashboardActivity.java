package com.app.keluhkesah;

import android.content.Intent;
import android.os.Bundle;

import com.app.keluhkesah.adapter.DashboardAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.keluhkesah.model.KeluhKesah;

import java.util.Objects;


public class DashboardActivity extends AppCompatActivity {

    RecyclerView recCurhat;
    String namaUser;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore fireDB;
    DashboardAdapter adapter;
    BottomNavigationView nab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recCurhat = findViewById(R.id.rec_curhat);
        recCurhat.setLayoutManager(new LinearLayoutManager(this));

        fireDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        namaUser = Objects.requireNonNull(user).getEmail();

        nab = findViewById(R.id.nab);
        initMaps();

        nab.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent home = new Intent(DashboardActivity.this, DashboardActivity.class);
                    startActivity(home);
                    finish();
                    break;
                case R.id.status:
                    Intent status = new Intent(DashboardActivity.this, StatusActivity.class);
                    startActivity(status);
                    finish();
                    break;
                case R.id.profile:
                    Intent profile = new Intent(DashboardActivity.this, ProfileActivity.class);
                    startActivity(profile);
                    finish();
                    break;
            }
            return false;
        });
    }

    private void initMaps() {
        FloatingActionButton fabMaps = findViewById(R.id.fab_maps);
        fabMaps.setOnClickListener(view ->
                startActivity(new Intent(DashboardActivity.this, MapsActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Query query = fireDB.collection("keluhkesah");
        FirestoreRecyclerOptions<KeluhKesah> options = new FirestoreRecyclerOptions.Builder<KeluhKesah>()
                .setQuery(query, KeluhKesah.class).build();
        adapter = new DashboardAdapter(options);
        recCurhat.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
