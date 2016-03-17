package com.chan.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.Toast;

import com.chan.app.myapplication.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/3/7.
 */
public class MyBmobUtils {

    /**
     * 登陆用户
     */
    public static void myLogin(final Context context, String userName, String pwd) {
        final BmobUser bu2 = new BmobUser();
        bu2.setUsername(userName);
        bu2.setPassword(pwd);
        bu2.login(context, new SaveListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(context, bu2.getUsername() + "登陆成功", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainActivity.class));
                Activity activity = (Activity) context;
                activity.finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 注册用户,若注册成功跳转到下一个Activity
     */
    public static void mySignUp(final Context context, String userName, String pwd, String email) {
        final BmobUser myUser = new BmobUser();
        myUser.setUsername(userName);
        myUser.setPassword(pwd);
        myUser.setEmail(email);
        myUser.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "注册成功:" + myUser.getUsername() + "-" + myUser.getObjectId() + "-" + myUser
                        .getCreatedAt() + "-" + myUser.getSessionToken() + ",是否验证：" + myUser.getEmailVerified(),
                        Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainActivity.class));
                Activity activity = (Activity) context;
                activity.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context, "注册失败:" + s.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    *   将String中的图片解析出来，再返回数据
    */
    public static SpannableString StringToBitMap(Context context, String msg) {
        SpannableString ss = new SpannableString(msg);
        Pattern p = Pattern.compile("/mnt/sdcard/.+?\\.\\w{3}");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            Bitmap bitmap = BitmapFactory.decodeFile(m.group());
            ImageSpan imageSpan = new ImageSpan(context, bitmap);
            ss.setSpan(imageSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

}
