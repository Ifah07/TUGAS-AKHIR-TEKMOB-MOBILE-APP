package com.app.keluhkesah;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.app.keluhkesah.model.KeluhKesah;
public class FormCurhatActivity extends AppCompatActivity {

    EditText edtCurhat;
    FirebaseFirestore fireDB;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_curhat);
        edtCurhat = findViewById(R.id.edtCurhat);

        user = FirebaseAuth.getInstance().getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();
    }

    public void postCurhat(View v){

        DocumentReference docRef = fireDB.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String url = document.getString("profile");
                    String userId = user.getUid();
                    String email = user.getEmail();
                    String konten = edtCurhat.getText().toString();
                    fireDB.collection("keluhkesah").document()
                            .set(new KeluhKesah(userId, email, konten, url))
                            .addOnFailureListener(e -> Log.w("store_error", e.getMessage()));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        finish();
    }
}
