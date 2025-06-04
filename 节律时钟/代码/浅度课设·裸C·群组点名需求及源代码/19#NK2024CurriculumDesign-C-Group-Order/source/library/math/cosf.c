/*
 * libm/cosf.c
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

static const double
	c1pio2 = 1 * M_PI_2,
	c2pio2 = 2 * M_PI_2,
	c3pio2 = 3 * M_PI_2,
	c4pio2 = 4 * M_PI_2;

float cosf(float x)
{
	float y[2];
	s32_t n, hx, ix;

	GET_FLOAT_WORD(hx,x);
	ix = hx & 0x7fffffff;

	if (ix <= 0x3f490fda)
	{
		if (ix < 0x39800000)
			if (((int) x) == 0)
				return 1.0;
		return __kernel_cosdf(x);
	}
	if (ix <= 0x407b53d1)
	{
		if (ix > 0x4016cbe3)
			return -__kernel_cosdf(x + (hx > 0 ? -c2pio2 : c2pio2));
		else
		{
			if (hx > 0)
				return __kernel_sindf(c1pio2 - x);
			else
				return __kernel_sindf(x + c1pio2);
		}
	}
	if (ix <= 0x40e231d5)
	{
		if (ix > 0x40afeddf)
			return __kernel_cosdf(x + (hx > 0 ? -c4pio2 : c4pio2));
		else
		{
			if (hx > 0)
				return __kernel_sindf(x - c3pio2);
			else
				return __kernel_sindf(-c3pio2 - x);
		}
	}
	else if (ix >= 0x7f800000)
		return x - x;
	else
	{
		n = __ieee754_rem_pio2f(x, y);
		switch (n & 3)
		{
		case 0:
			return __kernel_cosdf((double) y[0] + y[1]);
		case 1:
			return __kernel_sindf(-(double) y[0] - y[1]);
		case 2:
			return -__kernel_cosdf((double) y[0] + y[1]);
		default:
			return __kernel_sindf((double) y[0] + y[1]);
		}
	}
}
