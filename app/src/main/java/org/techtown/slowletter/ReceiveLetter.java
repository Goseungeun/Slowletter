package org.techtown.slowletter;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiveLetter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiveletter);

        Button read = (Button)findViewById(R.id.readbutton);

        //편지 읽기 버튼 누르면 편지 내용 불러와서 보여주기
    }
}