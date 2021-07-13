package edu.bluejack20_1.SOwhaDZ;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import Model.User;

import static edu.bluejack20_1.SOwhaDZ.App.CHANNEL_1_ID;
import static edu.bluejack20_1.SOwhaDZ.App.CHANNEL_2_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    private NotificationManagerCompat notificationManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String preferredLanguage="en";
    String[] listItems;

    private CardView cdwordOfTheDay, cdTranslateIt,cdImageUser;
    private TextView  descWordOfTheDay, highscoreTranslateIt, usernameTxtView;
    public static TextView titleWordOfTheDay;
    private ImageView imgUser;

// SYNONYM & ANTONYM
    private TextView synonymResult, antonymResult;
    private EditText synonymInput, antonymInput;
    private Button btnSynonym, btnAntonym;

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
    public static int highscore = 0;
    public static String idKey;
    DatabaseReference dDatabase;
    int day;
    int month;
    int year;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        final View view =  inflater.inflate(R.layout.fragment_home, container, false);
//        Calendar calendar = Calendar.getInstance();
//
//        Intent intent = new Intent(getApplicationContext())

//        Toast.makeText(getActivity(), "Notification Active", Toast.LENGTH_SHORT).show();

//        Initialization

//       Card View
        cdwordOfTheDay = view.findViewById(R.id.word_of_the_day_card);
        cdTranslateIt = view.findViewById(R.id.translate_it_card);
        cdImageUser = view.findViewById(R.id.user_image_card);

//        TextView
        titleWordOfTheDay = view.findViewById(R.id.word_of_the_day_title);
        descWordOfTheDay = view.findViewById(R.id.word_of_the_day_desc);
        highscoreTranslateIt = view.findViewById(R.id.translate_it_highscore);
        usernameTxtView = view.findViewById(R.id.home_your_name);

//        Image
        imgUser = view.findViewById(R.id.home_user_image);

//        titleWordOfTheDay.setText("Title");
//        descWordOfTheDay.setText("desc");
//        highscoreTranslateIt.setText(" : " + 123);

//        user
        usernameTxtView.setText(username);
        imgUser.setImageResource(R.drawable.sleep_icon);

//        SYNONYM & ANTONYM
        synonymInput = view.findViewById(R.id.synonym_card_input);
        antonymInput = view.findViewById(R.id.antonym_card_input);
        synonymResult = view.findViewById(R.id.synonym_card_result);
        antonymResult = view.findViewById(R.id.antonym_card_result);
        btnSynonym = view.findViewById(R.id.synonym_card_submit);
        btnAntonym = view.findViewById(R.id.antonym_card_submit);


        try {
            FileInputStream fin = getContext().openFileInput("date.txt");
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String c;
            while ((c = br.readLine()) != null) {
                sb.append(c);
            }

            String[] splitted = sb.toString().split(" ");
            int day = Integer.parseInt(splitted[0]);
            int month = Integer.parseInt(splitted[1]);
            int year = Integer.parseInt(splitted[2]);

            Calendar cld = new GregorianCalendar(year,month-1,day);

            if(cld.getTime().compareTo(Calendar.getInstance().getTime()) <= 0){
                //update tanggal
                //random
                WordLibrary wordLibrary = new WordLibrary();
                String word;
                word = wordLibrary.getRandomwordEnglish();

                FileOutputStream fos = null;
                try {
                    fos = getContext().openFileOutput("wotd.txt", Context.MODE_PRIVATE);
                    fos.write(word.getBytes());
//                    Toast.makeText(getActivity(),"Saatnya random wotd",Toast.LENGTH_LONG).show();
//                Toast.makeText(context, "Saved to " + context.getFilesDir() + "/" + "wotd.txt",
//                        Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                updateTanggal();

            }


            fin.close();
        } catch (FileNotFoundException e) {
            //klo date.txt g ada
            try {
                updateTanggal();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            //kalo fin.close error
            e.printStackTrace();
        }


        FileInputStream fis = null;
        try {
            fis = getContext().openFileInput("wotd.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            titleWordOfTheDay.setText(sb.toString());
        } catch (FileNotFoundException e) {
            String words;
            WordLibrary wordLibrarys = new WordLibrary();

            words = wordLibrarys.getRandomwordEnglish();
            titleWordOfTheDay.setText(words);
            FileOutputStream foss = null;
            try {
                foss = getContext().openFileOutput("wotd.txt", Context.MODE_PRIVATE);
                foss.write(words.getBytes());
//                Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/" + "wotd.txt",
//                        Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            } catch (IOException z) {
                z.printStackTrace();
            } finally {
                if (foss != null) {
                    try {
                        foss.close();
                    } catch (IOException j) {
                        j.printStackTrace();
                    }
                }
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        pindahin ke user page
        cdImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentUser()).addToBackStack(null).commit();
            }
        });


        cdwordOfTheDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailTranslationActivity.class);
                intent.putExtra("data1", titleWordOfTheDay.getText().toString());
                intent.putExtra("data2", descWordOfTheDay.getText().toString());
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        cdTranslateIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems = new String[]{"Indonesia", "English"};
                MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));

                mBuilder.setTitle("Choose the language");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
