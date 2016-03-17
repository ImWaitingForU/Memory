package com.chan.app.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chan.app.utils.MyBmobUtils;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register_ok;
    private EditText et_re_name;
    private EditText et_re_pwd;
    private EditText et_re_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register_ok = (Button) findViewById(R.id.btn_register_ok);
        et_re_email = (EditText) findViewById(R.id.et_re_email);
        et_re_name = (EditText) findViewById(R.id.et_re_name);
        et_re_pwd = (EditText) findViewById(R.id.et_re_pwd);

        btn_register_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_re_name.getText().toString();
                String pwd = et_re_pwd.getText().toString();
                String email = et_re_email.getText().toString();
                MyBmobUtils.mySignUp(RegisterActivity.this, userName, pwd, email);
            }
        });

    }
}
