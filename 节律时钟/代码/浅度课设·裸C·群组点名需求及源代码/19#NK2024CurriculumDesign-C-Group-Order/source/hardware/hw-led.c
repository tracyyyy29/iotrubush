/*
 * hw-led.c
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
 *
 */

#include <types.h>
#include <io.h>
#include <s5p6818-gpio.h>
#include <hw-led.h>

/*
 * LED1 -> S5P6818_GPIOA(16)
 * LED2 -> S5P6818_GPIOA(1)
 * LED3 -> S5P6818_GPIOA(2)
 * LED4 -> S5P6818_GPIOA(3)
 */
void led_initial(void)
{
	/* LED1 */
	gpio_set_cfg(S5P6818_GPIOA(16), 0);
	gpio_set_pull(S5P6818_GPIOA(16), GPIO_PULL_UP);
	gpio_direction_output(S5P6818_GPIOA(16), 0);
	gpio_set_value(S5P6818_GPIOA(16), 0);

	/* LED2 */
	gpio_set_cfg(S5P6818_GPIOA(1), 0);
	gpio_set_pull(S5P6818_GPIOA(1), GPIO_PULL_UP);
	gpio_direction_output(S5P6818_GPIOA(1), 0);
	gpio_set_value(S5P6818_GPIOA(1), 0);

	/* LED3 */
	gpio_set_cfg(S5P6818_GPIOA(2), 0);
	gpio_set_pull(S5P6818_GPIOA(2), GPIO_PULL_UP);
	gpio_direction_output(S5P6818_GPIOA(2), 0);
	gpio_set_value(S5P6818_GPIOA(2), 0);

	/* LED4 */
	gpio_set_cfg(S5P6818_GPIOA(3), 0);
	gpio_set_pull(S5P6818_GPIOA(3), GPIO_PULL_UP);
	gpio_direction_output(S5P6818_GPIOA(3), 0);
	gpio_set_value(S5P6818_GPIOA(3), 0);
}

void led_set_status(enum led_name name, enum led_status status)
{
	switch(name)
	{
	case LED_NAME_LED1:
		if(status == LED_STATUS_ON)
			gpio_direction_output(S5P6818_GPIOA(16), 1);
		else if(status == LED_STATUS_OFF)
			gpio_direction_output(S5P6818_GPIOA(16), 0);
		break;

	case LED_NAME_LED2:
		if(status == LED_STATUS_ON)
			gpio_direction_output(S5P6818_GPIOA(1), 1);
		else if(status == LED_STATUS_OFF)
			gpio_direction_output(S5P6818_GPIOA(1), 0);
		break;

	case LED_NAME_LED3:
		if(status == LED_STATUS_ON)
			gpio_direction_output(S5P6818_GPIOA(2), 1);
		else if(status == LED_STATUS_OFF)
			gpio_direction_output(S5P6818_GPIOA(2), 0);
		break;

	case LED_NAME_LED4:
		if(status == LED_STATUS_ON)
			gpio_direction_output(S5P6818_GPIOA(3), 1);
		else if(status == LED_STATUS_OFF)
			gpio_direction_output(S5P6818_GPIOA(3), 0);
		break;

	default:
		break;
	}
}