//                        .setText(listItems[i]);

                        Intent intent = new Intent(getContext(), TranslateItActivity.class);

                        if(i == 0){
                            preferredLanguage = "id";
                            intent.putExtra("txtLanguage", "INDONESIA");
                        }
                        if (i == 1){
                            preferredLanguage = "en";
                            intent.putExtra("txtLanguage", "ENGLISH");
                        }
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

                        dialog.dismiss();
                    }
                }).show();

            }
        });


        btnSynonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cari synonym
                String input = synonymInput.getText().toString();
//                synonymResult.setText();
                requestApiButtonClickThesaurusSyn();
            }
        });

        btnAntonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cari antonym
                String antonym = antonymInput.getText().toString();
//                antonymResult.setText();
                requestApiButtonClickThesaurusAnt();
            }
        });


        //Logic WOTD
//        titleWordOfTheDay.setText(NotificationReceiver.word);

        //Logic Save User To Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbUser= mDatabase.child("user");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());

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
            Glide.with(this).load(photo).into(imgUser);
            email = personEmail;
            usernameTxtView.setText(username);
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
                    data.setHighscore(Integer.parseInt(next.child("highscore").getValue().toString()));
                    userList.add(data);
                }
                Boolean flag = false;
                for (int i=0;i<userList.size();i++){
                    if(userList.get(i).getEmail().equals(email)){
                        try {

//                            highscoreTranslateIt.setText(getResources().getString(R.string.highscore)  + " : " +userList.get(i).getHighscore() +"");

                            highscoreTranslateIt.setText(getResources().getString(R.string.highscore)+ " : " + userList.get(i).getHighscore() +"");
                        }catch (Exception e){
                           highscoreTranslateIt = view.findViewById(R.id.translate_it_highscore);
//                            highscoreTranslateIt.setText("HIGHSCORES" + " : " + userList.get(i).getHighscore() +"");
                        }
                        idKey = userList.get(i).getIdKey();
                        highscore = userList.get(i).getHighscore();
                        flag = true;
                        try{
                            Glide.with(getActivity()).load(userList.get(i).getPhoto()).into(imgUser);
                        }catch (Exception e){

                        }
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









//        Intent intent = new Intent(getContext(), myBackgroundProcess.class);
//        intent.setAction("BackgroundProcess");
//
//        //Set the repeated Task
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(Calendar.HOUR_OF_DAY, 00);
//        calendar.set(Calendar.MINUTE, 00);
//        calendar.set(Calendar.SECOND, 00);
////
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10, pendingIntent);
//        Intent intent = new Intent(getContext(), myBackgroundProcess.class);
//        intent.setAction("BackgroundProcess");
//
//        //Set the repeated Task
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//

        if(titleWordOfTheDay.getText().toString().equals("") || titleWordOfTheDay.getText().toString().equals("Title")){

        }
        else{
//            requestApiButtonClick();
            DatabaseReference ddata;
            ddata = FirebaseDatabase.getInstance().getReference();
            ddata= ddata.child("definition");


            final ArrayList<String> definitionList = new ArrayList<>();

            ddata.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    definitionList.clear();
                    definitionList.add(snapshot.child(titleWordOfTheDay.getText().toString()).getValue().toString());
                    descWordOfTheDay.setText(definitionList.get(0));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }

    private void updateTanggal() throws IOException {


        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
//            titleWordOfTheDay.setText( year+" "+month+" "+day);
//            cld.add(Calendar.DAY_OF_MONTH,1);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        if(month == 1 || month == 3 || month==5 || month == 7 || month == 8 || month == 10 || month ==12){
            if(day == 31){
                if(month == 12){
                    taskMap.put("day", 1);
                    taskMap.put("month",1);
                    taskMap.put("year",year + 1);
                    day = 1;
                    month = 1;
                    year += 1;
                }
                else{
                    taskMap.put("day", 1);
                    taskMap.put("month",month +1);
                    taskMap.put("year",year);
                    day = 1;
                    month += 1;
                    year = year;
                }

            }
            else{
                taskMap.put("day", day+1);
                taskMap.put("month",month);
                taskMap.put("year",year);
                day += 1;
                month = month;
                year = year;
            }
        }
        else if(month == 2 && year %4 == 0){
            if(day == 29){
                if(month == 12){
                    taskMap.put("day", 1);
                    taskMap.put("month",1);
                    taskMap.put("year",year + 1);
                    day = 1;
                    month = 1;
                    year += 1;
                }
                else{
                    taskMap.put("day", 1);
                    taskMap.put("month",month +1);
                    taskMap.put("year",year);
                    day = 1;
                    month += 1;
                    year = year;
                }
            }
            else{
                taskMap.put("day", day+1);
                taskMap.put("month",month);
                taskMap.put("year",year);
                day += 1;
                month = month;
                year = year;
            }
        }
        else{
            if(day == 30){
                if(month == 12){
                    taskMap.put("day", 1);
                    taskMap.put("month",1);
                    taskMap.put("year",year + 1);
                    day = 1;
                    month = 1;
                    year += 1;
                }
                else{
                    taskMap.put("day", 1);
                    taskMap.put("month",month +1);
                    taskMap.put("year",year);
                    day = 1;
                    month += 1;
                    year = year;
                }

            }
            else{
                taskMap.put("day", day+1);
                taskMap.put("month",month);
                taskMap.put("year",year);
                day += 1;
                month = 1;
                year = year;
            }

        }
//            FirebaseDatabase.getInstance().getReference().child("lastRandom").updateChildren(taskMap);

        FileOutputStream fOut = getContext().openFileOutput("date.txt",Context.MODE_PRIVATE);
        String str = day + " " + month + " " + year;
        fOut.write(str.getBytes());
        fOut.close();
//        Toast.makeText(getActivity(),"update tanggal jadi besok "+day+" "+month+ " "+year,Toast.LENGTH_LONG).show();
        FileInputStream fis = null;
        try {
            fis = getContext().openFileInput("wotd.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            titleWordOfTheDay.setText(sb.toString());
        } catch (FileNotFoundException e) {
            String words;
            WordLibrary wordLibrarys = new WordLibrary();

            words = wordLibrarys.getRandomwordEnglish();
            titleWordOfTheDay.setText(words);
            FileOutputStream foss = null;
            try {
                foss = getContext().openFileOutput("wotd.txt", Context.MODE_PRIVATE);
                foss.write(words.getBytes());
//                Toast.makeText(getContext(), "Saved to " + getContext().getFilesDir() + "/" + "wotd.txt",
//                        Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            } catch (IOException z) {
                z.printStackTrace();
            } finally {
                if (foss != null) {
                    try {
                        foss.close();
                    } catch (IOException j) {
                        j.printStackTrace();
                    }
                }
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveUser()
    {
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

    }

    private String dictionaryEntries() {
        final String language = "en";
        final String word = titleWordOfTheDay.getText().toString();
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }



    public void requestApiButtonClick(){
        MyDictionary myDictionary = new MyDictionary(getContext(),descWordOfTheDay);
        url = dictionaryEntries();
        myDictionary.execute(url);
    }

    private String dictionaryEntriesThe() {
        final String language = "en";
        final String word = synonymInput.getText().toString();
        final String fields = "synonyms";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://words.bighugelabs.com/api/2/ae61da88c648b6f550daf3aa9d2749d6/"+word_id+"/json" ;

    }
    private String dictionaryEntriesTheAnt() {
        final String language = "en";
        final String word = antonymInput.getText().toString();
        final String fields = "synonyms";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://words.bighugelabs.com/api/2/ae61da88c648b6f550daf3aa9d2749d6/"+word_id+"/json" ;

    }

    public void requestApiButtonClickThesaurusSyn(){

        SynonymOnly synClass = new SynonymOnly(getContext(),synonymResult);
        urlThesaurasSyn = dictionaryEntriesThe();
        synClass.execute(urlThesaurasSyn);
    }
    public void requestApiButtonClickThesaurusAnt(){

        AntonymOnly synClass = new AntonymOnly(getContext(),antonymResult);
        urlThesaurasSyn = dictionaryEntriesTheAnt();
        synClass.execute(urlThesaurasSyn);
    }
//
//
//    public void sendOnChannel1(View v){
//        String message = "Word of the day";
//        String messageDesc = "New word of the day is coming";
//
//        Intent activityIntent = new Intent(getContext(), MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, activityIntent, 0);
//
//        Intent broadcastIntent = new Intent(getContext(), NotificationReceiver.class);
////        broadcastIntent.putExtra("title2", titleWordOfTheDay.getText().toString());
////        broadcastIntent.putExtra("desc2", descWordOfTheDay.getText().toString());
//        PendingIntent actionIntent = PendingIntent.getBroadcast(getContext(), 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification notification = new NotificationCompat.Builder(getContext(),CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.ic_round_search_24)
//                .setContentTitle(message)
//                .setContentText(messageDesc)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setContentIntent(contentIntent)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true)
//                .setColor(getResources().getColor(R.color.colorAccent))
//                .addAction(R.mipmap.ic_launcher, "Explore it", actionIntent)
//                .build();
//        notificationManager.notify(1, notification);
//
//    }
//
//
//    public void sendOnChannel2(View v){
//        String message = "222222  New word of the day is coming";
//
//        Notification notification = new NotificationCompat.Builder(getContext(),CHANNEL_2_ID)
//                .setSmallIcon(R.drawable.ic_round_arrow_right_alt_24)
//                .setContentTitle(message)
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//        notificationManager.notify(2, notification);
//
//    }






}