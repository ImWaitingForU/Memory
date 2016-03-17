package com.chan.app.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chan.app.utils.MyBmobUtils;
import com.tencent.tauth.Tencent;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class LaunchActivity extends AppCompatActivity {
    private Button btn_register;
    private Button btn_login;
    private EditText et_name;
    private EditText et_pwd;
    private ImageButton ib_qq;

    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
         /* 初始化 Bmob SDK 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application
         ID*/
        Bmob.initialize(this, "d723d7f86d2c46b20827b7df8c58fab0");

        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        //  mTencent = Tencent.createInstance("1105162137", this.getApplicationContext());
        initViews();

    }

    private void initViews() {
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_re_name);
        et_pwd = (EditText) findViewById(R.id.et_re_pwd);
        ib_qq = (ImageButton) findViewById(R.id.ib_qq);

        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        if (bmobUser != null) {
            // 允许用户使用应用
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = et_name.getText().toString();
                    String pwd = et_pwd.getText().toString();
                    MyBmobUtils.myLogin(LaunchActivity.this, userName, pwd);
                }
            });

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(LaunchActivity.this, RegisterActivity.class);
                    startActivity(i);

                }
            });
        }

        /*ib_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTencent.isSessionValid())
                {
                    mTencent.login(this, Scope, listener);
                }
            }
        });*/
    }

}


