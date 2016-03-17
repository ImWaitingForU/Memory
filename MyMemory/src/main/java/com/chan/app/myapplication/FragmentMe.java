package com.chan.app.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class FragmentMe extends Fragment {

    private Button btn_quit;
    private Button btn_changePwd;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_me, container, false);
        initViews();
        return v;
    }

    private void initViews() {
        btn_quit = (Button) v.findViewById(R.id.btn_quit);
        //退出登录
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut(getActivity());   //清除缓存用户对象
                startActivity(new Intent(getActivity(), LaunchActivity.class));
            }
        });

        btn_changePwd = (Button) v.findViewById(R.id.btn_changePwd);
        btn_changePwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(),ChangePwdActivity.class));

            }
        });
    }

}
