package edu.bluejack20_1.SOwhaDZ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TranslateItActivity extends AppCompatActivity {

    private TextView txtlanguage, txtCountdown, txtPoints, txtHighscore, txtChoice1, txtChoice2, txtChoice3, txtChoice4, title,txtattempt;
    private CardView cdChoice1, cdChoice2, cdChoice3, cdChoice4;
    private Button btnBack;

    WordLibrary wl = new WordLibrary();
    WordAnswer wa = new WordAnswer();
    int wrongCount = 0;
    int score = 0;
    int index;
    ArrayList<TextView> choices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SplashScreen.sharedRef.loadNighModeState()) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_it);

//        Initliazitation
//        TEXT VIEW
        txtlanguage = findViewById(R.id.translate_it_language);
        txtCountdown = findViewById(R.id.translate_it_countdown);
        txtPoints = findViewById(R.id.translate_it_point);
        txtHighscore = findViewById(R.id.translate_it_highscore);
        title = findViewById(R.id.translate_it_title);
        txtattempt = findViewById(R.id.translate_it_attempts);

        //  JAWABAN
        txtChoice1 = findViewById(R.id.translate_it_choice1_text);
        txtChoice2 = findViewById(R.id.translate_it_choice2_text);
        txtChoice3 = findViewById(R.id.translate_it_choice3_text);
        txtChoice4 = findViewById(R.id.translate_it_choice4_text);

//            CARDVIEW
        cdChoice1 = findViewById(R.id.translate_it_choice1_card);
        cdChoice2 = findViewById(R.id.translate_it_choice2_card);
        cdChoice3 = findViewById(R.id.translate_it_choice3_card);
        cdChoice4 = findViewById(R.id.translate_it_choice4_card);

        btnBack = findViewById(R.id.translate_it_back_button);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TranslateItActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//            SET TEXT
//        txtHighscore.setText("HIGHSCORE");
//        txtlanguage.setText("LANGUAGE");
//        getData();
//        txtPoints.setText("POINTS");
//        txtCountdown.setText("60s");
//        title.setText("BONJOUR");

        //  CHOICE
