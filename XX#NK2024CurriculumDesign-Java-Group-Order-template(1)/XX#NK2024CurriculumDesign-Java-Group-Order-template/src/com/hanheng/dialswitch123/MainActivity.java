/* *******************************************************
 * XX#NK2024CurriculumDesign-Java-Group-Order-template.c *
 * *******************************************************
 * 代码部分整体操作说明：									 *
 * 1.代码构成：											 *
 * 	由“认知发问”、“总结发问”、“编程主体”三部分三栏化构成。	 *
 * 														 *
 * 2.认知发问部分：										 *
 *  四元组为“问题定位”、“问题描述”、“解决状态”、“解决策略”。*
 *  -问题定位：											 *
 *    参见裸C代码部分给出的示例，在对应发问的编程主题部分进   *
 *  行标记，标签统一为Q1、Q2、……，发问人需要对注明发问人的     *
 *  角色身份（如NK2024-01#-Lea），具体位置的定位以注释的形    *
 *  式在代码中给出从而实现定位功能。					     *
 *  -问题描述：											 *
 *    参见示例，阐释自己的问题，要求表述清晰，问题聚焦。          *
 *  -解决状态：											 *
 *    参见示例，分为未解决/已解决两部分。其中已解决应标明解   *
 *  决人的角色身份（如NK2024-01#-Req）。                                                      *
 *  -解决策略：											 *
 *	     参见示例，未解决状态下填“无”，已解决状态应清晰表述解     *
 *  决策略。根据实际需求，完善四元组，并以四元组为单位进行      *
 *  扩展填充。                                                                                                                   *
 *                                                       *
 * 3.总结展示部分：                                                                                                       *
 *    参见示例对实现代码后的心得体会等进行总结展示交流，需     *
 *  要明确总结人的角色身份。                                                                                *
 *                                                       *
 * 4.编程主体部分：                                                                                                      *
 *    由代码和注释说明两部分构成。                                                                *
 * 	-代码：                                                                                                                         *
 *    大部分实现已经给出，需要深入学习理解。按照需求文档，    *
 *  对需要个性化定制的部分进行修改并增加相应的注释说明。          *
 * 	-注释说明：                                                                                                                *
 * 	     按照已有的注释说明的格式，进行注释操作，注意要保证同     *
 *  样的缩进等格式要求确保可读性。注释的操作包括：注释填充、*
 *  注释替换、注释增删，在进行操作后，同样需要进行角色身份     *
 *  的注明（##表示组号）：                                                                                     *
 *    -注释填充：NK2024F-XX#-Lea/Req/Log/Opt/Sho                 *
 *    -注释替换：NK2024R-XX#-Lea/Req/Log/Opt/Sho                 *
 *    -注释增删：NK2024I&D-XX#-Lea/Req/Log/Opt/Sho               *
 * ******************************************************/

/* ********************认  知  发  问  部  分********************
 *
 *
 *【问题定位（示例）】Q0(NK2024-XX#-Lea)
 *【问题表述（示例）】updateText内部如何对数码管进行显示特定字符操作的？
 *【解决状态（示例）】已解决(NK2024-XX#-Log)
 *【解决策略（示例）】在updataText方法内部，通过匿名内部类的方式继承Thread类并对其中的run()方法进行重载后调用start()开启新的线程来实现的。
 *					其中重载的部分是调用了封装好的com/hanheng/a53/seg7/Seg7Class.java的Seg7Class类的Seg7Show方法实现的，而Seg7Show的实现
 *					是通过调用C语言编译好的seg7-jni.so库来直接对硬件进行操作的，具体的位置在/libs目录下。
 *
 *【问题定位】Q1(..)
 *【问题表述】
 *【解决状态】
 *【解决策略】
 *
 *【问题定位】Q2(...)
 *【问题表述】
 *【解决状态】
 *【解决策略】
 *
 * ********************认  知  发  问  部  分********************/

/* ********************总  结  展  示  部  分********************
 *【总结展示1】NK2024-01#-Log
 *  通过java的代码实现，进一步熟悉了实验箱的硬件环境，掌握了通过拨码开关的方式实现对点阵、数码管等的控制
 *
 *【总结展示2】
 *
 *
 * ********************总  结  展  示  部  分********************/

/* ********************编  程  主  体  部  分********************/
package com.hanheng.dialswitch123;

