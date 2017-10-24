package com.ren.pullzoomsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImageView imageView = (ImageView) findViewById(R.id.image_);
        ListView listView = (ListView) findViewById(R.id.listview);
//        listView.setPullZoomView(imageView, true);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + "条数据");
        }
        final MyAdapter ad = new MyAdapter(list, this);
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ad.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
//        listView.setPullZoomView(linearLayout);
    }
}
