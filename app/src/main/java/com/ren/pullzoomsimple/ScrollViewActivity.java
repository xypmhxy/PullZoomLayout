package com.ren.pullzoomsimple;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ren.pullzoom.widget.PullZoomLayout;
import com.ren.pullzoom.widget.PullZoomScrollView;

/**
 * Created by Ren on 2017/10/25.
 * TODO
 */

public class ScrollViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_scroll);
        final PullZoomScrollView pullZoomScrollView = (PullZoomScrollView) findViewById(R.id.pull);
        pullZoomScrollView.setOnRefreshListener(new PullZoomLayout.onRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("rq", "刷新回调开始 ");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullZoomScrollView.refreshComplete();
                        Toast.makeText(ScrollViewActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
    }
}
