/*
 * io.h
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
#ifndef __IO_H__
#define __IO_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

static inline u8_t __read8(physical_addr_t addr)
{
	return( *((volatile u8_t *)(addr)) );
}

static inline u16_t __read16(physical_addr_t addr)
{
	return( *((volatile u16_t *)(addr)) );
}

static inline u32_t __read32(physical_addr_t addr)
{
	return( *((volatile u32_t *)(addr)) );
}

static inline u64_t __read64(physical_addr_t addr)
{
	return( *((volatile u64_t *)(addr)) );
}

static inline void __write8(physical_addr_t addr, u8_t value)
{
	*((volatile u8_t *)(addr)) = value;
}

static inline void __write16(physical_addr_t addr, u16_t value)
{
	*((volatile u16_t *)(addr)) = value;
}

static inline void __write32(physical_addr_t addr, u32_t value)
{
	*((volatile u32_t *)(addr)) = value;
}

static inline void __write64(physical_addr_t addr, u64_t value)
{
	*((volatile u64_t *)(addr)) = value;
}

static inline physical_addr_t virt_to_phys(virtual_addr_t virt)
{
	return (physical_addr_t)virt;
}

static inline virtual_addr_t phys_to_virt(physical_addr_t phys)
{
	return (virtual_addr_t)phys;
}

#define read8(a)		__read8(a)
#define read16(a)		__read16(a)
#define read32(a)		__read32(a)
#define read64(a)		__read64(a)

#define write8(a, v)	__write8((a), (v))
#define write16(a, v)	__write16((a), (v))
#define write32(a, v)	__write32((a), (v))
#define write64(a, v)	__write64((a), (v))

#ifdef __cplusplus
}
#endif

#endif /* __IO_H__ */
