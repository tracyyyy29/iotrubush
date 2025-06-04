/*
 * s5p6818-tick.h
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
#ifndef __S5P6818_TICK_H__
#define __S5P6818_TICK_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <stddef.h>
#include <io.h>
#include <sizes.h>
#include <string.h>
#include <s5p6818-clk.h>
#include <s5p6818-timer.h>
#include <s5p6818-irq.h>

/*
 * When HZ = 1000, The Max delay is 24 days
 */
#define time_after(a, b)		(((s32_t)(b) - (s32_t)(a) < 0))
#define time_before(a, b)		time_after(b, a)
#define time_after_eq(a,b)		(((s32_t)(a) - (s32_t)(b) >= 0))
#define time_before_eq(a,b)		time_after_eq(b, a)

extern volatile u32_t jiffies;

void s5p6818_tick_initial(void);
u32_t get_system_hz(void);
u64_t clock_gettime(void);

#ifdef __cplusplus
}
#endif

#endif /* __S5P6818_TICK_H__ */
