package com.example.endline_v1.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectServerTask extends AsyncTask<String, String, String> {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected String doInBackground(String... strings) {
        Log.d("ASYNC TASK", "doInBackground");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        try{
            JSONObject object = new JSONObject();
            object.accumulate("user_uid", user.getUid());
            object.accumulate("user_token", strings[1]);

            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try{
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "text/html");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(object.toString());
                writer.flush();
                writer.close();

                InputStream inputStream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(conn != null){
                    conn.disconnect();
                }
                if(reader != null){
                    reader.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s == null){
            Log.w("SERVER RES", "ERROR");
        }else{
            Log.d("SERVER RES", s);
        }
    }
}
