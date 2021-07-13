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

import javax.net.ssl.HttpsURLConnection;

public class MyDictionary extends AsyncTask<String,Integer,String> {
    final String app_id = MyApplication.app_id;
    final String app_key =MyApplication.app_key;
    String myurl;
    //    JSONObject json;
    Context context;
    TextView def,syn,an;


    MyDictionary(Context context, TextView def){
        this.context = context;
        this.def = def;
//        this.syn = syn;
//        this.an = an;
    }
    @Override
    protected String doInBackground(String... params) {
        myurl = params[0];
        try {
            URL url = new URL(myurl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
//            Log.e("hasil",stringBuilder.toString());
            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String definition;
        String antonym;
        String synonym;
        try{
            JSONObject json = new JSONObject(s);
            JSONArray results = json.getJSONArray("results");

            JSONObject entries = results.getJSONObject(0);
            JSONArray array = entries.getJSONArray("lexicalEntries");

            JSONObject entriess = array.getJSONObject(0);
            JSONArray e = entriess.getJSONArray("entries");

            JSONObject jsonObject = e.getJSONObject(0);
            JSONArray senses = jsonObject.getJSONArray("senses");

            JSONObject de = senses.getJSONObject(0);
            JSONArray d = de.getJSONArray("definitions");


            definition = d.getString(0);

            def.setText(definition);
//            Toast.makeText(context,definition,Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Log.e("Def","Faileddd");
        }

//        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
