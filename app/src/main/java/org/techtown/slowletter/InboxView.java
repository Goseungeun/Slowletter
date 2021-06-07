package org.techtown.slowletter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class InboxView extends AppCompatActivity {

    private int id;   //몇번째 편지인지 표시할 정수
    private TextView Tv_date;       //0000년 00월 00일로부터 온 편지
    private ImageView weatherIcon;  //날씨 아이콘
    private ImageView pictureImageView; //사진
    private TextView cont_letter;       //편지 내용

    public InboxView() {
        // error로 인해 하나 만들음
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inboxview);

        Tv_date = findViewById(R.id.textView_date);
        weatherIcon= findViewById(R.id.review_weatherIcon);
        pictureImageView=findViewById(R.id.review_pictureImageView);
        cont_letter=findViewById(R.id.review_cont_letter);

        //뒤로가기 버튼
        ImageButton inbox = (ImageButton) findViewById(R.id.BackButton2);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InboxList.class);
                startActivity(intent);
            }
        });

        //intent 값 받아오기
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");

        //DBconnection
        DBconnection();
    }

    private void DBconnection() {

        LetterDatabase db = LetterDatabase.getInstance(getApplicationContext());
        if(db!=null){
            String sql = "select * from "+LetterDatabase.TABLE_LETTER +" where _id = "+id;
            Cursor cursor = db.rawQUery(sql);

            //( 보낸날짜 구분자 '.', 받는날짜 구분자 '/')
            if(cursor.getCount()!=0){
                cursor.moveToNext();
                //db에서 값 가져오기
                String db_writedate = cursor.getString(cursor.getColumnIndex("WRITEDATE"));

                String[] writedate = db_writedate.split("\\.");
                Tv_date.setText(writedate[0]+"년 "+writedate[1]+"월 "+writedate[2]+"일로부터 온 편지");

                String WeatherText = cursor.getString(cursor.getColumnIndex("WEATHER"));
                imageWeather(WeatherText);
                String picturePath = cursor.getString(cursor.getColumnIndex("PICTURE"));

                pictureImageView.setImageURI(Uri.parse("file://" + picturePath));
                cont_letter.setText(cursor.getString(cursor.getColumnIndex("CONTEXT")));
                cont_letter.setBackgroundColor(cursor.getInt(cursor.getColumnIndex("BACKCOLOR")));

            }
            cursor.close();

        }
    }

    private void imageWeather(String weatherText) {
        switch (weatherText){
            case "맑음": weatherIcon.setImageResource(R.drawable.weather_icon_1);break;
            case "구름 조금": weatherIcon.setImageResource(R.drawable.weather_icon_2);break;
            case "구름 많음": weatherIcon.setImageResource(R.drawable.weather_icon_3);break;
            case "흐림": weatherIcon.setImageResource(R.drawable.weather_icon_4);break;
            case "비": weatherIcon.setImageResource(R.drawable.weather_icon_5);break;
            case "눈/비": weatherIcon.setImageResource(R.drawable.weather_icon_6);break;
            case "눈": weatherIcon.setImageResource(R.drawable.weather_icon_7);break;
        }

    }
}