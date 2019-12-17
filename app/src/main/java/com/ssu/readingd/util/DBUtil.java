package com.ssu.readingd.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ssu.readingd.dto.BookDTO;
import com.ssu.readingd.dto.MemoDTO;
import com.ssu.readingd.dto.UserDTO;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DBUtil {
    final static String TAG = "Database";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    Map<String, Object> memo;
    MemoDTO memoDTO;
    UserDTO userDTO;
    boolean find=false;

    public MemoDTO getMemo(String id) {

        DocumentReference docRef = db.collection("memos").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d(TAG, "DocumentSnapshot data" + document.getData());
                        memoDTO = document.toObject(MemoDTO.class);
                    }
                    else{
                        Log.d(TAG, "No such document");
                    }
                }
                else{
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        try {
            Thread.sleep(3000);
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }

        return memoDTO;
    }

    public void addMemo(MemoDTO memo) {

     //   db.collection("memos").document("90909").set(memo);
        db.collection("memos").document().set(memo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v(TAG, "memo update success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "memo update fail");
                    }
                });

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateMemo(String id, MemoDTO memo) {
        db.collection("memos")
                .document(id)
                .set(memo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v(TAG, "memo update success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "memo update fail");
                    }
                });
    }

    public void DeleteMemo(String id){
        db.collection("memos")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v(TAG, "memo delete success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "memo delete fail");
                    }
                });
    }


    //회원가입할 때랑 비밀번호 바꿀 때 둘다 이용할 수 있음.
    public void addUser(String id, String pwd) {

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        //pwd 암호화
        final String hashedPwd = Hashing.sha256().hashString(pwd, StandardCharsets.UTF_8).toString();
        user.put("pwd", hashedPwd);

        db.collection("users").document(id)
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("hs_test", "회원가입/비밀번호 변경 완료");
                } else {
                    Log.d("hs_test", "회원가입/비밀번호 변경 실패");
                }
            }
        });
    }

    public void getUser(String id){

        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("hs_test", "DocumentSnapshot data" + document.getData());
                        find = true;
                        Log.d("hs_test", "find : " + find);
                        //userDTO = document.toObject(UserDTO.class);
                        //Log.d("hs_test", "userDTO : " + userDTO.getId());
                    }
                    else{
                        Log.d("hs_test", "No such document");
                    }
                }
                else{
                    Log.d("hs_test", "get failed with", task.getException());
                }
            }
        });

        try {
            Thread.sleep(3000);
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }

        //Log.d("hs_test", "userDTO : " + userDTO.getId());
        Log.d("hs_test", "return find : " + find);
        //return userDTO;

    }

    public void addBook(String userID, BookDTO book) {
        db.collection("books").add(book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.v(TAG, "book data add : " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "book data add fail");
                    }
                });
    }

    public void updateBook(String id, BookDTO book) {
        db.collection("books")
                .document(id)
                .set(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v(TAG, "book update success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "book update fail");
                    }
                });
    }

    public void addData() {

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
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void deleteData(final String collection, final String id) {
        db.collection(collection).document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v(TAG, collection + "에서 " + id + " 삭제 성공");
                } else {
                    Log.v(TAG, collection + "에서 " + id + " 삭제 실패");
                }
            }
        });
    }

    public void updateData(String collection, String id, Map<String, Object> data) {
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        // data1.put("state", "CA");
        // data1.put("country", "USA");
        // data1.put("capital", false);
        // data1.put("population", 860000);
        // data1.put("regions", Arrays.asList("west_coast", "norcal"));


        //db.collection("컬렉션명").document("바꾸고 싶은 데이터 고유 아이디").update(data1);
        //db.collection("clothes").document("53ii8P0Bax7znoZeQ3yM").update(data1);//예시
        db.collection(collection).document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


    public void getData(String collection, String id) {
        db.collection(collection).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.v(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.v(TAG, "No such document");
                            }
                        } else {
                            Log.v(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void getRandomMemo(String userId) {
        db.collection("memos").whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total = task.getResult().size();
                            Random r = new Random();
                            int i = r.nextInt(total);
                            memo = task.getResult().getDocuments().get(i).getData();
                            if(memo==null){
                                Log.v(TAG,"memo null");
                            }else{
                                Log.v(TAG,"memo"+memo.get("book_name"));
                            }
                        } else {
                            Log.w(TAG, "Error getting memo documents.", task.getException());
                        }
                    }
                });
    }

    public ArrayList<String> getDatas(String collection, String criteria, boolean order) {
        final ArrayList<String> a = new ArrayList<>();
        if (order) {
            db.collection(collection).orderBy(criteria, Query.Direction.DESCENDING)
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
            db.collection(collection).orderBy(criteria, Query.Direction.ASCENDING)
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

    public void uploadImage(Bitmap bitmap, String name) {

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
    public void uploadImage(ImageView imageView, String name) {
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
    public void setImageViewFromDB(final Context con, final ImageView imageView, String name) {
        StorageReference httpsReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/" + name + ".jpg");

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
