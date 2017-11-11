package com.shinhan.kdh1.myrealestate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    String user_id;
    String su_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        String data = intent.getStringExtra("name");

        su_user_id = data;

        Button Button = (Button) findViewById(R.id.ButtonJoin); //해당 버튼을 지정합니다.
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                intent.putExtra("name",su_user_id);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button1 = (Button) findViewById(R.id.ButtonHome); //해당 버튼을 지정합니다.
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("name",su_user_id);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button2 = (Button) findViewById(R.id.ButtonOfferRegister); //해당 버튼을 지정합니다.
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("name",su_user_id);
                startActivity(intent); //액티비티 이동
            }
        });
        Button Button3 = (Button) findViewById(R.id.ButtonOffer); //해당 버튼을 지정합니다.
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(LoginActivity.this, ViewActivity.class);
                intent.putExtra("name",su_user_id);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button4 = (Button) findViewById(R.id.ButtonLogin); //해당 버튼을 지정합니다.
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.putExtra("name",su_user_id);
                startActivity(intent); //액티비티 이동
            }
        });

    }

    public void login(View view) {
        EditText userIdText = (EditText) findViewById(R.id.user_id);
        EditText passwordText = (EditText) findViewById(R.id.password);
        user_id = userIdText.getText().toString();
        su_user_id = null;
        new Login().execute(
               "http://172.16.2.14:52273/user/login",
                userIdText.getText().toString(),
                passwordText.getText().toString());
    }

    class Login extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", params[1]);
                postDataParams.put("password", params[2]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while (true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("로그인 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//로그인 성공
                    String token = json.getString("token");
                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", token);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    //Toast.makeText(getApplicationContext(),user_id+"", Toast.LENGTH_LONG).show();
                    su_user_id = user_id;

                    intent.putExtra("name",su_user_id);
                    startActivity(intent);
                    finish();
                } else {//로그인 실패
                    Toast.makeText(LoginActivity.this,
                            "아이디가 없거나 암호가 틀렸습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}