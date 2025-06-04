/*
 * s5p6818-gpio.h
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
#ifndef __S5P6818_GPIO_H__
#define __S5P6818_GPIO_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <s5p6818/reg-gpio.h>
#include <s5p6818/reg-alv.h>
#include <gpio/gpio.h>

#define S5P6818_GPIOA(x)		(x)
#define S5P6818_GPIOB(x)		(x + 32)
#define S5P6818_GPIOC(x)		(x + 64)
#define S5P6818_GPIOD(x)		(x + 96)
#define S5P6818_GPIOE(x)		(x + 128)
#define S5P6818_GPIOALV(x)		(x + 160)

void s5p6818_gpiochip_init(void);
void s5p6818_gpiochip_exit(void);
void s5p6818_gpiochip_alv_init(void);
void s5p6818_gpiochip_alv_exit(void);

#ifdef __cplusplus
}
#endif

#endif /* __S5P6818_GPIO_H__ */
