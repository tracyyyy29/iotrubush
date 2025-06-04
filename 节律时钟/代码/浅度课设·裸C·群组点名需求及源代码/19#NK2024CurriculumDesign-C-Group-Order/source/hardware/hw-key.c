/*
 * hw-key.c
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
#include <stddef.h>
#include <s5p6818-gpio.h>
#include <hw-key.h>

/*
 * VOL_UP    -> S5P6818_GPIOB(30)
 * VOL_DOWN  -> S5P6818_GPIOB(31)
 */
void key_initial(void)
{
	/* VOL_UP */
	gpio_set_cfg(S5P6818_GPIOB(30), 1);
	gpio_set_pull(S5P6818_GPIOB(30), GPIO_PULL_UP);
	gpio_direction_input(S5P6818_GPIOB(30));

	/* VOL_DOWN */
	gpio_set_cfg(S5P6818_GPIOB(31), 1);
	gpio_set_pull(S5P6818_GPIOB(31), GPIO_PULL_UP);
	gpio_direction_input(S5P6818_GPIOB(31));
}

static u32_t __get_key_status(void)
{
	u32_t key = 0;

	if(gpio_get_value(S5P6818_GPIOB(30)) == 0)
		key |= KEY_NAME_VOL_UP;

	if(gpio_get_value(S5P6818_GPIOB(31)) == 0)
		key |= KEY_NAME_VOL_DOWN;

	return key;
}

bool_t get_key_status(u32_t * key)
{
	static u32_t a = 0, b = 0, c = 0;

	a = __get_key_status();
	b = __get_key_status();
	c = __get_key_status();

	if((a == b) && (a == c))
	{
		*key = a;
		return TRUE;
	}

	return FALSE;
}

bool_t get_key_event(u32_t * keyup, u32_t * keydown)
{
	static u32_t key_old = 0x0;
	u32_t key;

	if(!get_key_status(&key))
		return FALSE;

	if(key != key_old)
	{
		*keyup = (key ^ key_old) & key_old;
		*keydown = (key ^ key_old) & key;
		key_old = key;

		return TRUE;
	}

	return FALSE;
}
