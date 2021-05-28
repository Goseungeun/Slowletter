package org.techtown.slowletter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView settingImange=findViewById(R.id.setting_icon);
        settingImange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });
        Button button1=findViewById(R.id.write_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),WriteLetter.class);
                startActivity(intent);
            }
        });


        Button button2=findViewById(R.id.inbox_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),InboxList.class);
                startActivity(intent);
            }
        });
    }
}