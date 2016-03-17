package com.chan.app.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ShowMemory extends AppCompatActivity {

    private EditText ed_showMyMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memory);

        initView();


    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        String time = bundle.getString("time");
        String objectId = bundle.getString("objectId");
        String allMsg = bundle.getString("allMsg");
        ed_showMyMemory = (EditText) findViewById(R.id.et_showMyMemory);
        ed_showMyMemory.setText("time:" + time + "objectId:" + objectId + "allMsg:" + allMsg);
    }
}
