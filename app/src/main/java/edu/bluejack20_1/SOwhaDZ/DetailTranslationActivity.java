package edu.bluejack20_1.SOwhaDZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

import java.util.Locale;

public class DetailTranslationActivity extends AppCompatActivity {

    private Button title, copy, speech, desc, btnBack;
    private TextView synonym, antonym;

    String data1, data2;
    String urlThesauras;
    public static final int REQUEST_CODE_SPEECH_INPUT = 100;

    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (SplashScreen.sharedRef.loadNighModeState()) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_translation);


        title = findViewById(R.id.detail_translate_title);
        desc = findViewById(R.id.detail_translate_desc);
        synonym = findViewById(R.id.detail_translate_card_synonym);
        antonym = findViewById(R.id.detail_translate_card_antonym);
        copy = findViewById(R.id.detail_translate_copy);
        speech = findViewById(R.id.detail_translate_speech);

        btnBack = findViewById(R.id.detail_back_button);

        getData();
        setData();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){

                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED ){
                        Log.e("TTS","Language Not Supported");
                    }else{
                        speech.setEnabled(true);
                    }
                }else{
//                    Log.e("TTS","Init Failed");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailTranslationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("desc",desc.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DetailTranslationActivity.this,"Copied",Toast.LENGTH_SHORT).show();
            }
        });

//         TEXT SPEECH
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = title.getText().toString();

                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);


                textToSpeech.setSpeechRate(0.7f);
            }
        });


//         COPY TITLE
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("title",title.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DetailTranslationActivity.this,"Copied",Toast.LENGTH_SHORT).show();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("title",title.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DetailTranslationActivity.this,"Copied",Toast.LENGTH_SHORT).show();
            }
        });
        requestApiButtonClickThesaurus();

    }

    private void getData(){
        if(getIntent().hasExtra("data1") && getIntent().hasExtra("data2")){
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
        }else{
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
//        ubah value di sini
        title.setText(data1);
        desc.setText(data2);
        synonym.setText("-");
        antonym.setText("-");
    }

    private String dictionaryEntriesThe() {
        final String language = "en";
        final String word = title.getText().toString();
        final String fields = "synonyms";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://words.bighugelabs.com/api/2/ae61da88c648b6f550daf3aa9d2749d6/"+word_id+"/json" ;

    }

    public void requestApiButtonClickThesaurus(){
        Synonym synClass = new Synonym(this,synonym,antonym);
        urlThesauras = dictionaryEntriesThe();
        synClass.execute(urlThesauras);
    }


    private void getLanguageCode(String s,String d) {
        int langCode = 11;
        int desCode = 11;
        switch (s){
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                textToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case "ja" :
                langCode = FirebaseTranslateLanguage.JA;
                textToSpeech.setLanguage(Locale.JAPANESE);
                break;
            case "ko" :
                langCode = FirebaseTranslateLanguage.KO;
                textToSpeech.setLanguage(Locale.KOREA);
                break;
            case "ms" :
                langCode = FirebaseTranslateLanguage.MS;
                break;
            case "ru" :
                langCode = FirebaseTranslateLanguage.RU;
                break;
            case "th" :
                langCode = FirebaseTranslateLanguage.TH;
                break;
            case "zh" :
                langCode = FirebaseTranslateLanguage.ZH;
                textToSpeech.setLanguage(Locale.CHINESE);
                break;
            case "id" :
                langCode = FirebaseTranslateLanguage.ID;
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

    }
}