package com.example.mseeageboardtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class LoginActivity extends AppCompatActivity {

    private static int SPLASH_DISPLAY_LENGHT= 2000;    //延迟2秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.activity_login);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(LoginActivity.this, Login.class);	//第二个参数即为执行完跳转后的Activity
                startActivity(intent);
                LoginActivity.this.finish();   //关闭splashActivity，将其回收，否则按返回键会返回此界面

            }
        }, SPLASH_DISPLAY_LENGHT);


    }
}
