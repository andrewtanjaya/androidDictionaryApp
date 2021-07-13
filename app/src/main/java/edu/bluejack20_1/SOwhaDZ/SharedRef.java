package edu.bluejack20_1.SOwhaDZ;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedRef {
    SharedPreferences mySharedPref;

    public SharedRef(Context context){
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    //    save nightMode state : true / false
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    //    load night mode state
    public Boolean loadNighModeState(){
        Boolean state  = mySharedPref.getBoolean("NightMode", false);
        return state;
    }
}
