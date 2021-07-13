package edu.bluejack20_1.SOwhaDZ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import Model.History;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView historyRecycler;
    final ArrayList<History> historiesWord = new ArrayList();
    final ArrayList<History> historiesDef = new ArrayList();

    DatabaseReference dbUser ;
    DatabaseReference mDatabase;

    ArrayList<History> s1;
    ArrayList<History> s2;

//    COMPONENT
    private TextView tvHistoryCount;
    private EditText etHistoryInput;
    private Button btnClearAll, btnBack;

    HistoryRecyclerAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SplashScreen.sharedRef.loadNighModeState()) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

//        Component
        etHistoryInput = findViewById(R.id.history_page_input);
        btnClearAll = findViewById(R.id.history_clear_all_btn);

        tvHistoryCount = findViewById(R.id.history_history_counts);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dbUser= mDatabase.child("history");

        dbUser.keepSynced(true);

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
                Collections.reverse(historiesDef);
                Collections.reverse(historiesWord);
                s1 = historiesWord;
                s2 = historiesDef;

                historyRecycler = findViewById(R.id.history_recycler_views);
                String historyCount = s1.size() + " " + getResources().getString(R.string.history_count);
                tvHistoryCount.setText(historyCount);

                historyAdapter = new HistoryRecyclerAdapter(HistoryActivity.this,s1,s2);
                historyRecycler.setAdapter(historyAdapter);
                historyRecycler.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

                ItemTouchHelper.SimpleCallback swipeController = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    //        drag and drop (rearaange)
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        final MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(HistoryActivity.this);

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
                                        etHistoryInput.setText("");
                                    }
                                })
                                .show();


//                        int position = viewHolder.getAdapterPosition();
////                        Toast.makeText(HistoryActivity.this,s1.get(position).getWord().toString(),Toast.LENGTH_SHORT).show();
//                        deleteOne(position);
//                        s1.remove(position);
//                        s2.remove(position);
//
//                        historyAdapter.notifyItemRemoved(position);
//                        etHistoryInput.setText("");
//                        MASUKKIN KODINGNAN DELETE HISTORY ONE BY ONE


                    }
                };

                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                itemTouchhelper.attachToRecyclerView(historyRecycler);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnBack = findViewById(R.id.history_back_button);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//               Clear all button
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etHistoryInput.getText().toString();
//                HAPUS SEMUA FUNCTION
                final MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(HistoryActivity.this);

//                int position = viewHolder.getAdapterPosition();

                mBuilder.setTitle(getResources().getString(R.string.confirm_deletion))
                        .setMessage(getResources().getString(R.string.are_you_sure_to_delete) + " " + getResources().getString(R.string.all_history)+ " ?")
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                int position = viewHolder.getAdapterPosition();

//                                historyAdapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                int position = viewHolder.getAdapterPosition();
////
////                                deleteOne(position);
////                                s1.remove(position);
////                                s2.remove(position);
                                deleteAll();

                                historyAdapter.notifyDataSetChanged();

//                                historyAdapter.notifyItemRemoved(position);
//                                        etHistoryInput.setText("");
                            }
                        })
                        .show();
//                deleteAll();
            }
        });
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(historyRecycler);


        etHistoryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String s) {
        ArrayList<History> filteredWord = new ArrayList<>();
        ArrayList<History> filteredDef = new ArrayList<>();

        for(int i=0;i<historiesWord.size();i++){
            if(historiesWord.get(i).getWord().toLowerCase().contains(s.toLowerCase())){
                filteredWord.add(historiesWord.get(i));
                filteredDef.add(historiesDef.get(i));
            }
        }

        s1 = filteredWord;
        s2 = filteredDef;
        historyRecycler = findViewById(R.id.history_recycler_views);

        historyAdapter = new HistoryRecyclerAdapter(HistoryActivity.this,s1,s2);
        historyRecycler.setAdapter(historyAdapter);
        historyRecycler.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        ItemTouchHelper.SimpleCallback swipeController = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //        drag and drop (rearaange)
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                final MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(HistoryActivity.this);

                Toast.makeText(HistoryActivity.this, "SWIPERD", Toast.LENGTH_LONG).show();
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
                                etHistoryInput.setText("");
                            }
                        })
                        .show();

            }
//                int position = viewHolder.getAdapterPosition();
//                s1.remove(position);
//                s2.remove(position);
//                historyAdapter.notifyItemRemoved(position);

//                        MASUKKIN KODINGNAN DELETE HISTORY ONE BY ONE



        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(historyRecycler);
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