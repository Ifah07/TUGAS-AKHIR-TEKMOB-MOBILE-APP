package com.app.keluhkesah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.keluhkesah.adapter.KeluhKesahAdapter;
import com.app.keluhkesah.model.KeluhKesah;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class StatusActivity extends AppCompatActivity {

    RecyclerView recCurhat;
    String namaUser;

    FirebaseUser user;
    FirebaseAuth mAuth;

    FirebaseFirestore fireDB;
    KeluhKesahAdapter adapter;

    BottomNavigationView nab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initFab();

        recCurhat = findViewById(R.id.rec_curhat);
        recCurhat.setLayoutManager(new LinearLayoutManager(this));

        fireDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        namaUser = Objects.requireNonNull(user).getEmail();

        Query query = fireDB.collection("keluhkesah")
                .whereEqualTo("userId", user.getUid());
        FirestoreRecyclerOptions<KeluhKesah> options = new FirestoreRecyclerOptions.Builder<KeluhKesah>()
                .setQuery(query, KeluhKesah.class).build();
        adapter = new KeluhKesahAdapter(options);
        recCurhat.setAdapter(adapter);
        adapter.startListening();

        nab = findViewById(R.id.nab);
        nab.setSelectedItemId(R.id.status);
        nab.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent home = new Intent(StatusActivity.this, DashboardActivity.class);
                        startActivity(home);
                        finish();
                        break;
                    case R.id.status:
                        Intent status = new Intent(StatusActivity.this, StatusActivity.class);
                        startActivity(status);
                        finish();
                        break;
                    case R.id.profile:
                        Intent profile = new Intent(StatusActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Query query = fireDB.collection("keluhkesah")
                .whereEqualTo("userId", user.getUid());
        FirestoreRecyclerOptions<KeluhKesah> options = new FirestoreRecyclerOptions.Builder<KeluhKesah>()
                .setQuery(query, KeluhKesah.class).build();
        adapter = new KeluhKesahAdapter(options);
        recCurhat.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initFab(){
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), FormCurhatActivity.class)));
    }
}