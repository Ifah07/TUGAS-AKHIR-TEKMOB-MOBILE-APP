7package com.app.keluhkesah;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.keluhkesah.model.KeluhKesah;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class DetailCurhatActivity extends AppCompatActivity {
    EditText txtDetailKonten;
    TextView txtDetailNama;
    FirebaseFirestore fireDB;
    KeluhKesah keluh;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_curhar);
        txtDetailKonten = findViewById(R.id.txtDetailKonten);
        txtDetailNama = findViewById(R.id.txtNamaDetail);

        Intent it = getIntent();
        keluh = (KeluhKesah) it.getSerializableExtra("current_keluhkesah");
        txtDetailNama.setText(keluh.nama);
        txtDetailKonten.setText(keluh.konten);

        fireDB = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void close(View v){
        finish();
    }
    public void deleteCurhat(View v){
        fireDB.collection("keluhkesah").document(keluh.uid)
                .delete()
                .addOnFailureListener(e -> Log.w("error_delete", e.getMessage()));
        finish();
    }

    public void updtCurhat(View v){
        DocumentReference docRef = fireDB.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String url = document.getString("profile");
                    String userId = user.getUid();
                    String email = user.getEmail();
                    String konten = txtDetailKonten.getText().toString();
                    fireDB.collection("keluhkesah").document(keluh.uid)
                            .set(new KeluhKesah(userId,email,konten,url))
                            .addOnFailureListener(e -> Log.w("store_error", e.getMessage()));
                    }
                } else {
                    Log.d(TAG, "Gagal");
                }
            });
        startActivity(new Intent(DetailCurhatActivity.this,StatusActivity.class));
    }
}
