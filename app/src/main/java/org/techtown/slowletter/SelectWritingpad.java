package org.techtown.slowletter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SelectWritingpad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectwritingpad);

        RadioGroup selectpad = (RadioGroup)findViewById(R.id.padgroup);
        Button apply = (Button)findViewById(R.id.apply);
        ImageButton back = (ImageButton)findViewById(R.id.BackButton);
        ImageView preview = (ImageView)findViewById(R.id.preview);

        preview.setVisibility(View.VISIBLE);

        //뒤로가기 버튼을 눌렀을 때
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WriteLetter.class);
                startActivity(intent);
            }
        });


        //라디오 그룹 선택값이 바뀌었을 때 실행
        selectpad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup selectpad, @IdRes int i) {
                //오늘 날씨 버튼이 눌렸을 때, 실행
                if(i == R.id.weather){
                    Toast.makeText(SelectWritingpad.this,"오늘의 날씨 선택",Toast.LENGTH_SHORT).show();
                    //미리보기 이미지 뷰 변경
                }

                //오늘 달의 위상 버튼이 눌렸을 때, 실행
                else if(i == R.id.moon){
                    Toast.makeText(SelectWritingpad.this,"오늘 달의 위상 선택",Toast.LENGTH_SHORT).show();
                    //미리보기 이미지 뷰 변경
                }

                //컬러팔레트 버튼이 눌렸을 때, 실행
                else if(i == R.id.colorselect){
                    Toast.makeText(SelectWritingpad.this,"컬러팔레트 선택",Toast.LENGTH_SHORT).show();
                    //미리보기 이미지 뷰 변경
                }

            }
        });

        //확인 버튼을 눌렀을 때, 바뀐 배경을 사용할 것인지 물어보는 팝업창 실행
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder((SelectWritingpad.this));
                builder.setMessage("선택한 편지지 배경을 적용하시겠습니까? ");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),WriteLetter.class);
                        Toast.makeText(SelectWritingpad.this,"편지지 배경이 적용되었습니다.",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertD = builder.create();
                alertD.show();
            }
        });
    }

}
