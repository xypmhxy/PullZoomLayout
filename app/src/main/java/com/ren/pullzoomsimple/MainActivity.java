package com.ren.pullzoomsimple;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ren.pullzoom.widget.PullZoomLayout;
import com.ren.pullzoom.widget.PullZoomScrollView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pullList = (Button) findViewById(R.id.pull_list);
        Button pullScroll = (Button) findViewById(R.id.pull_scroll);
        pullList.setOnClickListener(this);
        pullScroll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pull_list) {
            Intent intent = new Intent(this, PullListViewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.pull_scroll) {
            Intent intent = new Intent(this, ScrollViewActivity.class);
            startActivity(intent);
        }
    }
}
