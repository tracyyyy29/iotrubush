/*
 * s5p6818-timer.h
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
#ifndef __S5P6818_TIMER_H__
#define __S5P6818_TIMER_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <types.h>
#include <string.h>
#include <io.h>
#include <stddef.h>
#include <s5p6818-rstcon.h>
#include <s5p6818-gpio.h>
#include <s5p6818-clk.h>
#include <s5p6818/reg-timer.h>

void s5p6818_timer_reset(void);
void s5p6818_timer_start(int ch, int irqon);
void s5p6818_timer_stop(int ch, int irqon);
u64_t s5p6818_timer_calc_tin(int ch, u32_t period);
void s5p6818_timer_count(int ch, u32_t cnt);
u32_t s5p6818_timer_read(int ch);
void s5p6818_timer_irq_clear(int ch);

#ifdef __cplusplus
}
#endif

#endif /* __S5P6818_TIMER_H__ */
