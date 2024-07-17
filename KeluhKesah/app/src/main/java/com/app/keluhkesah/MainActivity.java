package com.app.keluhkesah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    public static final String KEY_USER = "DataUser";
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(this, DashboardActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    public void login(View v){
        String emailUser = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (!emailUser.equals("") && !password.equals("")){
            mAuth.signInWithEmailAndPassword(emailUser,password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Berhasil!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Gagal!", Toast.LENGTH_LONG).show();
                        }

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Isi Form Dahulu", Toast.LENGTH_LONG).show();
        }

    }

    public void toRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
