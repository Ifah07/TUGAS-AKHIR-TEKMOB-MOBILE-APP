package com.app.keluhkesah;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore fireDB;
    FirebaseAuth mAuth;

    TextView showNama, showEmail, showNim;

    Button updateProfile;
    ImageView imageView;
    BottomNavigationView nab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initFab();

        showNama = findViewById(R.id.show_nama);
        showEmail = findViewById(R.id.show_email);
        showNim = findViewById(R.id.show_nim);
        imageView = findViewById(R.id.img_profile);

        fireDB = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        DocumentReference docRef = fireDB.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    showNama.setText(document.getString("fullname"));
                    showEmail.setText(user.getEmail());
                    showNim.setText(document.getString("nim"));
                    String url = document.getString("profile");
                    if (url != null && !url.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(url)
                                .placeholder(R.drawable.ic_launcher_background)
                                .centerCrop()
                                .into(imageView);
                    }
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        updateProfile = findViewById(R.id.btn_update_profile);
        updateProfile.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
            finish();
        });

        nab = findViewById(R.id.nab);
        nab.setSelectedItemId(R.id.profile);
        nab.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent home = new Intent(ProfileActivity.this, DashboardActivity.class);
                        startActivity(home);
                        finish();
                        break;
                    case R.id.status:
                        Intent status = new Intent(ProfileActivity.this, StatusActivity.class);
                        startActivity(status);
                        finish();
                        break;
                    case R.id.profile:
                        Intent profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        finish();
                        break;
                }
                return false;
            }
        });
    }
    private void initFab(){
        FloatingActionButton fabLogOff = findViewById(R.id.fabLogoff);
        fabLogOff.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(getApplicationContext(),"Anda Berhasil Logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });
    }
}