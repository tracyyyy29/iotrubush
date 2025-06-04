/****************************************************
 *XX#NK2023CurriculumDesign-C-Group-Order-template.c*
 ****************************************************
 * 代码部分整体操作说明：									*
 * 1.代码构成：											*
 * 	由“认知发问”、“总结发问”、“编程主体”三部分三栏化构成。			*
 * 													*
 * 2.认知发问部分：										*
 *  四元组为“问题定位”、“问题描述”、“解决状态”、“解决策略”。 		*
 *  -问题定位：											*
 *    参见示例，在对应发问的编程主题部分进行标记，标签统一为Q1、Q2、……，*
 *  发问人需要对注明发问人的角色身份（如NK2023-01#-Le），具体位置的定	*
 *  位以注释的形式在代码中给出（如第510行的注释//Q0）从而实现定位功能。	*
 *  -问题描述：											*
 *    参见示例，阐释自己的问题，要求表述清晰，问题聚焦。				*
 *  -解决状态：											*
 *    参见示例，分为未解决/已解决两部分。其中已解决应标明解决人的角色身份	*
 *  （如NK2023-01#-R）。								*
 *  -解决策略：											*
 *	      参见示例，未解决状态下填“无”，已解决状态应清晰表述解决策略。		*
 * 	根据实际需求，完善四元组，并以四元组为单位进行扩展填充。			*
 * 													*
 * 3.总结展示部分：										*
 *    参见示例对实现代码后的心得体会等进行总结展示交流，需要明确总结人的角	*
 *  色身份。											*
 *  												*
 * 4.编程主体部分：										*
 *    由代码和注释说明两部分构成。							*
 *  -代码：											*
 *    大部分实现已经给出，需要深入学习理解。按照需求文档，对需要个性化定制  *
 *  的部分进行修改并增加相应的注释说明。						*
 *  -注释说明：											*
 *    按照已有的注释说明的格式，进行注释操作，注意要保证同样的缩进等格式要	*
 *  求确保可读性。注释的操作包括：注释填充、注释替换、注释增删，在进行操作	*
 *  后，同样需要进行角色身份的注明（##表示组号）：					*
 *    -注释填充：NK2023F-XX#-Le/R/Lo/O/S				*
 *    -注释替换：NK2023R-XX#-Le/R/Lo/O/S				*
 *    -注释增删：NK2023I&D-XX#-Le/R/Lo/O/S				*
 ***************************************************/

/*********************认  知  发  问  部  分********************
 *
 *【问题定位（示例）】Q0(NK2023-01#-Le)
 *【问题描述（示例）】tester_key_led_beep函数在哪里被调用的？
 *【解决状态（示例）】已解决(NK2023-01#-R)
 *【解决策略（示例）】在main.c的main函数中被调用
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
 *********************认  知  发  问  部  分********************/

/*********************总  结  展  示  部  分********************
 *【总结展示0】NK2023-01#-Lo
 *	通过裸C的代码实现，熟悉了实验箱的硬件环境，掌握了以中断的方式通过按键的方式实现对LED灯、蜂鸣器的控制……
 *
 *【总结展示1】
 *
 *
 *【总结展示2】
 *
 *
 *********************总  结  展  示  部  分********************/

/*********************编  程  主  体  部  分********************/

#include <main.h>
#include <s5p6818/reg-sys.h>
#include <s5p6818/reg-id.h>

static void gpiob30_interrupt_func(void *);
static void gpiob31_interrupt_func(void *);
/*******************hardware control card start*********************/
//description: control LEDs, init and so on
enum key_status{STAGE_CHANGE, ORDER} key_st; 																// 定义两种按键功能：“阶段切换”和“点名”

void led_all_off()								   															// LED灯全关
{												   															// led_set_status用来设置LED灯的状态，第一个参数为LED灯的编号，第二个参数为想要设置的状态。
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF); 															// 将第1个LED灯熄灭(OFF)
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF); 															// 将第2个LED灯熄灭(OFF)
	led_set_status(LED_NAME_LED3, LED_STATUS_OFF); 															// 将第3个LED灯熄灭(OFF)
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF); 															// 将第4个LED灯熄灭(OFF)
}

