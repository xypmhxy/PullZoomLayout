package com.ren.pullzoom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Ren on 2017/10/25.
 * TODO
 */

public class PullZoomScrollView extends PullZoomBaseView {

    private ScrollView scrollView;

    public PullZoomScrollView(Context context) {
        super(context);
    }

    public PullZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        /*获取listview*/
        if (getChildAt(1) instanceof ScrollView) {
            scrollView = (ScrollView) getChildAt(1);
            scrollView.setOnTouchListener(touchListener);
        }
    }

    protected OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    pressY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE :
                    if (scrollView.getScrollY() == 0) {
                        Log.e("rq", "到顶部");
                        canZoom = true;
                        return zoomView(ev);
                    }
                    break;
                case MotionEvent.ACTION_UP :
                    if (canZoom)
                        restroe();
                    if (needRefresh && refreshListener != null) {
                        refreshListener.onRefresh();
                        rotationProgress();
                    } else
                        refreshProgress.setVisibility(GONE);
                    needRefresh = false;
                    canZoom = false;
                    break;
            }
            return false;
        }
    };
}