//        txtChoice1.setText("CHOICE 1");
//        txtChoice2.setText("CHOICE 2");
//        txtChoice3.setText("CHOICE 3");
//        txtChoice4.setText("CHOICE 4");


        choices.add(txtChoice1);
        choices.add(txtChoice2);
        choices.add(txtChoice3);
        choices.add(txtChoice4);
        txtHighscore.setText(getResources().getString(R.string.highscore) + " : "+FragmentHome.highscore +"");
        txtattempt.setText((3-wrongCount)+ " "  + getResources().getString(R.string.attempt_left));
        resetQuestion();

        final CountDownTimer countDownTimer = new CountDownTimer(11000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtCountdown.setText(millisUntilFinished/1000 + "s " + getResources().getString(R.string.time_left));
                if(wrongCount < 3){
                    cdChoice1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wrongCount < 3){
                                if(txtChoice1.getText().toString().equals(choices.get(index).getText().toString())){
                                    score++;
                                }
                                else{
                                    wrongCount++;
                                    txtattempt.setText((3-wrongCount)+ " "  + getResources().getString(R.string.attempt_left));
                                }
                                resetQuestion();
                                start();
                            }
                        }
                    });
                    cdChoice2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wrongCount < 3){
                                if(txtChoice2.getText().toString().equals(choices.get(index).getText().toString())){
                                    score++;
                                }
                                else{
                                    wrongCount++;
                                    txtattempt.setText((3-wrongCount)+" "  + getResources().getString(R.string.attempt_left));
                                }
                                resetQuestion();
                                start();
                            }
                        }
                    });

                    cdChoice3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wrongCount < 3){
                                if(txtChoice3.getText().toString().equals(choices.get(index).getText().toString())){
                                    score++;
                                }
                                else{
                                    wrongCount++;
                                    txtattempt.setText((3-wrongCount)+" "  + getResources().getString(R.string.attempt_left));
                                }
                                resetQuestion();
                                start();
                            }
                        }
                    });

                    cdChoice4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wrongCount < 3){
                                if(txtChoice4.getText().toString().equals(choices.get(index).getText().toString())){
                                    score++;

                                }
                                else{
                                    wrongCount++;
                                    txtattempt.setText((3-wrongCount)+" "  + getResources().getString(R.string.attempt_left));
                                }
                                resetQuestion();
                                start();
                            }

                        }
                    });
                }
                else{
                    title.setText(getResources().getString(R.string.you_lost));
                    cancel();
                    if(score > FragmentHome.highscore){
                        txtHighscore.setText(getResources().getString(R.string.highscore) +" : "+score+"");
                        updateUser();
                    }
                }



                txtPoints.setText(score +" pts");
            }

            @Override
            public void onFinish() {
                if(wrongCount < 3){
                    resetQuestion();

                    this.start();
                    wrongCount++;
                    txtattempt.setText((3-wrongCount)+" "  + getResources().getString(R.string.attempt_left));

                }else{
                    title.setText("YOU LOSE");
                    cancel();
                    if(score > FragmentHome.highscore){
                        updateUser();
                        txtHighscore.setText(getResources().getString(R.string.highscore) + " : "+score+"");
                    }
                }


            }

        };

        countDownTimer.start();



    }

    private void getData(){
        if(getIntent().hasExtra("txtLanguage")){
            txtlanguage.setText(getIntent().getStringExtra("txtLanguage"));
//            data2 = getIntent().getStringExtra("data2");
        }else{
//            Toast.makeText(this,"NO LANGUAGE SELECTED",Toast.LENGTH_SHORT).show();
        }
    }

    private void resetQuestion(){
        if(txtlanguage.getText().toString().equals("INDONESIA")){
            title.setText(wl.getRandomWordIndo());

            Random random = new Random();
            String[] answers = new String[4];
            index = random.nextInt(choices.size());
            choices.get(index).setText(wa.getAnswerIndo(wl.getIndexIndo(title.getText().toString())));
            answers[index] = choices.get(index).getText().toString();
            for(int i =0;i<choices.size();i++){
                if(i != index){
                    String s;
                    int flag = 0;
                    do{
                        flag = 0;
                        s = wa.getAnswerIndo(wl.getIndexIndo(wl.getRandomWordIndo()));
                        for(int j=0;j<4;j++){
                            if(s.equals(answers[j])){
                                flag = 1;
                                break;
                            }
                        }
                    }while(choices.get(index).getText().toString().equals(s) || flag == 1);
                    choices.get(i).setText(s);
                    answers[i] = s;
                }
            }
        }
        else{
            title.setText(wl.getRandomwordEnglish());

            Random random = new Random();
            String[] answers = new String[4];
            index = random.nextInt(choices.size());
            choices.get(index).setText(wa.getAnswerEnglish(wl.getIndexEnglish(title.getText().toString())));
            answers[index] = choices.get(index).getText().toString();
            for(int i =0;i<choices.size();i++){
                if(i != index){
                    String s;
                    do{
                        s = wa.getAnswerEnglish(wl.getIndexEnglish(wl.getRandomwordEnglish()));
                        for(int j=0;j<4;j++){
                            if(s.equals(answers[j])){
                                s = choices.get(index).getText().toString();
                                break;
                            }
                        }
                    }while(choices.get(index).getText().toString().equals(s));
                    choices.get(i).setText(s);
                    answers[i] = s;
                }

            }
        }

    }

    private void updateUser(){
        try {
//                        Glide.with(FragmentUser.this).load(downloadUri).into(userProfile);
            Map<String, Object> taskMap = new HashMap<String, Object>();
            taskMap.put("highscore", score +"");
            FirebaseDatabase.getInstance().getReference().child("user").child(FragmentHome.idKey).updateChildren(taskMap);
        }catch (Exception e){

        }
    }
}