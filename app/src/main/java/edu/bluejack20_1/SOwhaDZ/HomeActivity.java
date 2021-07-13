package edu.bluejack20_1.SOwhaDZ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import Model.User;

public class HomeActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    Button userBtn ;
    DatabaseReference mDatabase;
    DatabaseReference dbUser ;
    private User user;
    String id ="";
    String username = "";
    String photo ="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
    String email ="";
    String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userBtn = findViewById(R.id.userBut);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        dbUser= mDatabase.child("user");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            id = personId;
            username = personName;
            if(personPhoto == null) {
                photo ="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
            }
            else{
                photo = personPhoto.toString();
            }
            email = personEmail;
        }

        final ArrayList<User> userList = new ArrayList<>();

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
                    if(userList.get(i).getEmail().equals(email)){
                        flag = true;
                    }
                }
                if(flag == false){
                    saveUser();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        user = new User();

    }

    private void saveUser()
    {

//
        String idKey = dbUser.push().getKey();
        user.setIdKey(idKey);
        user.setId(id);
        user.setEmail(email);
        user.setLanguage(language);
        user.setPhoto(photo);
        user.setUsername(username);
        user.setHighscore(0);
        //insert data
        dbUser.child(idKey).setValue(user);

//        Toast.makeText(this, "User Data Saved", Toast.LENGTH_SHORT).show();

    }

}