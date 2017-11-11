package com.shinhan.kdh1.myrealestate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    String s_location_si = null;
    String s_location_name = null;
    String s_total_amt = null;
    String s_original_amt= null;
    String s_premium = null;
    String s_rent = null;
    String s_loan = null;
    String s_migraion_fee = null;
    String s_tel_number = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        String data1 = intent.getStringExtra("indate");
        String data2 = intent.getStringExtra("location_gu");
        String data3 = intent.getStringExtra("location_name");
        String data4 = intent.getStringExtra("total_amt");
        String data5 = intent.getStringExtra("original_amt");

        /*
        TextView textView1 = (TextView) findViewById(R.id.location_gu) ;
        textView1.setText(data1) ;*/
        new LoadUserList().execute("http://172.16.2.14:52273/offer/detail/"+ data1+"/"+
        data4+"/"+data5);

    }

    public void telno(View view) {

        String tel = "tel:"+ s_tel_number;
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

    }

    class LoadUserList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(DetailActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("사용자 목록 로딩 중...");
            dialog.show();
        }

        protected void onPostExecute(String s) {//s-->서버에서 받은 JSON문자열
            super.onPostExecute(s);
            dialog.dismiss();

            try {
                JSONArray array = new JSONArray(s);
                ArrayList<String> strings = new ArrayList<String>();
                for(int i =0 ; i < array.length(); i++){

                    JSONObject obj = array.getJSONObject(i);
                    s_location_si = obj.getString("location_si");
                    s_location_name = obj.getString("location_name");
                    s_total_amt = obj.getString("total_amt");
                    s_original_amt = obj.getString("original_amt");
                    s_premium = obj.getString("premium");
                    s_rent = obj.getString("rent");
                    s_loan = obj.getString("loan");
                    s_migraion_fee = obj.getString("migration_fee");
                    s_tel_number = obj.getString("tel_number");
                }

                TextView textView11 = (TextView) findViewById(R.id.t_location_gu) ;
                textView11.setText(s_location_si) ;

                TextView textView12 = (TextView) findViewById(R.id.t_location_name) ;
                textView12.setText(s_location_name) ;

                TextView textView1 = (TextView) findViewById(R.id.t_tot_amt) ;
                textView1.setText(s_total_amt) ;

                TextView textView2 = (TextView) findViewById(R.id.t_original_amt) ;
                textView2.setText(s_original_amt) ;

                TextView textView3 = (TextView) findViewById(R.id.t_rent) ;
                textView3.setText(s_rent) ;

                TextView textView4 = (TextView) findViewById(R.id.t_loan) ;
                textView4.setText(s_loan) ;

                TextView textView5 = (TextView) findViewById(R.id.t_moving) ;
                textView5.setText(s_migraion_fee) ;

                TextView textView6 = (TextView) findViewById(R.id.t_premium) ;
                textView6.setText(s_premium) ;


                TextView textView7 = (TextView) findViewById(R.id.t_telno) ;
                textView7.setText(s_tel_number) ;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
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

    }

}
