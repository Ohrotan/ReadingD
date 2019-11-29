package com.ssu.readingd.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DBUtil {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a storage reference from our app
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public static void addData() {


        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        final String id = "kkdsfk@ssu.ac.kr";
        user.put("password", "비밀번호~~");


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static ArrayList<String> getData(String criteria, boolean order) {
        final ArrayList<String> a = new ArrayList<>();
        if (order) {
            db.collection("books").orderBy(criteria, Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // a.add(CustomDTO.mapToDTO(document.getData()));
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            db.collection("books").orderBy(criteria, Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //a.add(CustomDTO.mapToDTO(document.getData()));
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        return a;
    }

    public static void uploadImage(Bitmap bitmap, String name) {

        StorageReference imgRef = storageRef.child(name + ".jpg");

        final StorageReference imgRef2 = storageRef.child("images/" + name + ".jpg");

        imgRef.getName().equals(imgRef2.getName());    // true
        imgRef.getPath().equals(imgRef2.getPath());    // false


        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                Log.v("img", "suc/" + imgRef2.getDownloadUrl().toString());
                return imgRef2.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.v("img", "suc/" + downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    //https://firebase.google.com/docs/storage/android/create-reference?authuser=0
    public static void uploadImage(ImageView imageView, String name) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        uploadImage(bitmap, name);
    }

    //https://firebase.google.com/docs/storage/android/download-files?authuser=0
    //인자인 imageView가 imageResource가 세팅이 되어있어야만 이미지 삽입가능
    // ex)        ImageView img = new ImageView(this);
    //            img.setImageResource(R.drawable.pre_img);
    //            DBUtil.setImageViewFromDB(this, img, "coordi1");
    public static void setImageViewFromDB(final Context con, final ImageView imageView, String name) {
        StorageReference httpsReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/" +
                        "ssu-mylook.appspot.com/o/" + name + ".jpg");

        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(con)
                        .load(uri)
                        .into(imageView);
            }
        });

    }
}
