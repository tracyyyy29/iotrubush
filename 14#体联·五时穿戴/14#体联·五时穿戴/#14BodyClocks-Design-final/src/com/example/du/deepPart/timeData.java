package com.example.du.deepPart;

import java.util.ArrayList;
import java.util.List;


public class timeData {
    public static TimeInfo getInfo(int position) {
        return getList().get(position);

    }

    public static List<TimeInfo> getList() {
        List<TimeInfo> list = new ArrayList<TimeInfo>();
        TimeInfo timeInfo1 = new TimeInfo("子时", "大椎穴", "位于颈椎的凹陷处", "按摩大椎可用于缓解头痛、颈椎病、失眠和情绪不稳等问题");
        list.add(timeInfo1);

        TimeInfo timeInfo2 = new TimeInfo("丑时", "太阳穴", "位于眼眶下方的凹陷处", "按摩太阳穴可缓解头痛、眼疲劳和偏头痛等问题");
        list.add(timeInfo2);

        TimeInfo timeInfo3 = new TimeInfo("寅时", "曲池穴", "位于手臂外侧肘部凹陷处", "按摩曲池可以缓解肩颈疼痛、消化不良和胸闷等问题");
        list.add(timeInfo3);

        TimeInfo timeInfo4 = new TimeInfo("卯时", "阳陵泉穴", "位于小腿前外侧下方凹陷处", "按摩阳陵泉可以缓解腿部疼痛、腹部不适和失眠等问题");
        list.add(timeInfo4);

        TimeInfo timeInfo5 = new TimeInfo("辰时", "合谷穴", "位于手背大拇指与食指骨头之间的凹陷处", "按摩合谷可缓解头痛、口干、眼疲劳和便秘等问题");
        list.add(timeInfo5);

        TimeInfo timeInfo6 = new TimeInfo("巳时", "太溪穴", "位于脚底凹陷处", "按摩太溪可以缓解疲劳、消除焦虑、促进睡眠和调节内分泌等问题");
        list.add(timeInfo6);

        TimeInfo timeInfo7 = new TimeInfo("午时", "阴陵泉穴", "位于小腿内侧下方凹陷处", "按摩阴陵泉可以缓解月经不调、腹部胀痛和疲劳等问题");
        list.add(timeInfo7);

        TimeInfo timeInfo8 = new TimeInfo("未时", "内关穴", "位于手臂内侧腕关节处", "按摩内关可缓解心悸、恶心、焦虑和失眠等问题");
        list.add(timeInfo8);

        TimeInfo timeInfo9 = new TimeInfo("申时", "太冲穴", "位于足背第一、二蹠骨之间的凹陷处", "按摩太冲可缓解头痛、眼疲劳和经期不调等问题");
        list.add(timeInfo9);

        TimeInfo timeInfo10 = new TimeInfo("酉时", "风池穴", "位于颈侧外后方的凹陷处", "按摩风池可以缓解头痛、眩晕和颈椎病等问题");
        list.add(timeInfo10);

        TimeInfo timeInfo11 = new TimeInfo("戌时", "气海穴", "位于脐中央", "按摩气海可缓解胃痛、腹胀、消化不良和提高免疫力等问题");
        list.add(timeInfo11);

        TimeInfo timeInfo12 = new TimeInfo("亥时", "中脘穴", "位于腹部正中线上方凹陷处", "按摩中脘可以治疗恶心、烧心、嗳气、治慢性肝炎、慢性胃炎、胃痛，辅助消化等问题");
        list.add(timeInfo12);
        return list;
    }


    static class TimeInfo {
        private String time;
        private String acupoin;
        private String location;
        private String role;

        public TimeInfo(String time, String acupoin, String location, String role) {
            this.time = time;
            this.acupoin = acupoin;
            this.location = location;
            this.role = role;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAcupoin() {
            return acupoin;
        }

        public void setAcupoin(String acupoin) {
            this.acupoin = acupoin;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
