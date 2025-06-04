/*
 * libm/sqrtf.c
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

#include <math.h>

static const float one = 1.0, tiny = 1.0e-30;

float sqrtf(float x)
{
	float z;
	s32_t sign = (int) 0x80000000;
	s32_t ix, s, q, m, t, i;
	u32_t r;

	GET_FLOAT_WORD(ix,x);

	if ((ix & 0x7f800000) == 0x7f800000)
	{
		return x * x + x;
	}

	if (ix <= 0)
	{
		if ((ix & (~sign)) == 0)
			return x;
		else if (ix < 0)
			return (x - x) / (x - x);
	}

	m = (ix >> 23);
	if (m == 0)
	{
		for (i = 0; (ix & 0x00800000) == 0; i++)
			ix <<= 1;
		m -= i - 1;
	}
	m -= 127;
	ix = (ix & 0x007fffff) | 0x00800000;
	if (m & 1)
		ix += ix;
	m >>= 1;

	ix += ix;
	q = s = 0;
	r = 0x01000000;

	while (r != 0)
	{
		t = s + r;
		if (t <= ix)
		{
			s = t + r;
			ix -= t;
			q += r;
		}
		ix += ix;
		r >>= 1;
	}

	if (ix != 0)
	{
		z = one - tiny;
		if (z >= one)
		{
			z = one + tiny;
			if (z > one)
				q += 2;
			else
				q += (q & 1);
		}
	}
	ix = (q >> 1) + 0x3f000000;
	ix += (m << 23);

	SET_FLOAT_WORD(z,ix);
	return z;
}
