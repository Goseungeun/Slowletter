package org.techtown.slowletter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



import petrov.kristiyan.colorpicker.ColorPicker;


public class WriteLetter extends AppCompatActivity {
    private static final String TAG = "WriteLetter";


    ImageView pictureImageView;
    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;
    boolean isPhotoCanceled;

    int selectedPhotoMenu;

    File file;
    Bitmap resultPhotoBitmap;

    int mMode = AppConstants.MODE_INSERT;

    Calendar myCalendar = Calendar.getInstance();
    private Button cancel;
    private Button send;
    private Button writingpad;
    private EditText contents;
    private EditText receivedate;

    private ImageView weatherIcon;


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
         weatherIcon = (ImageView)findViewById(R.id.weatherIcon);


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


        pictureImageView = (ImageView)findViewById(R.id.pictureImageView);
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPhotoCaptured || isPhotoFileSaved) {
                    showPhotoDialog(AppConstants.CONTENT_PHOTO_EX);
                } else {
                    showPhotoDialog(AppConstants.CONTENT_PHOTO);
                }
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

        //Weather 실행

        Weather weather = new Weather();
        weather.GetResult();

    }

    private void updateLabel() {
        String myFormat = "편지 받을 날짜 : yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText receivedate = (EditText)findViewById(R.id.receivedate);
        receivedate.setText(sdf.format(myCalendar.getTime()));
    }


    public void setPicture(String picturePath, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        resultPhotoBitmap = BitmapFactory.decodeFile(picturePath, options);

        pictureImageView.setImageBitmap(resultPhotoBitmap);
    }



    public void showPhotoDialog(int id) {
        AlertDialog.Builder builder = null;

        switch(id) {

            case AppConstants.CONTENT_PHOTO:
                builder = new AlertDialog.Builder(this);

                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectedPhotoMenu = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(selectedPhotoMenu == 0 ) {
                            showPhotoCaptureActivity();
                        } else if(selectedPhotoMenu == 1) {
                            showPhotoSelectionActivity();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                break;

            case AppConstants.CONTENT_PHOTO_EX:
                builder = new AlertDialog.Builder(this);

                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo_ex, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selectedPhotoMenu = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(selectedPhotoMenu == 0) {
                            showPhotoCaptureActivity();
                        } else if(selectedPhotoMenu == 1) {
                            showPhotoSelectionActivity();
                        } else if(selectedPhotoMenu == 2) {
                            isPhotoCanceled = true;
                            isPhotoCaptured = false;

                            pictureImageView.setImageResource(R.drawable.picture1);
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                break;

            default:
                break;
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showPhotoCaptureActivity() {
        if (file == null) {
            file = createFile();
        }

        Uri fileUri = FileProvider.getUriForFile(this,"org.techtown.DGU.fileprovider", file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, AppConstants.REQ_PHOTO_CAPTURE);
        }
    }

    private File createFile() {
        String filename = "capture.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File outFile = new File(storageDir, filename);

        return outFile;
    }

    public void showPhotoSelectionActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, AppConstants.REQ_PHOTO_SELECTION);
    }

    /**
     * 다른 액티비티로부터의 응답 처리
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case AppConstants.REQ_PHOTO_CAPTURE:  // 사진 찍는 경우
                Log.d(TAG, "onActivityResult() for REQ_PHOTO_CAPTURE.");

                Log.d(TAG, "resultCode : " + resultCode);

                setPicture(file.getAbsolutePath(), 8);

                break;

            case AppConstants.REQ_PHOTO_SELECTION:  // 사진을 앨범에서 선택하는 경우
                Log.d(TAG, "onActivityResult() for REQ_PHOTO_SELECTION.");

                Uri selectedImage = intent.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                resultPhotoBitmap = decodeSampledBitmapFromResource(new File(filePath), pictureImageView.getWidth(), pictureImageView.getHeight());
                pictureImageView.setImageBitmap(resultPhotoBitmap);
                isPhotoCaptured = true;

                break;

        }
    }

    public static Bitmap decodeSampledBitmapFromResource(File res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(),options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(),options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height;
            final int halfWidth = width;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private String createFilename() {
        Date curDate = new Date();
        String curDateStr = String.valueOf(curDate.getTime());

        return curDateStr;
    }






    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();   //color 넣을 list

        colors.add("#FFE4E1");
        colors.add("#FFEBCD");
        colors.add("#FFFFE0");
        colors.add("#FFF8DC");
        colors.add("#FAEBD7");
        colors.add("#FFEFD5");
        colors.add("#FFFACD");
        colors.add("#F5F5DC");
        colors.add("#FAF0E6");
        colors.add("#FDF5E6");


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

    public class Weather {

        //청주 날씨 정보 주소
        private static final String WEATHER_URL = "http://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4311259000";

        // 입력 스트림, InputStream ( 날씨 누리에서 데이터를 받을 스트림 객체 )
        InputStream inputStream;

        String WeatherText = null;          //파싱으로 날씨정보 받은거 저장용
        //ImageView weatherIcon;      //Icon Image

        //asyncTask 안쓰면 다운시킴.
        class WeatherHttpAsyncTask extends AsyncTask<String,Void,String> {

            //메서드가 실제로 통신 할 때 작동하는 함수
            @Override
            protected String doInBackground(String... strings) {
                Log.i("!!!!doInBackground", "!!!!");
                //파싱 시작
                try {
                    URL url = new URL(WEATHER_URL);     // 인터넷 주소 처리.
                    inputStream = url.openStream(); // xml데이터를 입력 스트림으로 받는다.
                    WeatherText = xmlParsing();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("!!!!doInBackground", "WeatherText :"+WeatherText+"!!!!");

                return WeatherText;
            }

            //통신이 끝난 후 마무리작업(UI작업 등)을 하는 함수
            @Override
            protected void onPostExecute(String s)
            {
                Log.i("!!!!onPostExecute", "Start!!!!");
                super.onPostExecute(s);
                if(WeatherText!=null){
                    weatherIcon = (ImageView) weatherIcon.findViewById(R.id.weatherIcon);
                    weatherIcon.setImageResource(R.drawable.arrow);
                }
            }

        }


        //결과물 실행할 함수
        public void GetResult(){
            Log.i("!!!!GetResult", "Start!!!!");
            WeatherHttpAsyncTask task = new WeatherHttpAsyncTask();
            task.execute();

            Log.i("!!!!GetResult", "End!!!!");
        }



        //오늘의 날씨 정보를 parsing받기
        public String xmlParsing() {
            String result = null;

            try {
                String tag = "";        // xml 태그명 저장 변수

                // parserfactory 생성
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                //parser 생성
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, "UTF-8");


                // xml 문서의 요소 위치를 변수 eventType으로 저장.
                int eventType = parser.getEventType();

                //데이터 받아오기
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    Log.i("!!!!eventType", "들어오긴함!!!!");
                    switch (eventType)
                    {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();     // 시작 태그의 이름을 가져온다.
                            break;
                        case XmlPullParser.TEXT:
                            if (tag.equals("wfKor")) {
                                Log.i("!!!!eventType", "wfKor : " + parser.getText() + "!!!!");
                                result = parser.getText();
                            }
                            break;
                    }
                    if(result!=null){ break;}
                    eventType = parser.next();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }


}

