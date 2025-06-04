package com.example.du.deepPart;

import java.util.ArrayList;
import java.util.List;


public class timeData {
    public static TimeInfo getInfo(int position) {
        return getList().get(position);

    }

    public static List<TimeInfo> getList() {
        List<TimeInfo> list = new ArrayList<TimeInfo>();
        TimeInfo timeInfo1 = new TimeInfo("��ʱ", "��׵Ѩ", "λ�ھ�׵�İ��ݴ�", "��Ħ��׵�����ڻ���ͷʹ����׵����ʧ�ߺ��������ȵ�����");
        list.add(timeInfo1);

        TimeInfo timeInfo2 = new TimeInfo("��ʱ", "̫��Ѩ", "λ���ۿ��·��İ��ݴ�", "��Ħ̫��Ѩ�ɻ���ͷʹ����ƣ�ͺ�ƫͷʹ������");
        list.add(timeInfo2);

        TimeInfo timeInfo3 = new TimeInfo("��ʱ", "����Ѩ", "λ���ֱ�����ⲿ���ݴ�", "��Ħ���ؿ��Ի���羱��ʹ���������������Ƶ�����");
        list.add(timeInfo3);

        TimeInfo timeInfo4 = new TimeInfo("îʱ", "����ȪѨ", "λ��С��ǰ����·����ݴ�", "��Ħ����Ȫ���Ի����Ȳ���ʹ���������ʺ�ʧ�ߵ�����");
        list.add(timeInfo4);

        TimeInfo timeInfo5 = new TimeInfo("��ʱ", "�Ϲ�Ѩ", "λ���ֱ���Ĵָ��ʳָ��ͷ֮��İ��ݴ�", "��Ħ�Ϲȿɻ���ͷʹ���ڸɡ���ƣ�ͺͱ��ص�����");
        list.add(timeInfo5);

        TimeInfo timeInfo6 = new TimeInfo("��ʱ", "̫ϪѨ", "λ�ڽŵװ��ݴ�", "��Ħ̫Ϫ���Ի���ƣ�͡��������ǡ��ٽ�˯�ߺ͵����ڷ��ڵ�����");
        list.add(timeInfo6);

        TimeInfo timeInfo7 = new TimeInfo("��ʱ", "����ȪѨ", "λ��С���ڲ��·����ݴ�", "��Ħ����Ȫ���Ի����¾�������������ʹ��ƣ�͵�����");
        list.add(timeInfo7);

        TimeInfo timeInfo8 = new TimeInfo("δʱ", "�ڹ�Ѩ", "λ���ֱ��ڲ���ؽڴ�", "��Ħ�ڹؿɻ����ļ¡����ġ����Ǻ�ʧ�ߵ�����");
        list.add(timeInfo8);

        TimeInfo timeInfo9 = new TimeInfo("��ʱ", "̫��Ѩ", "λ���㱳��һ����ە��֮��İ��ݴ�", "��Ħ̫��ɻ���ͷʹ����ƣ�ͺ;��ڲ���������");
        list.add(timeInfo9);

        TimeInfo timeInfo10 = new TimeInfo("��ʱ", "���Ѩ", "λ�ھ�����󷽵İ��ݴ�", "��Ħ��ؿ��Ի���ͷʹ��ѣ�κ;�׵��������");
        list.add(timeInfo10);

        TimeInfo timeInfo11 = new TimeInfo("��ʱ", "����Ѩ", "λ��������", "��Ħ�����ɻ���θʹ�����͡��������������������������");
        list.add(timeInfo11);

        TimeInfo timeInfo12 = new TimeInfo("��ʱ", "����Ѩ", "λ�ڸ����������Ϸ����ݴ�", "��Ħ����������ƶ��ġ����ġ������������Ը��ס�����θ�ס�θʹ����������������");
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
