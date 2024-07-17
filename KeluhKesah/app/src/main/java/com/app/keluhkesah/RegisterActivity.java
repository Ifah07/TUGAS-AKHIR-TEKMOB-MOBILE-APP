package com.app.keluhkesah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.keluhkesah.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtRegEmail, edtRegPassword, edtRegNama, edtRegConfirm;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireDB;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRegEmail = findViewById(R.id.edtRegEmail);
        edtRegNama = findViewById(R.id.edtRegNama);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegConfirm = findViewById(R.id.edtRegConfirm);

        mAuth = FirebaseAuth.getInstance();
        fireDB = FirebaseFirestore.getInstance();
    }

    public void register(View v){
        String email, nama, password, confirm;
        email = edtRegEmail.getText().toString();
        nama = edtRegNama.getText().toString();
        password = edtRegPassword.getText().toString();
        confirm = edtRegConfirm.getText().toString();

        if (!email.equals("") && !nama.equals("")){
            if (password.length() > 5){
                if(password.equals(confirm)){
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    user();
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                } else {
                                    Toast.makeText(this,"Registrasi Gagal", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(this,"Password dan Konfirmasi tidak sama", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this,"Password Tidak Boleh Kurang 6 Karakter", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"Isi Terlebih Dahulu!", Toast.LENGTH_LONG).show();
        }
    }

    public void toLogin(View v){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void user(){
        String email, nama;
        email = edtRegEmail.getText().toString();
        nama = edtRegNama.getText().toString();
        user = mAuth.getCurrentUser();
        fireDB.collection("user").document(user.getUid())
                .set(new Users(user.getUid(), nama, "", ""))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Gagal Membuat Akun", e.getMessage());
                    }
                });
    }
}