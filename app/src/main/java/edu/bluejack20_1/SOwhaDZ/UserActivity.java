package edu.bluejack20_1.SOwhaDZ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Model.User;

public class UserActivity extends AppCompatActivity {
    TextView username;
    Button changePic;
    Button translate;
    Button signout;
    ImageView profile;
    GoogleSignInClient mGoogleSignInClient;
    Uri imguri;
    StorageReference sref;
    DatabaseReference dbUser ;
    DatabaseReference mDatabase;
    String personName = "";
    String personEmail = "";
    String personId = "";
    Uri personPhoto ;
    String idKey="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        username = findViewById(R.id.username);
        changePic = findViewById(R.id.changePic);
        translate = findViewById(R.id.translateBut);
        signout = findViewById(R.id.signout);
        profile = findViewById(R.id.profile);

        sref = FirebaseStorage.getInstance().getReference("Images");

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.signout:
                        signOut();
                        break;
                    // ...
                }
            }

        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
             personEmail = acct.getEmail();
             personId = acct.getId();
             personPhoto = acct.getPhotoUrl();

            username.setText("Hi , " + personName);
            if (personPhoto == null) {
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png").into(profile);
            } else {
                Glide.with(this).load(String.valueOf(personPhoto)).into(profile);
            }
        }
        Button changePic = findViewById(R.id.changePic);
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });


        final ArrayList<User> userList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbUser= mDatabase.child("user");
        dbUser.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    User data = new User();
                    data.setId(next.child("id").getValue().toString());
                    data.setIdKey(next.child("idKey").getValue().toString());
                    data.setUsername(next.child("username").getValue().toString());
                    data.setPhoto(next.child("photo").getValue().toString());
                    data.setLanguage(next.child("language").getValue().toString());
                    data.setEmail(next.child("email").getValue().toString());
                    data.setHighscore(0);
                    userList.add(data);
                }
                String data ="";
                Boolean flag = false;
                for (int i=0;i<userList.size();i++){
                    data+=userList.get(i).getUsername()+"\n";
                    if(userList.get(i).getId().equals(personId)){
                        idKey = userList.get(i).getIdKey();
                        if (userList.get(i).getPhoto() == null || userList.get(i).getPhoto().equals("")) {
                            Glide.with(UserActivity.this).load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png").into(profile);
                        } else {
                            Glide.with(UserActivity.this).load(String.valueOf(userList.get(i).getPhoto())).into(profile);
                        }
                        Toast.makeText(UserActivity.this,idKey,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserActivity.this, "Signed Out Successfull!", Toast.LENGTH_SHORT).show();
                        toLogin();
                    }
                });
    }

    private void toLogin(){
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()!=null){
            imguri = data.getData();
            FileUploader();
        }

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploader(){
        Toast.makeText(UserActivity.this,"ENTER",Toast.LENGTH_SHORT).show();
        final StorageReference Ref = sref.child(System.currentTimeMillis()+ "." + getExtension(imguri));

        Ref.putFile(imguri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }
                return Ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful())
                {
                    Uri downloadUri = task.getResult();
                    Glide.with(UserActivity.this).load(downloadUri).into(profile);
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("photo", downloadUri.toString());
                    FirebaseDatabase.getInstance().getReference().child("user").child(idKey).updateChildren(taskMap);
                    profile.setImageURI(downloadUri);
                } else
                {
                    Toast.makeText(UserActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}