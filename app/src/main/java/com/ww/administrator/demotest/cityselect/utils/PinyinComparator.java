package com.ww.administrator.demotest.cityselect.utils;

import com.ww.administrator.demotest.cityselect.bean.SortModel;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/7/27.
 */
public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
