package com.shinhan.kdh1.myrealestate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class RegisterActivity extends AppCompatActivity {
    private final String[] slocation1 = {"서울마포구", "서울강남구", "서울서초구", "서울송파", "서울성북"};
    private final String[] slocation2 = {"염리3구역", "공덕1구역", "아현1구역", "잠실5단지", "북아현3구역"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, slocation1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, slocation2);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner1.setPrompt(" 시-구선택");
        spinner1.setAdapter(adapter1);

        spinner2.setPrompt(" 지역선택");
        spinner2.setAdapter(adapter2);

        Button Button1 = (Button) findViewById(R.id.ButtonHome); //해당 버튼을 지정합니다.
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button2 = (Button) findViewById(R.id.ButtonOfferRegister); //해당 버튼을 지정합니다.
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });
        Button Button3 = (Button) findViewById(R.id.ButtonOffer); //해당 버튼을 지정합니다.
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(RegisterActivity.this, ViewActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button4 = (Button) findViewById(R.id.ButtonLogin); //해당 버튼을 지정합니다.
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });

    }
    // option1 체크박스가 눌렸을 때
/*
    public String Checked(View v) { // 체크되었을 때 동작할 메소드 구현
        // TODO Auto-generated method stub
        CheckBox option1 = (CheckBox) findViewById(R.id.public); // option1체크박스
        // 선언
        CheckBox option2 = (CheckBox) findViewById(R.id.jisang); // option1체크박스
        // 선언

        String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값
        if (option1.isChecked()) { // option1 이 체크되었다면
            resultText = "1";
        }
        if (option2.isChecked()) {
            resultText = "1"; // option2 이 체크되었다면
        }

        return resultText; // 체크된 값 리턴
    }
*/
    public void RegisterOffer(View view) {
        Spinner locationSpinner = (Spinner) findViewById(R.id.spinner1);
        Spinner locationSpinner2 = (Spinner) findViewById(R.id.spinner2);
        EditText totalamtText = (EditText) findViewById(R.id.tot_amt);
        EditText premiumText = (EditText) findViewById(R.id.premium);
        EditText rentText = (EditText) findViewById(R.id.rent);
        EditText loanText = (EditText) findViewById(R.id.loan);
        EditText movingText = (EditText) findViewById(R.id.moving);
        EditText originalamtText = (EditText) findViewById(R.id.original_amt);

        new registerOffer().execute(
                "http://172.16.2.14:52273/user/offer",
                locationSpinner.getSelectedItem().toString(),
                locationSpinner2.getSelectedItem().toString(),
                totalamtText.getText().toString(),
                premiumText.getText().toString(),
                rentText.getText().toString(),
                loanText.getText().toString(),
                movingText.getText().toString(),
                originalamtText.getText().toString());
    }

    class registerOffer extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                //postDataParams.put("user_id", params[1]);
                postDataParams.put("location_si", params[1]);
                postDataParams.put("location_name", params[2]);
                postDataParams.put("total_amt", params[3]);
                postDataParams.put("original_amt", params[4]);
                postDataParams.put("premium", params[5]);
                postDataParams.put("rent", params[6]);
                postDataParams.put("loan", params[7]);
                postDataParams.put("migration_fee", params[8]);

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
}
