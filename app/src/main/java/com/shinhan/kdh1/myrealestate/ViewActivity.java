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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
 /*   private Runnable updateUI = new Runnable() {
        public void run() {
            ViewActivity.this.itemAdpater.notifyDataSetChanged();
        }
    };
*/

    private final String[] condition = {"투자금액순","권리가액순","등록일순"};

    ListView listView = null;
    String search = null;
    String user_id = null;
    String resultText1 = "";

    class Item {

        String indate;
        String location_gu;
        String location_name;
        String total_amt;
        String original_amt;
        String premium;
        String rent;
        String loan;
        String migration_fee;
        String tel_number;

        Item(String indate, String location_gu, String location_name ,String total_amt , String original_amt,
             String premium, String rent, String loan ,String migration_fee , String tel_number) {
      // Item( String indate, String location_gu, String location_name  ) {

            this.indate = indate;
            this.location_gu = location_gu;
            this.location_name = location_name;
            this.total_amt = total_amt;
            this.original_amt = original_amt;
            this.premium = premium;
            this.rent = rent;
            this.loan = loan;
            this.migration_fee = migration_fee;
            this.tel_number = tel_number;
        }
    }

    ArrayList<Item> itemList = new ArrayList<Item>();
    Context mContext = null;
    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.view_offer, null);
            }

            TextView text1View = (TextView)convertView.findViewById(R.id.indate);
            TextView text2View = (TextView)convertView.findViewById(R.id.location_gu);
            TextView text3View = (TextView)convertView.findViewById(R.id.location_name);
            TextView text4View = (TextView)convertView.findViewById(R.id.total_amt);
            TextView text5View = (TextView)convertView.findViewById(R.id.original_amt);

            Item item = itemList.get(position);

            text1View.setText(item.indate);
            text2View.setText(item.location_gu);
            text3View.setText(item.location_name);
            text4View.setText(item.total_amt);
            text5View.setText(item.original_amt);

            text1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout itemParent = (LinearLayout)v.getParent();

                    /*
                    String tel = "tel:01055551211";

                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

*/
                    Intent intent = new Intent(ViewActivity.this, DetailActivity.class);
                    intent.putExtra("indate", ((TextView)itemParent.findViewById(R.id.indate)).getText());

                    intent.putExtra("location_gu", ((TextView)itemParent.findViewById(R.id.location_gu)).getText());
                    intent.putExtra("location_name", ((TextView)itemParent.findViewById(R.id.location_name)).getText());
                    intent.putExtra("total_amt", ((TextView)itemParent.findViewById(R.id.total_amt)).getText());
                    intent.putExtra("original_amt", ((TextView)itemParent.findViewById(R.id.original_amt)).getText());
                    startActivity(intent);/*
                    //intent.putExtra("premium", );
                   // intent.putExtra("rent", ((TextView)itemParent.findViewById(R.id.rent)).getText());
                    //intent.putExtra("migration_fee", ((TextView)itemParent.findViewById(R.id.migration_fee)).getText());
                   // intent.putExtra("tel_number", ((TextView)itemParent.findViewById(R.id.tel_number)).getText());
                     //액티비티 이동*/
                }
            });

            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }
    ItemAdapter itemAdpater = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mContext = getApplicationContext();

        Intent intent = getIntent();
        String data = intent.getStringExtra("name");
        user_id = data;
        // SPINNER

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, condition);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        spinner1.setPrompt("시-구선택");
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((condition[position]).equals("투자금액순"))
                    search = "total_amt";
                else if ((condition[position]).equals("권리가액순"))
                    search = "original_amt";
                else if ((condition[position]).equals("등록일순"))
                    search = "indate";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((ArrayAdapter)spinner1.getAdapter()).notifyDataSetChanged();

        //search = spinner1.getSelectedItem().toString();

       /* new LoadUserList().execute("http://172.16.2.14:52273/offer");*/

       // CHECK BOX


       // new LoadUserList().execute("http://192.168.43.224:52273/offer");
        Button Button1 = (Button) findViewById(R.id.ButtonHome); //해당 버튼을 지정합니다.
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, MainActivity.class);
                intent.putExtra("name", user_id);
                startActivity(intent); //액티비티 이동
            }
        });

        Button Button2 = (Button) findViewById(R.id.ButtonOfferRegister); //해당 버튼을 지정합니다.
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, RegisterActivity.class);
                intent.putExtra("name", user_id);
                startActivity(intent); //액티비티 이동
            }
        });
        Button Button3 = (Button) findViewById(R.id.ButtonOffer); //해당 버튼을 지정합니다.
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, ViewActivity.class);
                intent.putExtra("name", user_id);
                startActivity(intent); //액티비티 이동
            }
        });


        Button Button4 = (Button) findViewById(R.id.ButtonLogin); //해당 버튼을 지정합니다.
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(ViewActivity.this, LoginActivity.class);
                intent.putExtra("name", user_id);
                startActivity(intent); //액티비티 이동
            }
        });
    }

    public int search(View view) {
        resultText1 = null;
        CheckBox option1 = (CheckBox) findViewById(R.id.myinterest); // option1체크박스
        // 체크되었을 때 값을 저장할 스트링 값

        if (option1.isChecked()) { // option1 이 체크되었다면
            resultText1 = "1";
        }
        else{
            resultText1 = "0";
        }


        if (user_id == null)
        {
            Toast.makeText(getApplicationContext(),"로그인을 해주세요!", Toast.LENGTH_LONG).show();

            return 0;
        }



        if((search).equals("total_amt")) {
            if (resultText1 == "1") {

                new LoadUserList().execute("http://172.16.2.14:52273/offer/total_amt/" + user_id);
            } else
                new LoadUserList().execute("http://172.16.2.14:52273/offer/total_amt");
        }
        else if((search).equals("original_amt")) {
            if (resultText1 == "1") {

                new LoadUserList().execute("http://172.16.2.14:52273/offer/original_amt/" + user_id);
            } else
                new LoadUserList().execute("http://172.16.2.14:52273/offer/original_amt");
        }
        else if((search).equals("indate")) {
            if (resultText1 == "1") {

                new LoadUserList().execute("http://172.16.2.14:52273/offer/indate/" + user_id);
            } else
                new LoadUserList().execute("http://172.16.2.14:52273/offer/indate");
        }
        return 0;

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
                itemList.clear();
                for(int i =0 ; i < array.length(); i++){

                    JSONObject obj = array.getJSONObject(i);
                    itemList.add(new Item( obj.getString("indate"),obj.getString("location_si"),obj.getString("location_name")
                           ,obj.getString("total_amt"),obj.getString("original_amt"),obj.getString("premium"),obj.getString("rent")
                            ,obj.getString("loan"),obj.getString("migration_fee"),obj.getString("tel_number")));
                   //     itemList.add(new Item( obj.getString("indate"),obj.getString("location_si"),obj.getString("location_name")));
                }
                itemAdpater = new ItemAdapter(ViewActivity.this, R.layout.view_offer,
                        itemList);
                ListView listView = (ListView)findViewById(R.id.listview);
                listView.setAdapter(itemAdpater);

            } catch (Exception e){
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