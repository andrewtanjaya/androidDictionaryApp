package edu.bluejack20_1.SOwhaDZ;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import Model.History;
import Model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTranslate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTranslate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String[] listItems;
    private int[] listIcons;
    private Button mFrom, mTo, mSwap, mOcr, mSpeech, mTranslate, mListen, mViewHistory;
    private EditText inputText;
    private TextView outputText;
    DatabaseReference dbUser ;
    DatabaseReference mDatabase;


    RecyclerView historyRecycler;


    TextToSpeech textToSpeech;
    ImageView preview;
    Context applicationContext = MainActivity.getContextOfApplication();

    public static final int REQUEST_CODE_SPEECH_INPUT = 100;


    private Context context;

    ArrayList<History> s1;
    ArrayList<History> s2;
    int images[] = {};

    private String destination ="en";

    public FragmentTranslate() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_translate.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTranslate newInstance(String param1, String param2) {
        FragmentTranslate fragment = new FragmentTranslate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    final ArrayList<History> historiesWord = new ArrayList();
    final ArrayList<History> historiesDef = new ArrayList();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        setContentView(R.layout.activity_select_language_dialog);


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_translate, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        dbUser= mDatabase.child("history");

        dbUser.keepSynced(true);

        dbUser.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historiesDef.clear();
                historiesWord.clear();
                Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                int i=0;
                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    if(next.child("def").getValue().toString() != "" && next.child("userKey").getValue().toString().equals(FragmentHome.id)){
                        History his = new History();
                        his.setUserKey(next.child("userKey").getValue().toString());
                        his.setAntonym(next.child("antonym").getValue().toString());
                        his.setSynonym(next.child("synonym").getValue().toString());
                        his.setWord(next.child("word").getValue().toString());
                        his.setDef(next.child("def").getValue().toString());
                        his.setHistoryId(next.child("historyId").getValue().toString());
                        historiesDef.add(his);
                        historiesWord.add(his);
                    }
                }

                ArrayList<History> newDef = new ArrayList<>();
                ArrayList<History> newWord = new ArrayList<>();

                int size = 5;
                int position = historiesDef.size() - 1;
                for (int k = 0; k < size; k++){
                    if ((historiesDef.size()) > k){
                        newDef.add(historiesDef.get(position));
                        newWord.add(historiesWord.get(position));
                        position--;
                    }
                }

//                for(int k=0;historiesDef.get(k)!=null && k < 5 && historiesDef.size() > k;k++){
//                    newDef.add(historiesDef.get(k));
//                    newWord.add(historiesWord.get(k));
//                }

                s1 = newWord;
                s2 = newDef;


//                Collections.reverse(historiesDef);
//                Collections.reverse(historiesWord);
//
//                s1 = historiesWord;
//                s2 = historiesDef;

                historyRecycler = view.findViewById(R.id.history_recycler_views);

                final HistoryRecyclerAdapter historyAdapter = new HistoryRecyclerAdapter(getContext(),s1,s2);
                historyRecycler.setAdapter(historyAdapter);
                historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


                ItemTouchHelper.SimpleCallback swipeController = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    //        drag and drop (rearaange)
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        final MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(getActivity());

                        int position = viewHolder.getAdapterPosition();

                        mBuilder.setTitle(getResources().getString(R.string.confirm_deletion))
                                .setMessage(getResources().getString(R.string.are_you_sure_to_delete) + " " + s1.get(position).getWord() + " ?")
                                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        int position = viewHolder.getAdapterPosition();

                                        historyAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int position = viewHolder.getAdapterPosition();

                                        deleteOne(position);
                                        s1.remove(position);
                                        s2.remove(position);

                                        historyAdapter.notifyItemRemoved(position);
//                                        etHistoryInput.setText("");
                                    }
                                })
                                .show();



//                        int position = viewHolder.getAdapterPosition();
//                        deleteOne(position);
//                        s1.remove(position);
//                        s2.remove(position);
//
//                        historyAdapter.notifyItemRemoved(position);

