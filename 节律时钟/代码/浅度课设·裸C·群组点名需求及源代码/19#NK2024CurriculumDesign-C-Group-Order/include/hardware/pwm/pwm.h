/*
 * pwm.h
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

#ifndef __PWM_H__
#define __PWM_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <stddef.h>
#include <malloc.h>
#include <stdlib.h>
#include <io.h>
#include <list.h>

struct pwm_t
{
	const char * name;

	bool_t __enable;
	u32_t  __duty;
	u32_t  __period;
	bool_t __polarity;

	void (*config)(struct pwm_t * pwm, u32_t duty, u32_t period, bool_t polarity);
	void (*enable)(struct pwm_t * pwm);
	void (*disable)(struct pwm_t * pwm);

	void * priv;
};

struct pwm_list_t
{
	struct pwm_t * pwm;
	struct list_head entry;
};

extern struct pwm_list_t __pwm_list;

struct pwm_t * search_pwm(const char * name);
bool_t register_pwm(struct pwm_t * pwm);
bool_t unregister_pwm(struct pwm_t * pwm);
void pwm_config(struct pwm_t * pwm, u32_t duty, u32_t period, bool_t polarity);
void pwm_enable(struct pwm_t * pwm);
void pwm_disable(struct pwm_t * pwm);

#ifdef __cplusplus
}
#endif

#endif /* __PWM_H__ */
