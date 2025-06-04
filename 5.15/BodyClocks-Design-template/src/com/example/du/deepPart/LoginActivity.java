package com.example.du.deepPart;

// 导入需要的类
import android.content.Context;
import android.content.SharedPreferences;

import com.example.du15.R;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

/**
 * 【日期】：2024-04-27
 * 【编写者】：GXXLeaXX
 * 【主体功能】：这段代码用于对于用户数据库进行检索并处理用户的登录操作。
 * 
 * 数据结构与变量说明：
 * - usernameEditText：一个EditText，用于输入用户名
 * - passwordEditText：一个EditText，用于输入密码
 * - loginButton：一个Button，用于提交登录信息
 * 
 * 输入输出参数说明：
 * - 输入参数：用户输入的用户名和密码
 * 
 * 调用或使用的说明：
 * 这个Activity使用了Android的SharedPreferences来保存用户的登录状态和信息，使用了SQLite数据库来验证用户名和密码，以及注册新用户。
 * 在浅度课设com.example.du.curriculumPart。MainActivity的gotodeepDesign()中对于该Activity进行了调用，在结束点名后进行用户登陆。
 * 
 * 目标关联部分：
 * - 本段代码对齐本组v2.x的课设目标乘法结果——实现PSI功能，其中用户的数据库建立是最为基础的功能。
 * 
 * 组内角色协同部分：
 * - 与深度课设三栏需求中P34页的操作发问对应，实现通过数据库对于用户进行记录
 * - 由组内周志员和组长共同完成
 * 
 * 注释检讨：这段注释清楚地解释了这段代码的功能和工作原理，注释的比例适中，没有过多或过少的注释，但是行间注释较多，需要适当减少。
 */
public class LoginActivity extends Activity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件
        setContentView(R.layout.activity_login);

        // 获取用户名、密码输入框、登陆按钮
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);

        // 设置登录按钮的点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的用户名和密码
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // 验证用户名和密码
                if (validate(username, password)) {
                    // 保存用户的登录状态和信息
                    /**
                     * 在Android应用中，一种常见的方法是使用SharedPreferences来保存当前登录的用户的用户名。当用户登录成功后，你可以将用户名保存到SharedPreferences中。然后，在需要获取当前登录的用户的用户名时，你可以从SharedPreferences中读取用户名。
                     */
                    SharedPreferences sharedPref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isLoggedIn", true); // 用户已登录
                    editor.putString("username", username); // 保存用户名
                    editor.apply(); // 提交更改

                    // 跳转到主界面MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 结束当前Activity
                } else {
                    // 显示错误信息
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate(String username, String password) {
    	UserRecord dbHelper = new UserRecord(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 先只根据用户名查询用户
        String sql = "SELECT * FROM users WHERE username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });

        if (cursor.getCount() > 0) {
            // 用户名存在，检查密码
            cursor.moveToFirst();
            String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
            if (password.equals(storedPassword)) {
                // 密码正确
                cursor.close();
                db.close();
                return true;
            } else {
                // 密码错误，显示对话框
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("密码错误")
                        .setPositiveButton("确定", null)
                        .show();
            }
        } else {
            // 用户名不存在，添加新用户
            dbHelper.addUser(username, password);

            // 显示对话框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("未查询到您的信息，已为您自动注册")
                    .setPositiveButton("确定", null)
                    .show();
        }

        cursor.close();
        db.close();

        return false;
    }
}