//                        MASUKKIN KODINGNAN DELETE HISTORY ONE BY ONE

//                        switch (direction){
//                            case ItemTouchHelper.LEFT:
//                                s1.remove(position);
//                                s2.remove(position);
//                                historyAdapter.notifyItemRemoved(position);
//                                break;
//                            case ItemTouchHelper.RIGHT:
//                                s1.remove(position);
//                                s2.remove(position);
//                                historyAdapter.notifyItemRemoved(position);
//                                break;
//                        }
                    }
                };

                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                itemTouchhelper.attachToRecyclerView(historyRecycler);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        mFrom = (Button) Objects.requireNonNull(view.findViewById(R.id.fragment_from_select_language));
//        mFrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listItems = new String[]{"Indonesia", "India", "Iceland"};
//                listIcons = new int[] {R.drawable.indonesia, R.drawable.india, R.drawable.iceland};
//                MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
//                mBuilder.setTitle("Choose the language");
//                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        mFrom.setText(listItems[i]);
//                        dialog.dismiss();
//                    }
//                }).show();
//            }
//        });

        mTo = view.findViewById(R.id.fragment_to_select_language);
        mTo.setText("English");

        mTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems = new String[]{"Indonesia", "Chinese", "English", "Japanese", "Korean" , "Malay" , "Russian" , "Thai" };
                listIcons = new int[] {R.drawable.indonesia, R.drawable.india, R.drawable.iceland};
                MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
                mBuilder.setTitle("Choose the language");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mTo.setText(listItems[i]);
                        if(i == 0){
                            destination = "id";
                        }
                        if (i == 1){
                            destination = "zh";
                        }
                        if(i == 2){
                            destination = "en";
                        }
                        if(i == 3){
                            destination = "ja";
                        }
                        if(i == 4){
                            destination ="ko";
                        }
                        if(i == 5){
                            destination ="ms";
                        }
                        if(i == 6){
                            destination ="ru";
                        }
                        if(i == 7){
                            destination ="th";
                        }
                        dialog.dismiss();
                    }
                }).show();
            }
        });

//        mSwap = (Button) Objects.requireNonNull(view.findViewById(R.id.swap_language));
//
//        mSwap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String temp = "";
//                temp = mTo.getText().toString();
//                mTo.setText(mFrom.getText().toString());
//                mFrom.setText(temp);
//            }
//        });


//        ini input textnya
        inputText = (EditText) Objects.requireNonNull(view.findViewById(R.id.input_translate_text));
        if (!OCRActivity.resultTxt.equals("")) {
            inputText.setText(OCRActivity.resultTxt);
            OCRActivity.resultTxt = "";
        }

//        ini output textnya
        outputText = (TextView) Objects.requireNonNull(view.findViewById(R.id.output_translate_result));

        outputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("outputText",outputText.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
            }
        });
        // modif yang ini
        mOcr = (Button) Objects.requireNonNull(view.findViewById(R.id.ocr_button));

        mOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // masukkin codingan lu sini untu OCR
