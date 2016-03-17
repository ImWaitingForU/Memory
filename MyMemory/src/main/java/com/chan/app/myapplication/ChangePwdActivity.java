package com.chan.app.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePwdActivity extends AppCompatActivity {

    private EditText et_oldPwd;
    private EditText et_newPwd;
    private Button btn_cancel_changePwd;
    private Button btn_changePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        initViews();
    }

    private void initViews() {
        et_oldPwd = (EditText) findViewById(R.id.et_oldPwd);
        et_newPwd = (EditText) findViewById(R.id.et_newPwd);
        btn_changePwd = (Button) findViewById(R.id.btn_changePwd);
        btn_cancel_changePwd = (Button) findViewById(R.id.btn_cancel_changePwd);

        btn_changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPwd = et_oldPwd.getText().toString();
                String newPwd = et_newPwd.getText().toString();

                BmobUser.updateCurrentUserPassword(ChangePwdActivity.this, oldPwd, newPwd, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.i("smile", "密码修改成功，可以用新密码进行登录啦");
                        Toast.makeText(ChangePwdActivity.this, "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                        finish();
                        BmobUser.logOut(ChangePwdActivity.this);
                        BmobUser.getCurrentUser(ChangePwdActivity.this);//清楚本地用户缓存
                        startActivity(new Intent(ChangePwdActivity.this, LaunchActivity.class));
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Log.i("smile", "密码修改失败：" + msg + "(" + code + ")");
                        Toast.makeText(ChangePwdActivity.this, "密码修改失败" + msg + "(" + code + ")", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
    }
}
