/*
 * s5p6818-fb.h
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
#ifndef __S5P6818_FB_H__
#define __S5P6818_FB_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <string.h>
#include <stddef.h>
#include <malloc.h>
#include <io.h>
#include <graphic/surface.h>
#include <s5p6818-tick-delay.h>
#include <s5p6818/reg-gpio.h>
#include <s5p6818/reg-mlc.h>
#include <s5p6818/reg-dpc.h>
#include <s5p6818/reg-dpa.h>
#include <s5p6818-clk.h>
#include <s5p6818-pwm.h>
#include <graphic/surface.h>

void s5p6818_fb_initial(void);
struct surface_t * s5p6818_screen_surface(void);
void s5p6818_screen_swap(void);
void s5p6818_screen_flush(void);
void s5p6818_screen_backlight(int brightness);

#ifdef __cplusplus
}
#endif

#endif /* __S5P6818_FB_H__ */
