package org.techtown.slowletter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class InboxView extends AppCompatActivity {

    private int position;   //몇번째 편지인지 표시할 정수


    public InboxView() {
        // error로 인해 하나 만들음
    }

    Inbox_Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inboxview);

        //뒤로가기 버튼
        ImageButton inbox = (ImageButton) findViewById(R.id.BackButton2);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InboxList.class);
                startActivity(intent);
            }
        });


        //intent로 position값 받아오기
        // Intent InboxView_intent = getIntent();
        //InboxView_intent.getIntExtra("positino",position);
        //InboxView_intent.getSerializableExtra("items",);
        //item = InboxList.InboxAdapter.item.get(position);

    }
}