/*
 * reg-pwm.h
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
#ifndef __S5P6818_REG_PWM_H__
#define __S5P6818_REG_PWM_H__

#define S5P6818_PWM_BASE		(0xC0018000)

#define S5P6818_PWM_TCFG0	  	(S5P6818_PWM_BASE + 0x0000)
#define S5P6818_PWM_TCFG1	  	(S5P6818_PWM_BASE + 0x0004)
#define S5P6818_PWM_TCON	  	(S5P6818_PWM_BASE + 0x0008)
#define S5P6818_PWM_TSTAT	  	(S5P6818_PWM_BASE + 0x0044)

#define S5P6818_PWM0_BASE		(S5P6818_PWM_BASE + 0x000C)
#define S5P6818_PWM1_BASE		(S5P6818_PWM_BASE + 0x0018)
#define S5P6818_PWM2_BASE		(S5P6818_PWM_BASE + 0x0024)
#define S5P6818_PWM3_BASE		(S5P6818_PWM_BASE + 0x0030)

#define PWM_TCNTB				(0x00)
#define PWM_TCMPB				(0x04)
#define PWM_TCNTO				(0x08)

#endif /* __S5P6818_REG_PWM_H__ */