import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.beep.BeepClass;
import com.hanheng.a53.dotarray.DotArrayClass;
import com.hanheng.a53.dotarray.FontClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.seg7.Seg7Class;
import com.hanheng.dialswitch123.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
	private TextView[] text = new TextView[8];
	private ToggleButton[] tb = new ToggleButton[8];
	private Button btnButton;
	private ImageView image;
	private TextView[] textOrder = new TextView[5];
	private boolean flag;
	private boolean isFZero; 																			// 记录初始拨码开关是否正确归零
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		this.btnButton.setOnClickListener(this);
		for(int i = 0 ; i < tb.length ; i++){
			this.tb[i].setOnClickListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {														// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
																										//	初始化组件
	public void initView(){
		tb[0] = (ToggleButton)findViewById(R.id.toggleButton1);
		tb[1] = (ToggleButton)findViewById(R.id.toggleButton2);
		tb[2] = (ToggleButton)findViewById(R.id.toggleButton3);
		tb[3] = (ToggleButton)findViewById(R.id.toggleButton4);
		tb[4] = (ToggleButton)findViewById(R.id.toggleButton5);
		tb[5] = (ToggleButton)findViewById(R.id.toggleButton6);
		tb[6] = (ToggleButton)findViewById(R.id.toggleButton7);
		tb[7] = (ToggleButton)findViewById(R.id.toggleButton8);	
		text[0] = (TextView)findViewById(R.id.textView1);
		text[1] = (TextView)findViewById(R.id.textView2);
		text[2] = (TextView)findViewById(R.id.textView3);
		text[3] = (TextView)findViewById(R.id.textView4);
		text[4] = (TextView)findViewById(R.id.textView5);
		text[5] = (TextView)findViewById(R.id.textView6);
		text[6] = (TextView)findViewById(R.id.textView7);
		text[7] = (TextView)findViewById(R.id.textView8);
		btnButton=(Button)findViewById(R.id.button1);
		image=(ImageView)findViewById(R.id.imageView1);
		
		textOrder[0] = (TextView)findViewById(R.id.textView11);
		textOrder[1] = (TextView)findViewById(R.id.textView12);
		textOrder[2] = (TextView)findViewById(R.id.textView13);
		textOrder[3] = (TextView)findViewById(R.id.textView14);
		textOrder[4] = (TextView)findViewById(R.id.textView15);

		Log.i("初始化", "消息："+DipClass.Init());
		BeepClass.Init();
		isFZero = true;
		dipInit();
		LedClass.Init();
		int err = Seg7Class.Init();
		FontClass.getInstance();
		
		patternSwitch(0);																				//stage 0：启动阶段
		openThread();																					//四个LED灯代表的四位bit显示为0001B，代表进入启动阶段
	}
	
	public void dipInit(){																				//判断拨码开关初始位置是否正确
		int BEEP_ON = 0;
		if( DipClass.ReadValue() != 0 ){
			isFZero = false;
			BeepClass.IoctlRelay(BEEP_ON); 																//若不正确，beep声音提示同时触摸屏显示提示信息，等待调整正确
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle("警告！")
			.setMessage("拨码开关初始未置0，请检查调整确认无误后点击确定")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if( DipClass.ReadValue() != 0 ){
						dipInit();
					}
					isFZero = true;
					int BEEP_OFF = 1;
					BeepClass.IoctlRelay(BEEP_OFF);  													//拨码开关位置调整正确后关闭蜂鸣器
				}
			});
			dialog.create();
			dialog.show();
		}
	}

	@Override
	public void onClick(View arg0) {																	//通过屏幕上开关控制点名
		// TODO Auto-generated method stub
		int key=arg0.getId();
		switch (key) {
		case R.id.button1:
			exit();
			break;
		case R.id.toggleButton1:
			if(tb[0].isChecked()){																		
				patternSwitch(1);																		//stage 1：点名阶段·小组摘要
				show_group_number_open();																//四个LED灯代表的四位bit显示为0010B，代表进入点名阶段的小组摘要部分
			}else{
				show_group_number_close();
			}
			break;
		case R.id.toggleButton2:
			if(tb[1].isChecked()){
				show_first_name_lea_open();
			}else{
				show_first_name_lea_close();
			}
			break;
		case R.id.toggleButton3:
			if(tb[2].isChecked()){
				patternSwitch(2);																		//stage 1：点名阶段·组员点名
				show_first_name_log_open();																//四个LED灯代表的四位bit显示为0100B，代表进入点名阶段的组员点名部分
			}else{
				show_first_name_log_close();
			}
			break;
		case R.id.toggleButton4:
			if(tb[3].isChecked()){
				show_first_name_Reg_open();
			}else{
				show_first_name_Reg_close();
			}
			break;
		case R.id.toggleButton5:
			if(tb[4].isChecked()){
				show_first_name_opt_open();
			}else{
				show_first_name_opt_close();
			}
			break;
		case R.id.toggleButton6:
			if(tb[5].isChecked()){
				show_first_name_sho_open();
			}else{
				show_first_name_sho_close();
			}
			break;
		case R.id.toggleButton7:
			if(tb[6].isChecked()){
				patternSwitch(3);																		//stage 2：深度课设阶段
																										//结束点名阶段，四个LED灯代表的四位bit显示为1000B，代表进入深度课设阶段
			}else{

			}
			break;
		case R.id.toggleButton8:
			break;
		default:
			break;
		}

	}
	
	private int originalVal = 0;																		//拨码开关状态显示
	public void computed(int val){
		int diff = val - originalVal;																	// 计算差值
		if (diff < 0) {
			diff = -diff;
			switch(diff) {			
			case(1):changeState(0, 0);break;
			case(2):changeState(1, 0);break;
			case(4):changeState(2, 0);break;
			case(8):changeState(3, 0);break;
			case(16):changeState(4, 0);break;
			case(32):changeState(5, 0);break;
			case(64):changeState(6, 0);break;
			case(128):changeState(7, 0);break;
			}
		}
		else {
			switch(diff) {
			case(1):changeState(0, 1);break;
			case(2):changeState(1, 1);break;
			case(4):changeState(2, 1);break;
			case(8):changeState(3, 1);break;
			case(16):changeState(4, 1);break;
			case(32):changeState(5, 1);break;
			case(64):changeState(6, 1);break;
			case(128):changeState(7, 1);break;
			}
		}			
		
		this.originalVal = val;
	}
	
	public String addZero(int b){																		//字符串补零
		String val = Integer.toBinaryString(b&0xFF);
		String str="";
		if(val.length()<8){
			for(int i=0;i<8-val.length();i++){
				str+=0;
			}			
			return str+=val;
		}
		return val;
	}
	
	public void ledAllOff(){																			//LED灯全关
		LedClass.IoctlLed(0, 0);
		LedClass.IoctlLed(1, 0);
		LedClass.IoctlLed(2, 0);
		LedClass.IoctlLed(3, 0);
	}
	
	public void patternSwitch(int pattern){																//阶段切换
		ledAllOff();
		switch (pattern) {
		case 0:																							//stage0：启动阶段 0001B
			LedClass.IoctlLed(3, 1);
			break;
        case 1:																							//stage1：点名阶段·小组摘要 0010B
        	LedClass.IoctlLed(2, 1);
			break;
        case 2:        																					//stage1：点名阶段·组员点名 0100B
        	LedClass.IoctlLed(1, 1);
	        break;
        case 3:																							//浅度课设阶段结束
        	endGroupOrder();
        	LedClass.IoctlLed(0, 1);        															//stage2：深度课设阶段 1000B
	        break;
		default:
			break;
		}
	}
	
	public void endGroupOrder(){																		//浅度课设阶段结束操作：蜂鸣器鸣响+动画（主要点名组件淡出界面，点名标签保留）
		beepControl(3);  																				//三声短促的蜂鸣器鸣响“滴滴滴”，表示所有点名结束
		AnimationSet as = new AnimationSet(true);
		as.setDuration(1000);
		AlphaAnimation aa = new AlphaAnimation(1,0);
		aa.setDuration(1000);
		as.addAnimation(aa);
		TranslateAnimation ta = new TranslateAnimation(0,0,0,100);
		ta.setDuration(1000);
		as.addAnimation(ta);
		for(int i = 0 ; i < tb.length ; i++){
			tb[i].startAnimation(as);
			text[i].startAnimation(as);
		}
		btnButton.startAnimation(as);
		image.startAnimation(as);
		for(int i = 0 ; i < tb.length ; i++){
			tb[i].setVisibility(tb[i].GONE);
			text[i].setVisibility(text[i].GONE);
		}
		
		btnButton.setVisibility(btnButton.GONE);
		
		image.setVisibility(image.GONE);
	}
	
	public void beepControl(int number){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		try {
			for(int i = 0 ; i < number ; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(200);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(100);
			}
			} catch (InterruptedException e) { }
	}
	
	public void exit(){		
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("程序退出")
		.setMessage("您确定要退出吗？")
		.setIcon(R.drawable.ic_launcher);
		dialog.setCancelable(false);
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				flag=false;
				DipClass.Exit();
				BeepClass.Exit();
				DotArrayClass.Exit();
				LedClass.Exit();
				Seg7Class.Exit();
				MainActivity.this.finish(); 															//操作结束
			}
		});
		
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		dialog.show();
	}

	public void updateText(final float arg){															//数码管显示
	    new Thread(new Runnable() {																		//Q0
		public void run() {
				Seg7Class.Seg7Show(arg);
			}
		}).start();
	}
	
	//Author：XXX
	//Date：2024/XX/XX
	//Description：显示组号信息。组号"XX"的汉字为"X"，转换为4位四进制数为 XXXXQ
	public void show_group_number_open()
	{
																										//模板以数字"0"为例，"0"的汉字为"零"，转换为4位四进制数为 0000Q。
																										//请按照本组真实组号对本函数进行修改
		/********请在下方区域修改代码及注释，实现利用点阵和数码管显示本组组号相关信息的功能********/
		
		updateText((float) 0); 																			//数码管显示4位四进制数
		
		
		String str = "零";  
		byte[][] data = FontClass.getInstance().setContent(str,this.getAssets()); 						//点阵显示汉字"零"
		
		/********请在上方区域修改代码及注释，实现利用点阵和数码管显示本组组号相关信息的功能********/
	}
	
	public void show_group_number_close()																//关闭显示组号信息的数码管
	{
		updateText(0);
	}
	
	//Author：XXX
	//Date：2024/XX/XX
	//Description：显示组长姓氏信息。组长姓氏的汉字为"X"，该姓氏的拼音首字母小写为"X"，其ASCII码为 XXXX XXXXB，转换成4位四进制数为 XXXXQ
	public void show_first_name_lea_open()
	{
																										//模板以"长"为例，"长"的拼音首字母小写为"z"，其ASCII码为 0111 1010B，转换成4位四进制数为1322Q
																										//请按照本组组长真实姓氏对本函数进行修改
		/********请在下方区域修改代码及注释，实现利用点阵和数码管显示组长姓氏相关信息的功能********/
		
		updateText((float) 1322); 																		//数码管显示4位四进制数
		
																										
		textOrder[0].setBackgroundColor(Color.rgb(0, 255, 0));											//标签变为绿色，显示"组长已点名"
		textOrder[0].setText("组长已点名"); 																					
		String str = "长";																				//点阵显示汉字"长"
		byte[][] data = FontClass.getInstance().setContent(str,this.getAssets());
		beepControl(2);   																				//点名正确结束后，蜂鸣器响两声
		
		/********请在上方区域修改代码及注释，实现利用点阵和数码管显示组长姓氏相关信息的功能********/
	}
	
	public void show_first_name_lea_close()																//关闭显示组长姓氏信息的数码管
	{
		updateText(0);
	}
	
	//Author：XXX
	//Date：2024/XX/XX
	//Description：显示周志员姓氏信息
	//周志员姓氏的汉字为"X"，该姓氏的拼音首字母小写为"X"，其ASCII码为 XXXX XXXXB，转换成4位四进制数为 XXXXQ
	public void show_first_name_log_open()
	{
		/********请在下方区域编写代码及注释，实现利用点阵和数码管显示周志员姓氏相关信息的功能********/
		
		
		
		
		
		
		
		
		
		
		
		
		/********请在上方区域编写代码及注释，实现利用点阵和数码管显示周志员姓氏相关信息的功能********/
	}
	
	
	public void show_first_name_log_close()																//关闭显示周志员姓氏信息的数码管
	{
		updateText(0);
	}
	
	//Author：XXX
	//Date：2024/XX/XX
	//Description：显示需求员姓氏信息
	//需求员姓氏的汉字为"X"，该姓氏的拼音首字母小写为"X"，其ASCII码为 XXXX XXXXB，转换成4位四进制数为 XXXXQ
	public void show_first_name_Reg_open()
	{
		/********请在下方区域编写代码及注释，实现利用点阵和数码管显示需求员姓氏相关信息的功能********/
		
		
		
		
		
		
		
		
		
		
		
		
		/********请在上方区域编写代码及注释，实现利用点阵和数码管显示需求员姓氏相关信息的功能********/
	}
	
	
	public void show_first_name_Reg_close()																//关闭显示需求员姓氏信息的数码管
	{
		updateText(0);
	}
	
	//Author：XXX
	//Date：2024/XX/XX
	//Description：显示优裁员姓氏信息
	//优裁员姓氏的汉字为"X"，该姓氏的拼音首字母小写为"X"，其ASCII码为 XXXX XXXXB，转换成4位四进制数为 XXXXQ
	public void show_first_name_opt_open()
	{
		/********请在下方区域编写代码及注释，实现利用点阵和数码管显示优裁员姓氏相关信息的功能********/
		
		
		
		
		
		
		
		
		
		
		
		
		/********请在上方区域编写代码及注释，实现利用点阵和数码管显示优裁员姓氏相关信息的功能********/
	}
	
	
	public void show_first_name_opt_close()																//关闭显示优裁员姓氏信息的数码管
	{
		updateText(0);
	}
	
	//Author：XXX
	//Date：2024/XX/XX
	//Description：显示展示员姓氏信息
	//展示员姓氏的汉字为"X"，该姓氏的拼音首字母小写为"X"，其ASCII码为 XXXX XXXXB，转换成4位四进制数为 XXXXQ
	public void show_first_name_sho_open()
	{
		/********请在下方区域编写代码及注释，实现利用点阵和数码管显示展示员姓氏相关信息的功能********/
		
		
		
		
		
		
		
		
		
		
		
		
		/********请在上方区域编写代码及注释，实现利用点阵和数码管显示展示员姓氏相关信息的功能********/
	}
	
	public void show_first_name_sho_close()																//关闭显示展示员姓氏信息的数码管
	{
		updateText(0);
	}

	public void changeState(int i,int tag){																//获取到拨码开关：1-显示组号信息;2-显示组长姓氏信息；3-显示周志员姓氏信息；4-显示需求员姓氏信息；5-显示展示员姓氏信息
		if(!isFZero){
			return;
		}
		if(tag==1){
			switch (i) {
			case 0:
																										//四个LED灯代表的四位bit显示为0010B，代表进入点名阶段的小组摘要部分
				patternSwitch(1);																		//stage 1：点名阶段·小组摘要
				tb[0].setChecked(true);
				show_group_number_open();
				break;
			case 1:
				tb[1].setChecked(true);
				show_first_name_lea_open();
				break;
			case 2:
																										//四个LED灯代表的四位bit显示为0100B，代表进入点名阶段的组员点名部分
				patternSwitch(2);
				tb[2].setChecked(true);
				show_first_name_log_open();
				break;
			case 3:
				tb[3].setChecked(true);	
				show_first_name_Reg_open();
				break;
			case 4:
				tb[4].setChecked(true);
				show_first_name_opt_open();
				break;
			case 5:
				tb[5].setChecked(true);	
				show_first_name_sho_open();
				break;
			case 6:
				tb[6].setChecked(true);
																										//stage 2：深度课设阶段
																										//四个LED灯代表的四位bit显示为1000B，代表进入深度课设阶段
				patternSwitch(3);
				break;
			case 7:
				tb[7].setChecked(true);	
				break;
			default:
				break;
			}
		}else{
			switch (i) {
			case 0:
				tb[0].setChecked(false);
				show_group_number_close();
				break;
			case 1:
				tb[1].setChecked(false);
				show_first_name_lea_close();
				break;
			case 2:
				tb[2].setChecked(false);
				show_first_name_log_close();
				break;
			case 3:
				tb[3].setChecked(false);
				show_first_name_Reg_close();
				break;
			case 4:
				tb[4].setChecked(false);
				show_first_name_opt_close();
				break;
			case 5:
				tb[5].setChecked(false);		
				show_first_name_sho_close();
				break;
			case 6:
				tb[6].setChecked(false);
				break;
			case 7:
				tb[7].setChecked(false);
				break;
			default:
				break;
			}
		}
	}
	
	private Handler uiHandler = new Handler(){															//初始化按钮文字
		public void handleMessage(Message msg){
			if(msg.what==1){	
				Log.i("获取数据", ""+msg.arg1);
				computed(msg.arg1);
			}
		}
	};
    
	public void openThread(){
			if(!flag){
				MyThread thread=new MyThread();
				this.flag=true;
				thread.start();
			}		
	}
	
	class MyThread extends Thread{																		//读取字符线程
		public void run(){
			int num = 0;				
			while(flag){
				try {				
					Message msgMessage=new Message();
					int value=DipClass.ReadValue();
					msgMessage.what=1;
					msgMessage.arg1=value;
					uiHandler.sendMessage(msgMessage);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}		
}
/* ********************编  程  主  体  部  分********************/
