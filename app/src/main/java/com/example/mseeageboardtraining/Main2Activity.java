package com.example.mseeageboardtraining;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

    }


    protected void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                EditText sm_detail = (EditText)findViewById(R.id.sm_detail);
                EditText sm_title = (EditText)findViewById(R.id.sm_title);
                EditText sm_author = (EditText)findViewById(R.id.sm_author);

                String detail = sm_detail.getText().toString();
                String title = sm_title.getText().toString();
                String author = sm_author.getText().toString();

                OkHttpClient Client  = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://172.17.112.45:5000/add_msg?title="+title+"&author="+author+"&detail="+detail)//请求的url
                        .build();
                System.out.println();
                //创建/Call
                Response response = null;
                try {
                    response = Client.newCall(request).execute();
                    //加入队列 异步操作
                    String result = response.body().string();
                    System.out.println(result);
                        System.out.println(result);
                        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                        startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
   }
}

