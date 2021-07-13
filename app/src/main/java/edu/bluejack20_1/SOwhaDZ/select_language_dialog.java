package edu.bluejack20_1.SOwhaDZ;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

public class select_language_dialog extends AppCompatActivity {

    private String[] listItems;
    private int[] listIcons;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language_dialog);

        mBtn = findViewById(R.id.fragment_select_language);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems = new String[]{"Indonesia", "India", "Iceland"};
                listIcons = new int[] {R.drawable.indonesia, R.drawable.india, R.drawable.iceland};
                MaterialAlertDialogBuilder mBuilder = new MaterialAlertDialogBuilder(select_language_dialog.this);

                mBuilder.setTitle("Choose the language");
                mBuilder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mBtn.setText(listItems[i]);
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }
}