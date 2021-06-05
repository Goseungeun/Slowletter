package org.techtown.slowletter;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import petrov.kristiyan.colorpicker.ColorPicker;

public class WriteLetter extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private Button cancel;
    private Button send;
    private Button writingpad;
    private EditText contents;
    private EditText receivedate;

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_letter);

         cancel = (Button)findViewById(R.id.cancelbutton);
         send = (Button)findViewById(R.id.sendbutton);
         writingpad = (Button)findViewById(R.id.changeback);
         contents = (EditText) findViewById(R.id.cont_letter);
         receivedate = (EditText)findViewById(R.id.receivedate);


        //받는 날짜 클릭시 날짜 설정하는 datepicker 실행
        receivedate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new DatePickerDialog(WriteLetter.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //편지지 배경 선택 버튼 클릭시 색상 선택 다이얼로그 실행
        writingpad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openColorPicker();
            }

        });


        //취소 버튼 클릭시 정말 취소할 것인지 묻는 팝업창 실행
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder((WriteLetter.this));
                builder.setMessage("편지 작성을 취소하시겠습니까? \n 작성한 편지는 저장되지 않습니다.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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

    private void updateLabel() {
        String myFormat = "편지 받을 날짜 : yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText receivedate = (EditText)findViewById(R.id.receivedate);
        receivedate.setText(sdf.format(myCalendar.getTime()));
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();   //color 넣을 list

        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");

        colorPicker.setColors(colors).setColumns(5).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener(){
            @Override
            public void onChooseColor(int position, int color){
               contents.setBackgroundColor(color);
            }

            @Override
            public void onCancel(){

            }
        }).show();

    }
}

