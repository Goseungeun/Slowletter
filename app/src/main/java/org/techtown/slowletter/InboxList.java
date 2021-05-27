package org.techtown.slowletter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class InboxList extends AppCompatActivity {

    GridView inboxlist;
    InboxAdapter inboxlist_adpater;
    //임의로 dday배열 설정
    long []d_day_array = new long[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inboxlist);

        //뒤로가기 버튼
        ImageButton inbox = (ImageButton) findViewById(R.id.BackButton1);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        inboxlist = findViewById(R.id.inbox_gridview);
        inboxlist_adpater = new InboxAdapter(this);

        //DB에서 불러와야 할 내용
        inboxlist_adpater.addItem(new Inbox_Item(2015,10,20,2018,10,20));
        inboxlist_adpater.addItem(new Inbox_Item(2016,10,21,2017,10,20));
        inboxlist_adpater.addItem(new Inbox_Item(2017,5,7,2021,10,20));
        inboxlist_adpater.addItem(new Inbox_Item(2019,10,11,2022,10,20));
        inboxlist_adpater.addItem(new Inbox_Item(2020,1,1,2022,5,20));
        inboxlist_adpater.addItem(new Inbox_Item(2020,5,5,2023,10,20));
        inboxlist_adpater.addItem(new Inbox_Item(2020,7,21,2023,11,20));
        inboxlist_adpater.addItem(new Inbox_Item(2020,8,19,2023,11,20));
        inboxlist_adpater.addItem(new Inbox_Item(2021,4,4,2023,11,20));

        inboxlist.setAdapter(inboxlist_adpater);


    }

    public class InboxAdapter extends BaseAdapter {
        public ArrayList<Inbox_Item> items=new ArrayList<Inbox_Item>();
        Context context;

        public void addItem(Inbox_Item item){
            items.add(item);
        }

        public InboxAdapter(Context c){
            context=c;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Inbox_Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InboxItemViewHolder viewHolder;

            //DB에서 값을 가져올 때 바꿔야할 값 //지금 임의로 설정한 값임
            Inbox_Item inbox_item = items.get(position);

            //오늘 날짜
            Calendar today = Calendar.getInstance();
            //받을 날짜
            Calendar receive_day= Calendar.getInstance();
            receive_day.set(inbox_item.r_year,inbox_item.r_month,inbox_item.r_day);

            /// D-day 계산 시작
            //일단위로 값 가져오기
            long long_receive_day = receive_day.getTimeInMillis()/(24*60*60*1000);
            long long_today = today.getTimeInMillis()/(24*60*60*1000);

            //두 날짜 빼기
            long d_day = long_today-long_receive_day;
            d_day_array[position]=d_day;
            //receive day가 현재보다 미래면 -
            //receive day가 현재보다 과거면 + 가 나온다.
            ///D-day 계산 끝

            //inbox_item.xml과 연결

            if(d_day>=0){
                //d-day가 지났으면 open
                inbox_item.setLetter_open(true);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.inbox_item_open,parent,false);

                viewHolder = new InboxItemViewHolder();
                OpenbasicSetting(viewHolder,convertView,inbox_item);

            }else{
                //d-day가 안지났으면 close
                inbox_item.setLetter_open(false);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.inbox_item_close,parent,false);

                viewHolder = new InboxItemViewHolder();
                ClosebasicSetting(viewHolder,convertView,inbox_item,d_day);
            }

            //각각 클릭했을 때 해당 뷰로 이동
            inboxlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Inbox_Item selection = inboxlist_adpater.getItem(position);
                    if(selection.letter_open){
                        //intent를 이용해서 InboxView에 값 넘겨주기
                        Intent intent = new Intent(getApplicationContext(),InboxView.class);
                        /*intent.putExtra("position",position);
                        intent.putExtra("item", (Parcelable) items.get(position));*/
                        startActivity(intent);
                    }else{
                        Toast toast =Toast.makeText(getApplicationContext(),"편지가 열리기까지\n"+Math.abs(d_day_array[position])+"일 만큼 남았습니다", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            });

            return convertView;
        }

        //열린 편지 기본 ui 세팅
        public void OpenbasicSetting(InboxItemViewHolder viewHolder, View convertView, Inbox_Item inbox_item){
            viewHolder.d_day_Tv = (TextView)convertView.findViewById(R.id.receive_day_open_textview);
            viewHolder.d_day_Tv.setText(""+inbox_item.r_year+"."+inbox_item.r_month+"."+inbox_item.r_day);
            viewHolder.sent_Tv  = (TextView)convertView.findViewById(R.id.sent_day_open_textview);
            viewHolder.sent_Tv.setText(""+inbox_item.s_year+"."+inbox_item.s_month+"."+inbox_item.s_day);
        }

        //닫힌 편지 기본 ui 세팅
        public void ClosebasicSetting(InboxItemViewHolder viewHolder, View convertView, Inbox_Item inbox_item, long d_day){
            viewHolder.d_day_Tv = (TextView)convertView.findViewById(R.id.d_day_close_textview);
            viewHolder.d_day_Tv.setText("D - "+Math.abs(d_day));
            viewHolder.sent_Tv  = (TextView)convertView.findViewById(R.id.sent_day_close_textview);
            viewHolder.sent_Tv.setText(""+inbox_item.s_year+"."+inbox_item.s_month+"."+inbox_item.s_day);
        }
    }

}
