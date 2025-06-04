/*
 * libc/stdlib/rand.c
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

#include <stdlib.h>

static unsigned short __seed[3];

static long jrand48(unsigned short xsubi[3])
{
	u64_t x;

	/*
	 * The xsubi[] array is little endian by spec
	 */
	x = (u64_t) (u16_t)xsubi[0] +
	    ((u64_t) (u16_t)xsubi[1] << 16) +
	    ((u64_t) (u16_t)xsubi[2] << 32);

	x = (0x5deece66dULL * x) + 0xb;

	xsubi[0] = (unsigned short)(u16_t)x;
	xsubi[1] = (unsigned short)(u16_t)(x >> 16);
	xsubi[2] = (unsigned short)(u16_t)(x >> 32);

	return (long)(s32_t)(x >> 16);
}

static long lrand48(void)
{
	return (u32_t)jrand48(__seed) >> 1;
}

static void srand48(long seedval)
{
	unsigned short * seed = __seed;

	seed[0] = 0x330e;
	seed[1] = (unsigned short)seedval;
	seed[2] = (unsigned short)((u32_t)seedval >> 16);
}

int rand(void)
{
	return (int)lrand48();
}

void srand(unsigned int seed)
{
	srand48(seed);
}
