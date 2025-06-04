package com.example.du.deepPart;

import java.util.ArrayList;
import java.util.List;


public class jlData {
    public static TimeInfo getInfo(int position) {
        return getList().get(position);

    }

    public static List<TimeInfo> getList() {
        List<TimeInfo> list = new ArrayList<TimeInfo>();
        TimeInfo timeInfo1 = new TimeInfo("子时", "胆经", "", "胆足少阳之脉，起于目锐眦，上抵头角，下耳后，循颈，行手少阳之前，至肩上，却交出手少阳之后，入缺盆；其支者，从耳后入耳中，出走耳前，至目锐眦后；其支者，别锐眦，下大迎，合于手少阳，抵于（出页），下加颊车，下颈合缺盆，以下胸中，贯隔，络肝属胆，循胁里，出气街，绕毛际，横入髀厌中；其直者，从缺盆下腋，循胸，过季胁，下含髀厌中，以下循髀阳，出膝外廉，下外辅骨之前，直下抵绝骨之端，下出外踝之前，循足跗上，入小趾次趾之间；其支者，别跗上，入大趾之间，循大趾歧骨内出其端，还贯爪甲，出三毛");
        list.add(timeInfo1);

        TimeInfo timeInfo2 = new TimeInfo("丑时", "肝经", "", "肝足厥阴之脉，起于大趾丛毛之际，上循足跗上廉，去内踝一寸，上髁八寸，交出太阴之后，上腘内廉，循股阴人毛中，过阴器，抵小腹，挟胃，属肝络胆，上贯隔，布胁肋，循喉咙之后，上入颃颡，连目系，上出额，与督脉会于巅；其支者，从目系下颊里，环唇内；其支者，复从肝别贯隔，上注肺");
        list.add(timeInfo2);

        TimeInfo timeInfo3 = new TimeInfo("寅时", "肺经", "", "肺手太阴之脉，起于中焦，下络大肠，还循胃口，上隔属肺，从肺系横出腋下，下循臑内，行少阴心主之前，下肘中，循臂内上骨下廉，入寸口。上鱼，循鱼际，出大指之端；其支者从腕后直出次指内廉，出其端");
        list.add(timeInfo3);

        TimeInfo timeInfo4 = new TimeInfo("卯时", "大肠经", "", "大肠手阳明之脉，起于大指次指之端，循指上廉，出合谷两骨之间，上入两筋之中，循臂上廉，入肘外廉，上臑外前廉，上肩，出髃骨之前廉，上出于柱骨之会上，下入缺盆络肺，下膈属大肠；其支者，从缺盆上颈，贯颊，入下齿中，还出挟口，交人中，左之右，右之左，上挟鼻孔");
        list.add(timeInfo4);

        TimeInfo timeInfo5 = new TimeInfo("辰时", "胃经", "", "胃足阳明之脉，起于鼻之交頞中，旁纳太阳之脉，下循鼻外，入上齿中，还出抉口，环唇，下交承浆，却循颐后下廉，出大迎，循颊车，上耳前，过客主人，循发际，至额颅；其支者，从大迎前下人迎，循喉咙，入缺盆，下膈，属胃络脾；其直者，从缺盆下乳内廉，下挟脐，入气街中；其支者，起于胃口，下循腹里，下至气街中而合，以下辟关，抵伏兔，下膝膑中，下循胫外廉，下足跗，入中趾内间；其支者，下廉三寸而别，下入中指外间；其支者，别附上，入大指间，出其端");
        list.add(timeInfo5);

        TimeInfo timeInfo6 = new TimeInfo("巳时", "脾经", "", "脾足太阴之脉，起于大趾之端，循趾内侧白肉际，过核骨后，上内踝前廉，上腨内，循胫骨后，交出厥阴之前，上膝股内前廉，入腹，属脾络胃，上隔，挟咽，连舌本，散舌下；其支者，复从胃别上隔，注心中");
        list.add(timeInfo6);

        TimeInfo timeInfo7 = new TimeInfo("午时", "心经", "", "心手少阴之脉，起于心中，出属心系，下隔络小肠；其支者，从心系上挟咽，系目系；其直者，复从心系却上肺，下出腋下，下循臑内后廉，行手太阴心主之后，下肘内，循臂内后廉，抵掌后锐骨之端，入掌内后廉，循小指之内，出其端");
        list.add(timeInfo7);

        TimeInfo timeInfo8 = new TimeInfo("未时", "小肠经", "", "小肠手太阳之脉，起于小指之端。循手外侧上腕，出踝中。直上循臂骨下廉，出肘内侧两骨之间，上臂臑后廉，出肩解，绕肩胛，交肩上，入缺盆，络心，循咽下隔，抵胃，属小肠；其支者，从缺盆循颈，上颊，至目锐眦，却入耳中；其支者，别颊上（出页），抵鼻，至目内眦，斜络于颧");
        list.add(timeInfo8);

        TimeInfo timeInfo9 = new TimeInfo("申时", "膀胱经", "", "膀胱足太阳之脉，起于目内眦，上额，交巅；其支者，从巅至耳上角；其直者，从巅入络脑，还出别下项，循肩膊内，挟脊抵腰中，入循膂，络肾属膀胱；其支者，从腰中下挟脊、贯臀入腘中；其支者，从膊内左右别下贯胛，挟脊内，过髀枢，循髀外，从后廉下合腘中，以下贯腨内，出外踝之后，循京骨，至小趾外侧");
        list.add(timeInfo9);

        TimeInfo timeInfo10 = new TimeInfo("酉时", "肾经", "", "肾足少阴之脉，起于小趾之下，邪（斜）走足心，出于然谷之下。循内踝之后，别入跟中，以上腨内，出腘内廉，上股内后廉。贯脊，属肾络膀胱；其直者，从肾上贯肝膈，入肺中，循喉咙，挟舌本；其支者，从肺出络心，注胸中");
        list.add(timeInfo10);

        TimeInfo timeInfo11 = new TimeInfo("戌时", "心包经", "", "心主手厥阴心包之脉，起于胸中，出属心包络，下隔，历络三焦；其支者，循胸从胁，下腋三寸，上抵腋，下循臑内，行太阴少阴之间，入肘中，下臂行两筋之间，入掌中，循中指，出其端；其支者，别掌中，循小指次指出其端");
        list.add(timeInfo11);

        TimeInfo timeInfo12 = new TimeInfo("亥时", "三焦经", "", "三焦手少阳之脉，起于小指次指之端，上出两指之间，循手表腕，出臂外两骨之间，上贯肘，循臑外上肩，而交出足少阳之后。入缺盆，布膻中，散络心包，下隔循属三焦；其支者，从膻中上出缺盆，上项，系耳后直上，出耳上角，以屈下颊至（出页）；其支者，从耳后至耳中，出走耳前，过客主人前，交颊，至目锐眦");
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
