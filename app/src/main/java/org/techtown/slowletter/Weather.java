package org.techtown.slowletter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Weather extends AppCompatActivity {

    //청주 날씨 정보 주소
    private static final String WEATHER_URL="http://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4311259000";

    // 입력 스트림, InputStream ( 날씨 누리에서 데이터를 받을 스트림 객체 )
    InputStream inputStream;

    String WeatherText=null;        //파싱으로 날씨정보 받은거 저장용


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //icon imageview 연결

        //파싱 시작
        try
        {
            URL url = new URL(WEATHER_URL);     // 인터넷 주소 처리.
            inputStream = url.openStream(); // xml데이터를 입력 스트림으로 받는다.
            WeatherText = xmlParsing();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    //오늘의 날씨 정보를 parsing받기
    public String xmlParsing(){
        String result=null;
        String parsing_time = null; //파싱한 데이터중 시간관련된거

        //현재 시간 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("k");  //1~24로 나타낸다.
        String curtime = sdf.format(date);

        try {
            String tag = "";        // xml 태그명 저장 변수

            // parserfactory 생성
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            //parser 생성
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream,"UTF-8");


            // xml 문서의 요소 위치를 변수 eventType으로 저장.
            int eventType = parser.getEventType();

            //데이터 받아오기
            while(eventType != XmlPullParser.END_DOCUMENT){
                if((eventType==XmlPullParser.TEXT)&&tag.equals("hour")){
                    //여기서 parser.getText()가 가질 수 있는 값은 24,3,6,9,12,15,18,21
                    if(Integer.parseInt(parser.getText())==Integer.parseInt(curtime)){
                        while(eventType != XmlPullParser.END_DOCUMENT){
                            eventType = parser.next();
                            if((eventType==XmlPullParser.TEXT)&&tag.equals("wfKor")){
                                result = parser.getText();
                                break;
                            }
                        }
                       break;
                    }else if((Integer.parseInt(parser.getText())+1==Integer.parseInt(curtime)) ||(Integer.parseInt(parser.getText())+1-24==Integer.parseInt(curtime)) ){
                        while(eventType != XmlPullParser.END_DOCUMENT){
                            eventType = parser.next();
                            if((eventType==XmlPullParser.TEXT)&&tag.equals("wfKor")){
                                result = parser.getText();
                                break;
                            }
                        }
                        break;
                    }else if((Integer.parseInt(parser.getText())+2==Integer.parseInt(curtime)) ||(Integer.parseInt(parser.getText())+2-24==Integer.parseInt(curtime))){
                        while(eventType != XmlPullParser.END_DOCUMENT){
                            eventType = parser.next();
                            if((eventType==XmlPullParser.TEXT)&&tag.equals("wfKor")){
                                result = parser.getText();
                                break;
                            }
                        }
                        break;
                    }
                }
                eventType = parser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return result;
    }


}
