package edu.bluejack20_1.SOwhaDZ;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class AntonymOnly extends AsyncTask<String, Integer,String> {
    final String app_id = "9f90e13f";
    final String app_key = "965033b96db2d834aaf4a2ebfe743c7a";
    String myurl;
    //    JSONObject json;
    Context context;
    TextView def;

    AntonymOnly(Context context, TextView def){
        this.context = context;
        this.def = def;
//        this.syn = syn;
//        this.an = an;
    }
    @Override
    protected String doInBackground(String... params) {
        myurl = params[0];
        URL to;
        try {
            to =new URL (myurl);
            BufferedReader br=new BufferedReader(new InputStreamReader(to.openStream()));
            String inputLine;

            while((inputLine=br.readLine())!=null){
                System.out.println(inputLine); // in the form noun|syn|world ..
                return inputLine;
            }
        }catch (Exception e){
            Log.e("Eror Syn", "Erorr");
            return e.toString();
        }

        return "NULL";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String definition = "";
        String anto ="";
        try{
            JSONObject json = new JSONObject(s);
            JSONObject results = new JSONObject();
            try{
                results = json.getJSONObject("adjective");
            }catch(Exception e){
                try{
                    results = json.getJSONObject("verb");
                }catch(Exception b){
                    try{
                        results = json.getJSONObject("noun");
                    }catch(Exception c){
                        Toast.makeText(context, "No Synonym For This Word", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            try{
                JSONArray res = results.getJSONArray("syn");
                for (int i=0;i<5 && i < res.length();i++){
                    definition += res.getString(i) + "\n";
                }

            }catch(Exception e){
                definition += "-\n";

            }
            anto += "";
            try{
                JSONArray an = results.getJSONArray("ant");
                for (int i=0;i<5 && i < an.length();i++){
                    anto += an.getString(i) + "\n";
                }
            }catch(Exception c){
                anto += "-\n";
            }

            def.setText(anto);

        }catch(Exception e){
            System.out.println(e.toString());
            def.setText("-");
        }

    }
}