//                showImageImportedDialog();
                moveToNewActivity();
            }
        });


        mSpeech = (Button) Objects.requireNonNull(view.findViewById(R.id.speech_button));

        mSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // masukkin codingan lu sini untu SPEECH
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak!");
                startActivityForResult(speechIntent, REQUEST_CODE_SPEECH_INPUT);
            }
        });

        mTranslate = (Button) Objects.requireNonNull(view.findViewById(R.id.submit_translate));

        textToSpeech = new TextToSpeech(applicationContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){

                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED ){
                        Log.e("TTS","Language Not Supported");
                    }else{
                        mTranslate.setEnabled(true);
                    }
                }else{
                    Log.e("TTS","Init Failed");
                }
            }
        });

        mTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // masukkin codingan lu sini untu TRANSLATE
                mFrom.setText("Detecting..");
                FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
                identifier.identifyLanguage(inputText.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(s.equals("und")){
                            Toast.makeText(getActivity(),"Language Not Identified",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            getLanguageCode(s,destination);
                        }
                    }

                });

            }
        });

        mListen = (Button) Objects.requireNonNull(view.findViewById(R.id.text_to_speech));

        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // masukkin codingan lu sini untu TRANSLATE

                String text = inputText.getText().toString();

                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);


                textToSpeech.setSpeechRate(0.7f);

            }
        });

        mViewHistory = view.findViewById(R.id.view_history_btn);

        mViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });


        // NEW BESOK MIGRATE


        return view;
    }

    private void getLanguageCode(String s,String d) {
        int langCode = 11;
        int desCode = 11;
        switch (s){
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                mFrom.setText("English");
                textToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case "ja" :
                langCode = FirebaseTranslateLanguage.JA;
                mFrom.setText("Japanese");
                textToSpeech.setLanguage(Locale.JAPANESE);
                break;
            case "ko" :
                langCode = FirebaseTranslateLanguage.KO;
                textToSpeech.setLanguage(Locale.KOREA);
                mFrom.setText("Korean");
                break;
            case "ms" :
                langCode = FirebaseTranslateLanguage.MS;
                mFrom.setText("Malay");
                break;
            case "ru" :
                langCode = FirebaseTranslateLanguage.RU;
                mFrom.setText("Russian");
                break;
            case "th" :
                langCode = FirebaseTranslateLanguage.TH;
                mFrom.setText("Thai");
                break;
            case "zh" :
                langCode = FirebaseTranslateLanguage.ZH;
                textToSpeech.setLanguage(Locale.CHINESE);
                mFrom.setText("Chinese");
                break;
            case "id" :
                langCode = FirebaseTranslateLanguage.ID;
                mFrom.setText("Indonesian");
                break;
        }
        switch (d){

            case "en":
                desCode = FirebaseTranslateLanguage.EN;
                break;
            case "ja" :
                desCode = FirebaseTranslateLanguage.JA;
                break;
            case "ko" :
                desCode = FirebaseTranslateLanguage.KO;
                break;
            case "ms" :
                desCode = FirebaseTranslateLanguage.MS;
                break;
            case "ru" :
                desCode = FirebaseTranslateLanguage.RU;
                break;
            case "th" :
                desCode = FirebaseTranslateLanguage.TH;
                break;
            case "zh" :
                desCode = FirebaseTranslateLanguage.ZH;
                break;
            case "id" :
                desCode = FirebaseTranslateLanguage.ID;
                break;
        }

        translateText(langCode,desCode);
    }

    private void translateText(int langCode, int desCode) {
        outputText.setText("Translating...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(langCode).setTargetLanguage(desCode).build();
        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(inputText.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        outputText.setText(s);
                        saveHistory(inputText.getText().toString() , outputText.getText().toString());
                    }
                });
            }
        });
    }

    private void moveToNewActivity () {

        Intent i = new Intent(getActivity(), OCRActivity.class);
        getActivity().finish();
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
        getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_SPEECH_INPUT){
                if(resultCode == Activity.RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputText.setText(result.get(0));
                }
            }

        }
    }

    public void setInputText(String text){
        inputText.setText(text);
    }

    private void saveHistory(String word, String def)
    {
        History history = new History();
//
        String idKey = dbUser.push().getKey();
        history.setAntonym("-");
        history.setSynonym("-");
        history.setWord(word);
        history.setDef(def);
        history.setHistoryId(idKey);
        history.setUserKey(FragmentHome.id.toString());
        //insert data
        dbUser.child(idKey).setValue(history);

//        Toast.makeText(this, "User Data Saved", Toast.LENGTH_SHORT).show();

    }

    public void deleteOne(int index){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("history").orderByChild("historyId").equalTo(s1.get(index).getHistoryId());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteAll(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("history").orderByChild("userKey").equalTo(FragmentHome.id.toString());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}