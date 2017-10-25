package com.ren.pullzoomsimple;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ren.pullzoom.widget.PullZoomLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ren on 2017/10/25.
 * TODO
 */

public class PullListViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_listview);
        final PullZoomLayout pullLayout = (PullZoomLayout) findViewById(R.id.pull);
        pullLayout.setOnRefreshListener(new PullZoomLayout.onRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("rq", "刷新回调开始 ");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullLayout.refreshComplete();
                        Toast.makeText(PullListViewActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
        ListView listView = (ListView) findViewById(R.id.listview);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + "条数据");
        }
        final MyAdapter ad = new MyAdapter(list, this);
        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PullListViewActivity.this, ad.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
