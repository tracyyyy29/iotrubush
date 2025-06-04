package com.example.du.deepPart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

/**
 * 【日期】：2024-04-27
 * 【编写者】：GXXLeaXX
 * 【主体功能】：这段代码用于计算用户的PSI状态。
 * 【数据结构与变量说明】：
 * - PI：圆周率
 * - P_CYCLE：体力周期
 * - S_CYCLE：情绪周期
 * - I_CYCLE：智力周期
 * - THRESHOLD：用于判断临界点的阈值，一个接近0的小范围来判断临界点。
 * 【输入输出参数说明】：输入参数：用户的出生信息和PSI值； 输出参数：今天的PSI状态
 * 【调用说明】这个类用于计算用户的PSI状态，需要传入数据库用户的出生信息和PSI值，然后计算出今天的PSI状态；结果可以用于更新数据库中的PSI状态，在之后新增提醒界面，提醒用户今天的状态。
 * 【课程目标指向】对齐课程目标中的PSI功能实现，完成计算用户的PSI状态，为用户提供今天的状态提醒。
 * 【组织目标指向】小版本递进中的精进闭环工具对齐(三栏交档留痕&梯子图留痕&board任务工单)&结伴组数据共享协同并进
 * 【组内角色协同】：1.需求员：与深度课设三栏需求中P34页的任务发问对应，实现对于PSI公式的分解计算；2.周志员：总结反思本周心得；3.组长：完成构思，实现用户数据库的建立与更新函数
 * 【注释检讨】：注释清楚地解释了这段代码的功能和工作原理，但是行内注释较多，需要适当减少。
 */
public class PSIcalculate {
    private static final double PI = Math.PI;
    private static final int P_CYCLE = 23;
    private static final int S_CYCLE = 28;
    private static final int I_CYCLE = 33;
    private static final double THRESHOLD = 0.1;

    public void calculatePsiStatusForToday(SQLiteDatabase db, String username) {
        // 从数据库中获取用户的出生信息和PSI值
        Cursor cursor = db.query("users",
                new String[] { "birth_year", "birth_month", "birth_day", "P_value", "S_value", "I_value" },
                "username = ?",
                new String[] { username },
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int birthYear = cursor.getInt(cursor.getColumnIndex("birth_year"));
            int birthMonth = cursor.getInt(cursor.getColumnIndex("birth_month"));
            int birthDay = cursor.getInt(cursor.getColumnIndex("birth_day"));
            double pValue = cursor.getDouble(cursor.getColumnIndex("P_value"));
            double sValue = cursor.getDouble(cursor.getColumnIndex("S_value"));
            double iValue = cursor.getDouble(cursor.getColumnIndex("I_value"));

            // 计算出生天数和今天的相位
            int[] psiStatusToday = calculatePsiStatus(birthYear, birthMonth, birthDay, pValue, sValue, iValue);

            int P_status = psiStatusToday[0];
            int S_status = psiStatusToday[1];
            int I_status = psiStatusToday[2];

            // TODO：获取完PSI状态之后后续对于PSI状态的处理

            cursor.close();
        }
    }

    private int[] calculatePsiStatus(int birthYear, int birthMonth, int birthDay, double pValue, double sValue,
            double iValue) {
        // 设置日历对象为出生日期
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(birthYear, birthMonth - 1, birthDay);

        // 设置当前日期
        Calendar today = Calendar.getInstance();

        // 计算出生到现在的天数
        long daysBetween = (today.getTimeInMillis() - birthDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);

        // 计算PSI相位
        double pPhase = (daysBetween % P_CYCLE) * (2 * PI / P_CYCLE) + (pValue * 2 * PI);
        double sPhase = (daysBetween % S_CYCLE) * (2 * PI / S_CYCLE) + (sValue * 2 * PI);
        double iPhase = (daysBetween % I_CYCLE) * (2 * PI / I_CYCLE) + (iValue * 2 * PI);

        // 计算PSI状态
        int P_status = calculateStatus(pPhase);
        int S_status = calculateStatus(sPhase);
        int I_status = calculateStatus(iPhase);

        // 返回今天的PSI状态
        return new int[] { P_status, S_status, I_status };
    }

    private int calculateStatus(double phase) {
        double sinValue = Math.sin(phase);

        if (Math.abs(sinValue) < THRESHOLD) {
            return 0; // 临界
        } else if (sinValue > 0) {
            return 1; // 高潮
        } else {
            return -1; // 低潮
        }
    }
}