void led_all_on()								  															// LED灯全开
{												  															// led_set_status用来设置LED灯的状态，第一个参数为LED灯的编号，第二个参数为想要设置的状态。
	led_set_status(LED_NAME_LED1, LED_STATUS_ON); 															// 将第1个LED灯打开(ON)
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 将第2个LED灯打开(ON)
	led_set_status(LED_NAME_LED3, LED_STATUS_ON); 															// 将第3个LED灯打开(ON)
	led_set_status(LED_NAME_LED4, LED_STATUS_ON); 															// 将第4个LED灯打开(ON)
}

void init_stat() 																							// 初始化系统状态
{
	key_st = STAGE_CHANGE;			 																		// 按键功能初始化为“阶段切换”
									 																		// 群组点名任务开始标志
	led_all_on();					 																		// 打开所有LED灯
	beep_set_status(BEEP_STATUS_ON); 																		// 设置蜂鸣器为鸣响状态
	mdelay(3000);					 																		// 延迟3秒

	led_all_off();					 																		// 关闭所有LED灯
	beep_set_status(BEEP_STATUS_OFF);																		// 设置蜂鸣器为关闭状态
	mdelay(1000);					 																		// 延迟1秒
}

void tester_reset()																							// 蜂鸣器配合LED灯倒计时，对系统进行软复位
{
	led_set_status(LED_NAME_LED1, LED_STATUS_ON);
	led_set_status(LED_NAME_LED2, LED_STATUS_ON);
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);
	led_set_status(LED_NAME_LED4, LED_STATUS_ON);
	mdelay(1000);

	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);

	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);

	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	led_set_status(LED_NAME_LED3, LED_STATUS_OFF);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);

	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(2000);
																											// 系统软件复位
	write32(phys_to_virt(S5P6818_SYS_PWRCONT), (read32(phys_to_virt(S5P6818_SYS_PWRCONT)) & ~(0x1 << 3)) | (0x1 << 3));
	write32(phys_to_virt(S5P6818_SYS_PWRMODE), (read32(phys_to_virt(S5P6818_SYS_PWRMODE)) & ~(0x1 << 12)) | (0x1 << 12));

	while (1)
	{
	}
}

static void gpiob30_interrupt_func(void *data)																// 中断服务函数，VOL_UP_KEY -> GPIOB(30)，音量"+"
{
	request_irq("GPIOB31", gpiob31_interrupt_func, IRQ_TYPE_EDGE_FALLING, 0);								// 如果同时按下音量"+"键和音量"—"键，申请GPIOB(31)对应的中断服务函数
	mdelay(200);
	key_status_change();																					// 根据程序流程运行进度切换按键功能
}

static void gpiob31_interrupt_func(void *data)																// 中断服务函数，VOL_DOWN_KEY -> GPIOB(31)，音量"—"
{
	request_irq("GPIOB30", gpiob30_interrupt_func, IRQ_TYPE_EDGE_FALLING, 0);								// 如果同时按下音量"+"键和音量"—"键，申请GPIOB(30)对应的中断服务函数
	mdelay(200);
	key_status_change();																					// 根据流程运行进度切换按键功能
}
/*******************hardware control card end***********************/

