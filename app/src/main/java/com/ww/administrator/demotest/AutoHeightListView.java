package com.ww.administrator.demotest;

import android.widget.ListView;

/**解决listview在Scrollview等嵌套滑动的控件中的高度问题
 * Created by Administrator on 2016/4/24.
 */
public class AutoHeightListView extends ListView {
    public AutoHeightListView(android.content.Context context,
                              android.util.AttributeSet attrs) {
        super(context, attrs);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
