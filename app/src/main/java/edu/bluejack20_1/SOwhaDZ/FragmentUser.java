package edu.bluejack20_1.SOwhaDZ;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import Model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUser extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] listItems;
    private String preferredLanguage ="en";


    Uri imguri;
    StorageReference sref;




    //    Component
    private Button btnPreferredLanguage, btnShare, btnHistory, btnSignOut, btnChangeTheme;
    private ImageView userProfile;
    private TextView userName, userEmail;


    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mDatabase;
    DatabaseReference dbUser ;
    private User user;
    public static String id ="";
    String username = "";
    String photo ="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
    String email ="";
    String language = "en";
    String url;
    String urlThesaurasSyn ="";
    Uri personPhoto;
    String personName;
    String personEmail;

    String idKey, personId;

    public FragmentUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUser newInstance(String param1, String param2) {
        FragmentUser fragment = new FragmentUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view =  inflater.inflate(R.layout.fragment_user, container, false);
        sref = FirebaseStorage.getInstance().getReference("Images");

        btnPreferredLanguage = view.findViewById(R.id.user_preferred_language);
        btnHistory = view.findViewById(R.id.user_view_history);
        btnShare = view.findViewById(R.id.user_share_app);
        btnSignOut = view.findViewById(R.id.user_sign_out);

        userName = view.findViewById(R.id.user_username);
        userEmail = view.findViewById(R.id.user_email);
        userProfile = view.findViewById(R.id.user_profile_pic);
        btnChangeTheme = view.findViewById(R.id.user_change_theme);



        //Logic Save User To Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbUser= mDatabase.child("user");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());

        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
            id = personId;
            username = personName;

            if(personPhoto == null) {
                photo ="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
            }
            else{
                photo = personPhoto.toString();
            }

            Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(photo).into(userProfile);

            userEmail.setText(personEmail);
            userName.setText(username);
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
                    data.setIdKey(next.child("idKey").getValue().toString());
                    data.setUsername(next.child("username").getValue().toString());
                    data.setPhoto(next.child("photo").getValue().toString());
                    data.setLanguage(next.child("language").getValue().toString());
                    data.setEmail(next.child("email").getValue().toString());
                    data.setHighscore(0);
                    userList.add(data);
                }
                Boolean flag = false;
                for (int i=0;i<userList.size();i++){
                    if(userList.get(i).getEmail().equals(email)){
                        idKey = userList.get(i).getIdKey();
                        if (userList.get(i).getPhoto() == null || userList.get(i).getPhoto().equals("")) {
                            Glide.with(getActivity().getApplicationContext()).load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png").into(userProfile);
                        } else {
                            Glide.with(getActivity().getApplicationContext()).load(String.valueOf(userList.get(i).getPhoto())).into(userProfile);
                        }
//                        highscoreTranslateIt.setText("Highscore : " +userList.get(i).getHighscore() +"");
//                        userEmail.setText(userList.get(i).getEmail());
//                        userName.setText(userList.get(i).getUsername());
                        flag = true;
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        masukkin nama user dari db
//        userName.setText();

//        masukkin gambar user dari db

        btnPreferredLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                munculin modal
                listItems = new String[]{"Indonesia", "English"};
                MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
                mBuilder.setTitle("Choose the language");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
//                        .setText(listItems[i]);
                        if(i == 0){
                            preferredLanguage = "id";
                            updateUser();
                            setLocale("in");
                        }
                        if (i == 1){
                            preferredLanguage = "en";
                            updateUser();
                            setLocale("en");
                        }
                        dialog.dismiss();
                    }
                }).show();

            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          sign out
//                Toast.makeText(getContext(), "hello", 20000);
                signOut();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                share the app
                try{
                    Intent intentt = new Intent(Intent.ACTION_SEND);
                    intentt.setType("text/plain");
                    intentt.putExtra(Intent.EXTRA_SUBJECT,"SHARE APPS");
                    String sharemessage = "Download SOwhaDZ Apps in PlayStore now with this link!\nhttps://play.google.com/store/apps/details?="+BuildConfig.APPLICATION_ID+"\n";
                    intentt.putExtra(Intent.EXTRA_TEXT,sharemessage);
                    startActivity(Intent.createChooser(intentt,"Share App Via"));
                }catch (Exception e){
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                }
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        //        NEW
        btnChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SplashScreen.sharedRef.loadNighModeState()){
//                    Toast.makeText(getContext(), "LIGHT THEME", Toast.LENGTH_SHORT).show();
                    SplashScreen.sharedRef.setNightModeState(false);
                    restartApp();
                } else {
//                    Toast.makeText(getContext(), "DARK THEME", Toast.LENGTH_SHORT).show();
                    SplashScreen.sharedRef.setNightModeState(true);
                    restartApp();
                }
            }
        });


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
                    if(userList.get(i).getId().equals(FragmentHome.id)){
                        idKey = userList.get(i).getIdKey();
                        if (userList.get(i).getPhoto() == null || userList.get(i).getPhoto().equals("")) {

                            try {
                                Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png").into(userProfile);
                            }catch (Exception e){
                                userProfile = view.findViewById(R.id.user_profile_pic);
                            }
                        } else {
                            try {

                                Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(String.valueOf(userList.get(i).getPhoto())).into(userProfile);
                            }catch (Exception e){
                                userProfile = view.findViewById(R.id.user_profile_pic);
                            }
                        }
//                        Toast.makeText(getActivity(),idKey,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        toLogin();
                    }
                });

    }
    private void toLogin(){
        Intent intent = new Intent(getActivity(),IntroActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData()!=null){
            imguri = data.getData();
            FileUploader();
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploader(){
//        Toast.makeText(getActivity(),"ENTER",Toast.LENGTH_SHORT).show();
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
//                    try {
                        Glide.with(getActivity().getApplicationContext()).load(downloadUri).into(userProfile);
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("photo", downloadUri.toString());
                        FirebaseDatabase.getInstance().getReference().child("user").child(FragmentHome.idKey).updateChildren(taskMap);
//                    }catch (Exception e){
//
//                    }

                } else
                {
                    Toast.makeText(getActivity(), "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUser(){
        try {
//                        Glide.with(FragmentUser.this).load(downloadUri).into(userProfile);
            Map<String, Object> taskMap = new HashMap<String, Object>();
            taskMap.put("language", preferredLanguage +"");
            FirebaseDatabase.getInstance().getReference().child("user").child(FragmentHome.idKey).updateChildren(taskMap);
        }catch (Exception e){

        }
    }


    private void restartApp() {
        Intent intent = new Intent(getActivity().getApplicationContext(), getActivity().getClass());
        startActivity(intent);
        getActivity().finish();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh);
    }
}