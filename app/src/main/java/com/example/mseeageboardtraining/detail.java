package com.example.mseeageboardtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class detail extends AppCompatActivity {

    private TextView detail;
    private TextView title;
    private TextView author;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detail = (TextView)findViewById(R.id.msgdetail);
        time = (TextView)findViewById(R.id.msgtime);
        title = (TextView)findViewById(R.id.msgauthor);
        author = (TextView)findViewById(R.id.msgtitle);


        Bundle bundle = this.getIntent().getExtras();

        detail.setText(bundle.getString("detail"));
        author.setText(bundle.getString("author"));
        time.setText(bundle.getString("time"));
        title.setText(bundle.getString("title"));


    }
}
