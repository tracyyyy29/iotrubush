package com.example.du.deepPart;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 銆愭棩鏈熴�戯細2024-04-27
 * 銆愮紪鍐欒�呫�戯細GXXLeaXX
 * 銆愪富浣撳姛鑳姐�戯細瀹屾垚鐢ㄦ埛鏁版嵁搴撶殑寤虹珛涓庢洿鏂板嚱鏁帮紝鍖呮嫭鐢ㄦ埛鐨勭敓鏃ャ�丳SI鍊肩殑淇敼銆�
 * 銆愭暟鎹粨鏋勪笌鍙橀噺璇存槑銆戯細
 * - DATABASE_NAME锛氭暟鎹簱鍚嶇О
 * - DATABASE_VERSION锛氭暟鎹簱鐗堟湰
 * 銆愯緭鍏ヨ緭鍑哄弬鏁拌鏄庛�戯細杈撳叆鍙傛暟锛氱敤鎴风殑鐢ㄦ埛鍚嶃�佸瘑鐮併�佺敓鏃ャ�丳SI鍊煎拰鍛ㄦ湡锛涜緭鍑哄弬鏁帮細鏃�
 * 銆愯皟鐢ㄨ鏄庛�戯細鍦˙irthInputActivity涓皟鐢╱pdateBirth()鍑芥暟锛屾洿鏂扮敤鎴风殑鐢熸棩锛涘湪PSIInputUtil涓皟鐢╱pdatePSI()鍑芥暟锛屾洿鏂扮敤鎴风殑PSI鍊笺��
 * 銆愯绋嬬洰鏍囨寚鍚戙�戦�氳繃瀹炵幇鐢ㄦ埛鐨勬暟鎹簱锛屽畬鎴愮敤鎴风殑鐢熸棩鍜孭SI鍊肩殑璁板綍锛屼负鍚庣画鐨凱SI鍊艰绠楁彁渚涘熀纭�銆�
 * 銆愮粍缁囩洰鏍囨寚鍚戙�戝皬鐗堟湰閫掕繘涓殑绮捐繘闂幆宸ュ叿瀵归綈(涓夋爮浜ゆ。鐣欑棔&姊瓙鍥剧暀鐥�&board浠诲姟宸ュ崟)&缁撲即缁勬暟鎹叡浜崗鍚屽苟杩�
 * 銆愮粍鍐呰鑹插崗鍚屻�戯細1.闇�姹傚憳锛氫笌娣卞害璇捐涓夋爮闇�姹備腑P34椤电殑鎿嶄綔鍙戦棶瀵瑰簲锛屽疄鐜伴�氳繃鏁版嵁搴撳浜庣敤鎴疯繘琛岃褰曪紱2.鍛ㄥ織鍛橈細鎬荤粨鍙嶆�濇湰鍛ㄥ績寰楋紱3.缁勯暱锛氬畬鎴愭瀯鎬濓紝瀹炵幇鐢ㄦ埛鏁版嵁搴撶殑寤虹珛涓庢洿鏂板嚱鏁�
 * 銆愭敞閲婃璁ㄣ�戯細娉ㄩ噴娓呮鍦拌В閲婁簡杩欐浠ｇ爜鐨勫姛鑳藉拰宸ヤ綔鍘熺悊锛屼絾鏄鍐呮敞閲婅緝澶氾紝闇�瑕侀�傚綋鍑忓皯銆�
 */

public class UserRecord extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    // 鏋勯�犲嚱鏁帮紝鍒涘缓鎴栨墦寮�鏁版嵁搴�
    public UserRecord(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 褰撴暟鎹簱棣栨鍒涘缓鏃惰皟鐢╫nCreate()
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 鍒涘缓users琛紝鍖呭惈id銆乽sername銆乸assword銆乥irth_year銆乥irth_month銆乥irth_day銆乥irth_hour銆丳_value銆丼_value銆両_value銆丳_period銆丼_period鍜孖_period鍗佷笁涓瓧娈�
        String sql = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "birth_year INTEGER, " +
                "birth_month INTEGER, " +
                "birth_day INTEGER, " +
                "birth_hour INTEGER, " +
                "P_value REAL, " +
                "S_value REAL, " +
                "I_value REAL, " +
                "P_period INTEGER, " +
                "S_period INTEGER, " +
                "I_period INTEGER" +
                ")";
        // 鎵цSQL璇彞
        db.execSQL(sql);
    }

    // 澧炲姞鐢ㄦ埛绫诲瀷
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("birth_year", 0);
        values.put("birth_month", 0);
        values.put("birth_day", 0);
        values.put("birth_hour", 0);
        values.put("P_value", 0);
        values.put("S_value", 0);
        values.put("I_value", 0);
        values.put("P_period", 23); // 鍒濆鍖朠鍛ㄦ湡涓�23澶�
        values.put("S_period", 28); // 鍒濆鍖朣鍛ㄦ湡涓�28澶�
        values.put("I_period", 33); // 鍒濆鍖朓鍛ㄦ湡涓�33澶�

        db.insert("users", null, values);
        db.close();
    }

    // 淇敼鐢ㄦ埛鐢熸棩
    public void updateBirth(String username, int year, int month, int day, int hour) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("birth_year", year);
        values.put("birth_month", month);
        values.put("birth_day", day);
        values.put("birth_hour", hour);

        db.update("users", values, "username = ?", new String[] { username });
        db.close();
    }

    // 淇敼鐢ㄦ埛鍒濆PSI
    public void updatePSI(String username, double P_value, double S_value, double I_value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("P_value", P_value);
        values.put("S_value", S_value);
        values.put("I_value", I_value);

        db.update("users", values, "username = ?", new String[] { username });
        db.close();
    }

    // 褰撴暟鎹簱鐗堟湰鍗囩骇鏃惰皟鐢�
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 鍒犻櫎鏃х殑users琛�
        String sql = "DROP TABLE IF EXISTS users";
        // 鎵цSQL璇彞
        db.execSQL(sql);
        // 鍒涘缓鏂扮殑users琛�
        onCreate(db);
    }
}