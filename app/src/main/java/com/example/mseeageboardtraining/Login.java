package com.example.mseeageboardtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    public Button Editip;
    public Button loginButton;
    public EditText api_ip;
    public String ip;
    public EditText username;
    public EditText password;
    public String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        getApiIp(getBaseContext());
        getAuthor(getBaseContext());


        Editip = (Button)findViewById(R.id.Editip);
        Editip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setApiIp(getBaseContext());
            }
        });

        loginButton = (Button) findViewById(R.id.logbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                System.out.println(ip);
            }
        });

    }

     private void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                String user = username.getText().toString();
                String pass = password.getText().toString();
                OkHttpClient Client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://"+ip+":5000/login?username="+user+"&password="+pass)
                        .build();

                Response response = null;
                try {
                    response = Client.newCall(request).execute();
                    String result = response.body().string();
                    System.out.println(result);
                    if (result.equals("{\"login\": false, \"error_code\": 0, \"msg\": \"success\"}")){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                        setAuthor(getBaseContext());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this,"登录成功",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        Toast.makeText(Login.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }


    private void getApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        ip = login.getString("api_ip", "192.168.43.88");
    }

    //得到用户名
    private void getAuthor(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAuthor", Context.MODE_PRIVATE);
        author = login.getString("author", null);
        if (author != null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }
    }


    private void setApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = login.edit();
        api_ip = (EditText)findViewById(R.id.api_ip);
        edit.putString("api_ip",api_ip.getText().toString());
        edit.apply();
        Toast.makeText(Login.this, "设置成功", Toast.LENGTH_SHORT).show();

    }

    private void setAuthor(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAuthor", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = login.edit();
        edit.putString("author",username.getText().toString());
        edit.apply();

    }
}
