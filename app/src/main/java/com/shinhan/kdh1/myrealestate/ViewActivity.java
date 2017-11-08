package com.shinhan.kdh1.myrealestate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {


    private final String[] condition = {"투자금액순","권리가순","날짜순"};

    ListView listView = null;
    ItemAdapter itemAdpater = null;
    ArrayList<Item> itemList = new ArrayList<Item>();
    Context mContext = null;

    private Runnable updateUI = new Runnable() {
        public void run() {
            ViewActivity.this.itemAdpater.notifyDataSetChanged();
        }
    };
    class Item {

        String location_si;
        String location_name;
        Item(String title, String text) {

            this.location_si = title;
            this.location_name = text;
        }
    }
    class ItemAdapter extends ArrayAdapter {


        Context mContext;
        int rowResourceId;
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(this.rowResourceId, null);
            }
            Item item = itemList.get(position);
            if(item !=null){
                TextView text1View = (TextView)convertView.findViewById(R.id.title);
                TextView text2View = (TextView)convertView.findViewById(R.id.text);

                text1View.setText(item.location_si);
                text2View.setText(item.location_name);
            }
            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            mContext = context;
            itemList =  (ArrayList)objects;
            rowResourceId = resource;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mContext = getApplicationContext();
        itemAdpater = new ItemAdapter(ViewActivity.this, R.layout.view_offer,
                itemList);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(itemAdpater);
        new LoadUserList().execute("//192.168.42.148:52273/offer");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, condition);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        spinner1.setPrompt(" 시-구선택");
        spinner1.setAdapter(adapter1);

        Spinner locationSpinner = (Spinner) findViewById(R.id.spinner1);
        locationSpinner.getSelectedItem().toString();
       /* new LoadUserList().execute("http://172.16.2.14:52273/offer");*/

       // new LoadUserList().execute("http://192.168.0.14:52273/offer");
        new LoadUserList().execute("http://192.168.42.148:52273/offer");
        Button Button1 = (Button) findViewById(R.id.ButtonHome); //해당 버튼을 지정합니다.
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, MainActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button2 = (Button) findViewById(R.id.ButtonOfferRegister); //해당 버튼을 지정합니다.
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, RegisterActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });
        Button Button3 = (Button) findViewById(R.id.ButtonOffer); //해당 버튼을 지정합니다.
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, ViewActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });


        Button Button4 = (Button) findViewById(R.id.ButtonLogin); //해당 버튼을 지정합니다.
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, LoginActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });

    }

    class LoadUserList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ViewActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("사용자 목록 로딩 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {//s-->서버에서 받은 JSON문자열
            dialog.dismiss();
            try{
                JSONArray array = new JSONArray(s);
                ArrayList<String> strings = new ArrayList<String>();
                    Toast.makeText(getApplicationContext(),array.length()+"", Toast.LENGTH_LONG).show();
                    for(int i =0 ; i < array.length(); i++){

                    JSONObject obj = array.getJSONObject(i);

                    itemList.add(new Item( obj.getString("location_si"),obj.getString("location_name")));;
                        runOnUiThread(updateUI);
                }            } catch (Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try{
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return output.toString();
        }
    }
}