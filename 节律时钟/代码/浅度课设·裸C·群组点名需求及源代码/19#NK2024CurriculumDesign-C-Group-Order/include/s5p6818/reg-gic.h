/*
 * reg-gic.h
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

#ifndef __S5P6818_REG_GIC_H__
#define __S5P6818_REG_GIC_H__

#define S5P6818_GIC_CPU_BASE	(0xc000a000)
#define S5P6818_GIC_DIST_BASE	(0xc0009000)

#define CPU_CTRL				(0x00)
#define CPU_PRIMASK				(0x04)
#define CPU_BINPOINT			(0x08)
#define CPU_INTACK				(0x0c)
#define CPU_EOI					(0x10)
#define CPU_RUNNINGPRI			(0x14)
#define CPU_HIGHPRI				(0x18)

#define DIST_CTRL				(0x000)
#define DIST_CTR				(0x004)
#define DIST_ENABLE_SET			(0x100)
#define DIST_ENABLE_CLEAR		(0x180)
#define DIST_PENDING_SET		(0x200)
#define DIST_PENDING_CLEAR		(0x280)
#define DIST_ACTIVE_BIT			(0x300)
#define DIST_PRI				(0x400)
#define DIST_TARGET				(0x800)
#define DIST_CONFIG				(0xc00)
#define DIST_SOFTINT			(0xf00)

#endif /* __S5P6818_REG_GIC_H__ */
