package com.example.du.deepPart;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateStemBranchUtils {
    protected static final String[] EARTHLY_BRANCHES = {"��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��"};
    /**
     * ʱ������
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
     * ����ʱ��֧
     *
     * @return ʱ��֧
     */
    public static int calHourStemBranch() {
        int hour = hous();
        int branchIndex = 0;
        // ��ȡʱ����֧ �ӳ�ʱ-��ʱ��û��ɸѡ��Ĭ��Ϊ��ʱ
        for (int i = 1; i < HOUR_ARRAY.length; i++) {
            if (hour >= HOUR_ARRAY[i][0] && hour < HOUR_ARRAY[i][1]) {
                branchIndex = i;
            }
        }
        Log.e("test", "DateStemBranchUtils,calHourStemBranch: 35: " + EARTHLY_BRANCHES[branchIndex] + "ʱ");
        return branchIndex;
    }

}