/***************group order functions card start*************************/
//description: the core functions to realize stage and group order
void system_stage(int tag) 																					// 系统阶段选择函数:1-启动阶段，2-点名阶段，3-深度课设阶段，0-重启阶段
{
	switch (tag)
	{

	case 1:								 																	// 启动阶段01，长响一秒，间隔一秒，长响三秒：01
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(1000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(3000);
		beep_set_status(BEEP_STATUS_OFF);	 																// 蜂鸣器关闭
		mdelay(1000);
		break;

	case 2:								 																	// 点名阶段10，长响三秒，间隔一秒，长响一秒：10
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(3000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(1000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		break;

	case 3:								 																	// 深度课设阶段11，长响三秒，间隔一秒，长响三秒：11
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(3000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(3000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		break;

	case 0:								 																	// 重启阶段00，长响一秒，间隔一秒，长响一秒：00
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(1000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		beep_set_status(BEEP_STATUS_ON); 																	// 蜂鸣器响
		mdelay(1000);
		beep_set_status(BEEP_STATUS_OFF); 																	// 蜂鸣器关闭
		mdelay(1000);
		break;

	default:
		break;
	}
}

// author: G19LeaHuang
// date: 2024/3/13
// 显示组号的二进制转换格式，组号“XX”转换成8位二进制数为XXXX XXXXB，模板以数字"0"为例，"0"转换成8位二进数为0000 0000B，请按照本组真实组号对本函数进行修改
void show_group_number()
{
	/********请在下方区域修改代码及注释，实现利用LED灯显示本组组号二进制形式高4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED3, LED_STATUS_OFF);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF);  															// 0
	/********请在上方区域修改代码及注释，实现利用LED灯显示本组组号二进制形式高4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	/********请在下方区域修改代码及注释，实现利用LED灯显示本组组号二进制形式低4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED3, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_ON); 															// 1
	/********请在上方区域修改代码及注释，实现利用LED灯显示本组组号二进制形式低4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	beep_set_status(BEEP_STATUS_ON);   																		// 蜂鸣器鸣响0.2秒、关闭0.1秒；再重复一次，即快速“滴滴”两声，表示显示组号结束
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(100);
	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);
}

// author: G19LeaHuang
// date: 2024/3/13
// 显示组长姓氏拼音首字母小写的ASCII码。组长姓氏“XXX”的拼音首字母小写“X”所对应ASCII码为XXXX XXXXB
void show_first_name_Lea()
{																											// 模板以"长"为例，“长”的拼音首字母小写“z”所对应ASCII码为0111 1010B，请按照本组组长真实姓氏对本函数进行修改
	/********请在下方区域修改代码及注释，实现利用LED灯显示组长姓氏拼音首字母小写的ASCII码高4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED3, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED4, LED_STATUS_ON);  															// 1
	/********请在上方区域修改代码及注释，实现利用LED灯显示组长姓氏拼音首字母小写的ASCII码高4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000); 	 																						// 延迟一秒

	/********请在下方区域修改代码及注释，实现利用LED灯显示组长姓氏拼音首字母小写的ASCII码低4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED3, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF); 															// 0
	/********请在上方区域修改代码及注释，实现利用LED灯显示组长姓氏拼音首字母小写的ASCII码低4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	beep_set_status(BEEP_STATUS_ON); 																		// 蜂鸣器鸣响0.2秒、关闭0.1秒；再重复一次，即快速“滴滴”两声，表示组长点名结束
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(100);
	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);
}

// author: G19ReqZhang
// date: 2024/3/13
// 显示需求员姓氏拼音首字母小写的ASCII码。需求员姓氏“XXX”的拼音首字母小写“X”所对应ASCII码为XXXX XXXXB
void show_first_name_req()
{
	/********请在下方区域编写代码及注释，实现利用LED灯显示需求员姓氏拼音首字母小写的ASCII码高4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_ON); 															// 1
	/********请在上方区域编写代码及注释，实现利用LED灯显示需求员姓氏拼音首字母小写的ASCII码高4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	/********请在下方区域编写代码及注释，实现利用LED灯显示需求员姓氏拼音首字母小写的ASCII码低4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF); 															// 0
	/********请在上方区域编写代码及注释，实现利用LED灯显示需求员姓氏拼音首字母小写的ASCII码低4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	beep_set_status(BEEP_STATUS_ON);  																		// 蜂鸣器鸣响0.2秒、关闭0.1秒；再重复一次，即快速“滴滴”两声，表示需求员点名结束
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(100);
	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);
}

// author: G19LogTang
// date: 2023/xx/xx
// 显示周志员姓氏拼音首字母小写的ASCII码。周志员姓氏“XXX”的拼音首字母小写“X”所对应ASCII码为XXXX XXXXB
void show_first_name_log()
{
	/********请在下方区域编写代码及注释，实现利用LED灯显示周志员姓氏拼音首字母小写的ASCII码高4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_ON); 															// 1
	/********请在上方区域编写代码及注释，实现利用LED灯显示周志员姓氏拼音首字母小写的ASCII码高4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	/********请在下方区域编写代码及注释，实现利用LED灯显示周志员姓氏拼音首字母小写的ASCII码低4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED3, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF); 															// 0
	/********请在上方区域编写代码及注释，实现利用LED灯显示周志员姓氏拼音首字母小写的ASCII码低4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	beep_set_status(BEEP_STATUS_ON);																		// 蜂鸣器鸣响0.2秒、关闭0.1秒；再重复一次，即快速“滴滴”两声，表示周志员点名结束
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(100);
	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);
}

// author: G19OptZhang
// date: 2023/xx/xx
// 显示优裁员姓氏拼音首字母的小写的ASCII码。优裁员姓氏“XXX”的拼音首字母小写“X”所对应ASCII码为XXXX XXXXB
void show_first_name_opt()
{
	/********请在下方区域编写代码及注释，实现利用LED灯显示优裁员姓氏拼音首字母小写的ASCII码高4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_ON); 															// 1
	/********请在上方区域编写代码及注释，实现利用LED灯显示优裁员姓氏拼音首字母小写的ASCII码高4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	/********请在下方区域编写代码及注释，实现利用LED灯显示优裁员姓氏拼音首字母小写的ASCII码低4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED2, LED_STATUS_OFF); 															// 0
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF); 															// 0
	/********请在上方区域编写代码及注释，实现利用LED灯显示优裁员姓氏拼音首字母小写的ASCII码低4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	beep_set_status(BEEP_STATUS_ON);																		// 蜂鸣器鸣响0.2秒、关闭0.1秒；再重复一次，即快速“滴滴”两声，表示优裁员点名结束
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(100);
	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);
}

// author: G19ShoGuo
// date: 2023/xx/xx
// 显示展示员姓氏拼音首字母小写的ASCII码。展示员姓氏“XXX”的拼音首字母小写“X”所对应ASCII码为XXXX XXXXB
void show_first_name_sho()
{
	/********请在下方区域编写代码及注释，实现利用LED灯显示展示员姓氏拼音首字母小写的ASCII码高4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_OFF); 															// 0
	/********请在上方区域编写代码及注释，实现利用LED灯显示展示员姓氏拼音首字母小写的ASCII码高4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	/********请在下方区域编写代码及注释，实现利用LED灯显示展示员姓氏拼音首字母小写的ASCII码低4位的功能********/
	led_set_status(LED_NAME_LED1, LED_STATUS_OFF);  															// 0
	led_set_status(LED_NAME_LED2, LED_STATUS_ON); 															// 1
	led_set_status(LED_NAME_LED3, LED_STATUS_ON);  															// 1
	led_set_status(LED_NAME_LED4, LED_STATUS_ON); 															// 1
	/********请在上方区域编写代码及注释，实现利用LED灯显示展示员姓氏拼音首字母小写的ASCII码低4位的功能********/

	mdelay(3000);
	led_all_off(); 																							// LED灯全关
	mdelay(1000);  																							// 延迟一秒

	beep_set_status(BEEP_STATUS_ON);																		// 蜂鸣器鸣响0.2秒、关闭0.1秒；再重复一次，即快速“滴滴”两声，表示展示员点名结束
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(100);
	beep_set_status(BEEP_STATUS_ON);
	mdelay(200);
	beep_set_status(BEEP_STATUS_OFF);
	mdelay(1000);
}

void interrupt_group_order()																				// 根据按键依次对需求员、周志员、优裁员、展示员进行点名
{
	u32_t VOL_UP_STAT = gpio_get_value(S5P6818_GPIOB(30));													// 读取音量"+"键的按键状态，判断是否按下，读取值为0，为按键已按下；值为1，为按键未按下
	u32_t VOL_DOWN_STAT = gpio_get_value(S5P6818_GPIOB(31));												// 读取音量"—"键的按键状态，判断是否按下，读取值为0，为按键已按下；值为1，为按键未按下

	if (VOL_UP_STAT == 0 && VOL_DOWN_STAT == 0)
	{

		show_first_name_opt();																				// 优裁员点名
		show_first_name_sho();																				// 展示员点名

		beep_set_status(BEEP_STATUS_ON);																	// 三声短促的蜂鸣器鸣响“滴滴滴”，表示所有点名结束
		mdelay(200);
		beep_set_status(BEEP_STATUS_OFF);
		mdelay(100);
		beep_set_status(BEEP_STATUS_ON);
		mdelay(200);
		beep_set_status(BEEP_STATUS_OFF);
		mdelay(100);
		beep_set_status(BEEP_STATUS_ON);
		mdelay(200);
		beep_set_status(BEEP_STATUS_OFF);
		mdelay(3000);

		key_st = STAGE_CHANGE;																				// 点名结束，按键功能切换为“阶段切换”
	}
	else if (VOL_UP_STAT == 0 && VOL_DOWN_STAT != 0)
	{
		show_first_name_log();																				// 周志员点名
	}
	else if (VOL_UP_STAT != 0 && VOL_DOWN_STAT == 0)
	{
		show_first_name_req();																				// 需求员点名
	}
	else
	{
	}
}

void interrupt_stage_change()																				// 根据按键切换不同stage
{
	u32_t VOL_UP_STAT = gpio_get_value(S5P6818_GPIOB(30));    												// 读取音量"+"键的按键状态，判断是否按下，读取值为0，为按键已按下；值为1，为按键未按下
	u32_t VOL_DOWN_STAT = gpio_get_value(S5P6818_GPIOB(31));												// 读取音量"—"键的按键状态，判断是否按下，读取值为0，为按键已按下；值为1，为按键未按下

	if (VOL_UP_STAT == 0 && VOL_DOWN_STAT == 0)																// stage2：深度课设阶段
	{
		system_stage(3);																					// 等待状态

		led_all_on(); 																						// 打开所有LED灯
		while (1)	  																						// 每间隔3秒，循环一次：蜂鸣器快速鸣响三声“滴滴滴”
		{
			beep_set_status(BEEP_STATUS_ON);
			mdelay(200);
			beep_set_status(BEEP_STATUS_OFF);
			mdelay(100);
			beep_set_status(BEEP_STATUS_ON);
			mdelay(200);
			beep_set_status(BEEP_STATUS_OFF);
			mdelay(100);
			beep_set_status(BEEP_STATUS_ON);
			mdelay(200);
			beep_set_status(BEEP_STATUS_OFF);
			mdelay(3000);
		}


		// system_stage(0);																					// stage3：重启阶段
		// tester_reset();																					// 蜂鸣器配合LED灯倒计时，对系统进行软复位
	}
	else if (VOL_UP_STAT == 0 && VOL_DOWN_STAT != 0)
	{
		system_stage(2);																					// stage1：点名阶段

		show_group_number();																				// 显示组号
		show_first_name_Lea(); 																				// 组长点名
																											// 组员点名
		key_st = ORDER;																						// 按键功能切换为“点名”
	}
	else if (VOL_UP_STAT != 0 && VOL_DOWN_STAT == 0)
	{

		system_stage(1); 																					// stage0：启动阶段01
	}
	else
	{
	}
}

void key_status_change()																					// 根据全局变量key_st的值切换按键功能为“阶段切换”或“点名”
{
	switch (key_st)
	{
	case STAGE_CHANGE:																						// 按键功能切换为“阶段切换”

		interrupt_stage_change();
		break;
	case ORDER:																								// 按键功能切换为“点名”
		interrupt_group_order();
		break;
	}
}
/*******************group order functions card end***********************/

/*******************main function card start*********************/
//description: the main function for main.c's calling and calling other function
int tester_key_led_beep(int argc, char *argv[])																// 通过按键中断方式进行群组点名任务
{																											// Q0
	init_stat(); 																							// 初始化系统状态：四个LED灯全亮，蜂鸣器长响，持续3秒；之后LED灯全灭，蜂鸣器关闭，持续1秒
	gpio_set_cfg(S5P6818_GPIOB(30), 1);																		// 初始化VOL_UP_KEY按键对应的GPIOB(30)
	gpio_set_pull(S5P6818_GPIOB(30), GPIO_PULL_UP);
	gpio_direction_input(S5P6818_GPIOB(30));
	request_irq("GPIOB30", gpiob30_interrupt_func, IRQ_TYPE_EDGE_FALLING, 0);								// 申请音量"+"键对应的中断服务函数，下降沿触发中断

	gpio_set_cfg(S5P6818_GPIOB(31), 1);																		// 初始化VOL_DOWN_KEY按键对应的GPIOB(31)
	gpio_set_pull(S5P6818_GPIOB(31), GPIO_PULL_UP);
	gpio_direction_input(S5P6818_GPIOB(31));
	request_irq("GPIOB31", gpiob31_interrupt_func, IRQ_TYPE_EDGE_FALLING, 0);								// 申请音量"—"键对应的中断服务函数，下降沿触发中断

	while (1)
	{
	}

	return 0;
}
/*******************main function card end***********************/

/*********************编  程  主  体  部  分********************/
