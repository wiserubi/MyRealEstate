package com.shinhan.kdh1.myrealestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {
    private final String[] slocation1 = {"서울마포구", "서울강남구", "서울서초구", "서울송파", "서울성북"};
    private final String[] slocation2= {"염리3구역", "공덕1구역", "아현1구역", "잠실5단지", "북아현3구역"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, slocation1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, slocation2);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        spinner1.setPrompt(" 시-구선택");
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
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
}
