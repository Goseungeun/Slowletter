package org.techtown.slowletter;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Weather extends AppCompatActivity {
    private static final String WEATHER_URL="http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY="ckhpjWpsgG5rczTgZVLzVvvq2bPoSqs7f%2FJfjmQde7673c13RmmYrVcDW%2F75aYhOhNSjxK7ozv3PNcNLH1xkKg%3D%3D";

    ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //imageView=findViewById(R.id.);
        getWeatherInfo();



    }

    public void getWeatherInfo(){
        if()
    }

}
