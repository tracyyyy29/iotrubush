/*
 * gpio.h
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

#ifndef __GPIO_H__
#define __GPIO_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <stddef.h>
#include <malloc.h>
#include <stdlib.h>
#include <io.h>
#include <list.h>

enum gpio_pull_t {
	GPIO_PULL_UP			= 0,
	GPIO_PULL_DOWN			= 1,
	GPIO_PULL_NONE			= 2,
};

enum gpio_drv_t {
	GPIO_DRV_LOW			= 0,
	GPIO_DRV_MEDIAN			= 1,
	GPIO_DRV_HIGH			= 2,
};

enum gpio_rate_t {
	GPIO_RATE_SLOW			= 0,
	GPIO_RATE_FAST			= 1,
};

enum gpio_direction_t {
	GPIO_DIRECTION_INPUT	= 0,
	GPIO_DIRECTION_OUTPUT	= 1,
	GPIO_DIRECTION_UNKOWN	= 2,
};

struct gpiochip_t
{
	const char * name;
	int base;
	int ngpio;

	void (*set_cfg)(struct gpiochip_t * chip, int offset, int cfg);
	int  (*get_cfg)(struct gpiochip_t * chip, int offset);
	void (*set_pull)(struct gpiochip_t * chip, int offset, enum gpio_pull_t pull);
	enum gpio_pull_t (*get_pull)(struct gpiochip_t * chip, int offset);
	void (*set_drv)(struct gpiochip_t * chip, int offset, enum gpio_drv_t drv);
	enum gpio_drv_t (*get_drv)(struct gpiochip_t * chip, int offset);
	void (*set_rate)(struct gpiochip_t * chip, int offset, enum gpio_rate_t rate);
	enum gpio_rate_t (*get_rate)(struct gpiochip_t * chip, int offset);
	void (*set_dir)(struct gpiochip_t * chip, int offset, enum gpio_direction_t dir);
	enum gpio_direction_t (*get_dir)(struct gpiochip_t * chip, int offset);
	void (*set_value)(struct gpiochip_t * chip, int offset, int value);
	int  (*get_value)(struct gpiochip_t * chip, int offset);

	void * priv;
};

struct gpiochip_t * search_gpiochip(const char * name);
bool_t register_gpiochip(struct gpiochip_t * chip);
bool_t unregister_gpiochip(struct gpiochip_t * chip);

int gpio_is_valid(int no);
void gpio_set_cfg(int no, int cfg);
int gpio_get_cfg(int no);
void gpio_set_pull(int no, enum gpio_pull_t pull);
enum gpio_pull_t gpio_get_pull(int no);
void gpio_set_drv(int no, enum gpio_drv_t drv);
enum gpio_drv_t gpio_get_drv(int no);
void gpio_set_rate(int no, enum gpio_rate_t rate);
enum gpio_rate_t gpio_get_rate(int no);
void gpio_set_direction(int no, enum gpio_direction_t dir);
enum gpio_direction_t gpio_get_direction(int no);
void gpio_set_value(int no, int value);
int gpio_get_value(int no);
void gpio_direction_output(int no, int value);
void gpio_direction_input(int no);

#ifdef __cplusplus
}
#endif

#endif /* __GPIO_H__ */
