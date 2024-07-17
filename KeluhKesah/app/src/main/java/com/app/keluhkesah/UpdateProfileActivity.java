package com.app.keluhkesah;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.keluhkesah.model.Users;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateProfileActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore fireDB;
    TextView showNama, showEmail, showNim;
    Button updateProfile, back;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        imageView = findViewById(R.id.img_profile);

        showNama = findViewById(R.id.show_nama);
        showNim = findViewById(R.id.show_nim);
        back = findViewById(R.id.btn_back);

        fireDB = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        updateProfile = findViewById(R.id.btn_update_profile);

        DocumentReference docRef = fireDB.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        showNama.setText(document.getString("fullname"));
                        showNim.setText(document.getString("nim"));
                        String url = document.getString("profile");
                        if (url != null && !url.isEmpty()) {
                            Glide.with(UpdateProfileActivity.this)
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
            }
        });

        imageView.setOnClickListener(v -> {
            selectImage();
        });

        updateProfile.setOnClickListener(v -> {
            upload();
        });

        back.setOnClickListener(v -> {
            startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
            finish();
        });

    }

    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            } else if (items[item].equals("Choose from Library")){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 20);
            } else if (items[item].equals("Cancel")){
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null){
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.post(() -> {
                       imageView.setImageBitmap(bitmap);
                    });

                } catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        if (requestCode == 10 && resultCode == RESULT_OK){
            final Bundle extras = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) extras.get("data");
                imageView.post(() -> {
                    imageView.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

    private void upload(){

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        //upload
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("img_profile/"+user.getUid());
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(taskSnapshot -> {
                    reference.getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            String imageUrl = task.getResult().toString();
                            String nama = showNama.getText().toString();
                            String nim = showNim.getText().toString();
                            Users userData = new Users(user.getUid(), nama, nim, imageUrl);
                            fireDB.collection("user").document(user.getUid()).set(userData)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(UpdateProfileActivity.this, "Upload Success", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(UpdateProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(UpdateProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

        startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
        finish();
    }


}