/*
 * cp15.h
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

#ifndef __ARM32_CP15_H__
#define __ARM32_CP15_H__

#include <types.h>

#ifdef __cplusplus
extern "C" {
#endif

void irq_enable(void);
void irq_disable(void);
void fiq_enable(void);
void fiq_disable(void);
void icache_enable(void);
void icache_disable(void);
int icache_status(void);
void dcache_enable(void);
void dcache_disable(void);
int dcache_status(void);
void wbuffer_enable(void);
void wbuffer_disable(void);
int wbuffer_status(void);
void mmu_enable(void);
void mmu_disable(void);
int mmu_status(void);
void vic_enable(void);
void vic_disable(void);
int vic_status(void);
void branch_enable(void);
void branch_disable(void);
int branch_status(void);
void ttb_set(u32_t base);
u32_t ttb_get(void);
void domain_set(u32_t domain);
u32_t domain_get(void);
void tlb_invalidate(void);
u32_t smp_processor_id(void);

#ifdef __cplusplus
}
#endif

#endif /* __ARM32_CP15_H__ */
