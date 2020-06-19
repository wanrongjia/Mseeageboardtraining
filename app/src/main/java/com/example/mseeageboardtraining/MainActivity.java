package com.example.mseeageboardtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  {

    public JSONObject object;
    public ListView lv;
    public ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    public String api_ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取ip
        getApiIp(getBaseContext());
        //调用获取数据函数
        init();

        //留言按钮点击事件
        Button ly= (Button) findViewById(R.id.ly);
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, Mseeage.class);
                startActivity(intent2);
            }
        });

        //刷新按钮点击事件
        Button sx= (Button) findViewById(R.id.sx);
        sx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        //注销按钮点击事件
        Button zx= (Button) findViewById(R.id.zx);
        zx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAuthor(getBaseContext());
                Intent intent3= new Intent(MainActivity.this,Login.class);
                startActivity(intent3);
            }
        });
    }

    //刷新方法
    private void refresh() {
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void init() {
        lv=(ListView) findViewById(R.id.lv);
        list.clear();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this,detail.class);

                Bundle bundle = new Bundle();
                bundle.putString("title",map.get("title").toString());
                bundle.putString("author",map.get("name").toString());
                bundle.putString("time",map.get("shijian").toString());
                bundle.putString("detail",map.get("detail").toString());

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .url("http://"+api_ip+":5000/get_all_msg").build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                    String date=response.body().string();
                    //把数据传入解析josn数据方法
                    jsonJX(date);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }



    private void jsonJX(String date) {
        //判断数据是空
        if(date!=null){
            try {
                //将字符串转换成jsonObject对象
                JSONArray jsonArray = new JSONArray(date);
//                Gson gson = new Gson();
//                List(jsonBean) jsonlist = gson.fromJson(date,JsonBean.class);

                if (jsonArray.length() > 0) {
                    //遍历
                    for(int i=0;i<jsonArray.length();i++) {
                        object = jsonArray.getJSONObject(i);

                        Map<String, Object> map = new HashMap<String, Object>();

                        try {
                            //获取到json数据中的activity数组里的内容title
                            String title = object.getString("title");
                            //获取到json数据中的activity数组里的内容name
                            String author = object.getString("author");
                            //获取到json数据中的activity数组里的内容startTime
                            String time = object.getString("time");
                            //获取到json数据中的activity数组里的内容detail
                            String detail = object.getString("detail");
                            //存入map
                            map.put("title", title);
                            map.put("name", author);
                            map.put("shijian", time);
                            map.put("detail", detail);
                            //ArrayList集合
                            list.add(map);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
    //Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Mybaseadapter list_item=new Mybaseadapter();
                    lv.setAdapter(list_item);
                break;
            }

        }
    };

    //listview的适配器
    public class Mybaseadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();

        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = getLayoutInflater().inflate(R.layout.listview_item, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);
                viewHolder.shijian = (TextView) convertView.findViewById(R.id.shijian);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.title.setText(list.get(position).get("title").toString());
            viewHolder.textView.setText(list.get(position).get("name").toString());
            viewHolder.shijian.setText(list.get(position).get("shijian").toString());
            return convertView;
        }

    }

    final static class ViewHolder {
        TextView textView;
        TextView shijian;
        TextView title;
    }

    //获取ip地址
    private void getApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        api_ip = login.getString("api_ip", "192.168.43.88");
    }

    //设置注销
    private void setAuthor(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAuthor", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = login.edit();
        edit.putString("author",null);
        edit.apply();

    }

}
