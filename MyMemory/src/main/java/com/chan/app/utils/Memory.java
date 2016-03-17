package com.chan.app.utils;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/3/8.
 * 用户要保存的信息类，对应Memory表
 */
public class Memory extends BmobObject {
    private String allMsg; //所有信息

    public String getAllMsg() {
        return allMsg;
    }

    public void setAllMsg(String allMsg) {
        this.allMsg = allMsg;
    }
}
