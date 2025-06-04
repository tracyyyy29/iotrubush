package com.example.du.deepPart;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateStemBranchUtils {
    protected static final String[] EARTHLY_BRANCHES = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    /**
     * 时辰数组
     */
    protected static final int[][] HOUR_ARRAY = {{23, 1}, {1, 3}, {3, 5}, {5, 7}, {7, 9}, {9, 11}, {11, 13}, {13, 15}, {15, 17}, {17, 19}, {19, 21}, {21, 23}};

    public static int mins() {
        Date today = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("mm");
        int mm = Integer.parseInt(sf.format(today));
        return mm;
    }
    public static int hous() {
        Date today = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("HH");
        int hh = Integer.parseInt(sf.format(today));
        return hh;
    }

    /**
     * 计算时干支
     *
     * @return 时干支
     */
    public static int calHourStemBranch() {
        int hour = hous();
        int branchIndex = 0;
        // 获取时辰地支 从丑时-亥时，没有筛选到默认为子时
        for (int i = 1; i < HOUR_ARRAY.length; i++) {
            if (hour >= HOUR_ARRAY[i][0] && hour < HOUR_ARRAY[i][1]) {
                branchIndex = i;
            }
        }
        Log.e("test", "DateStemBranchUtils,calHourStemBranch: 35: " + EARTHLY_BRANCHES[branchIndex] + "时");
        return branchIndex;
    }

}
