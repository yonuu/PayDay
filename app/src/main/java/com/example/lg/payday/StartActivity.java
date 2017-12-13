package com.example.lg.payday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StartActivity extends AppCompatActivity {


    TextView Date;
    TextView stateText;
    String nago;
    String nnow;
    int state;

    int res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        nago = ListActivity.dtos.get(ListActivity.lstNum).ago;
        nnow = ListActivity.dtos.get(ListActivity.lstNum).now;
        state = ListActivity.dtos.get(ListActivity.lstNum).state;
        Date = (TextView)findViewById(R.id.Date);
        stateText = (TextView)findViewById(R.id.textView3);

        if(state == 0){
            Date.setText(nnow);
            stateText.setText("출근완료");
        }
        else if(state==1){
            Date.setText(nago);
            stateText.setText("퇴근");
        }



        Log.w("testing",ListActivity.dtos.get(ListActivity.lstNum).workspace);
    }
}
