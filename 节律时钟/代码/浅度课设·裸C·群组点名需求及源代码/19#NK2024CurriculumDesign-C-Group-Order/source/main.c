/*
 * main.c
 *
 * Copyright (C) 2015-2018 BeiJing OURS Education Technology Co., Ltd.
 * Copyright (C) 2017 Tangle.Xu. All rights reserved.
 *
 * Official site: http://www.ourselec.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, you can access it online at
 * http://www.gnu.org/licenses/gpl-2.0.html.
 *
 * Author: Tangle.Xu <420724072@qq.com>
 * Date: 2017.05
 * Version: v0.1
 */
#include <main.h>

extern int tester_key_led_beep(int argc, char * argv[]);

static void do_system_initial(void)
{
	malloc_init();

	s5p6818_clk_init();
	s5p6818_irq_init();
	s5p6818_gpiochip_init();
	s5p6818_gpiochip_alv_init();
	s5p6818_pwm_init();
	s5p6818_serial_initial();
	s5p6818_tick_initial();
	s5p6818_tick_delay_initial();
	s5p6818_fb_initial();

	led_initial();
	beep_initial();
	key_initial();
}

int main(int argc, char * argv[])
{
	do_system_initial();

	tester_key_led_beep(argc, argv);
	return 0;
}
