/*
 * hw-led.h
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
#ifndef __HW_LED_H__
#define __HW_LED_H__

#ifdef __cplusplus
extern "C" {
#endif

enum led_name {
	LED_NAME_LED1		= 1,
	LED_NAME_LED2		= 2,
	LED_NAME_LED3		= 3,
	LED_NAME_LED4		= 4,
};

enum led_status {
	LED_STATUS_OFF		= 0,
	LED_STATUS_ON		= 1,
};

void led_initial(void);
void led_set_status(enum led_name name, enum led_status status);

#ifdef __cplusplus
}
#endif

#endif /* __HW_LED_H__ */
