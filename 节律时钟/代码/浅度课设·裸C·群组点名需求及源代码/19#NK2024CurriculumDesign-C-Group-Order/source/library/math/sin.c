/*
 * libm/sin.c
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

double sin(double x)
{
	double y[2], z = 0.0;
	s32_t n, ix;

	GET_HIGH_WORD(ix,x);

	ix &= 0x7fffffff;
	if (ix <= 0x3fe921fb)
	{
		if (ix < 0x3e400000)
		{
			if ((int) x == 0)
				return x;
		}
		return __kernel_sin(x, z, 0);
	}
	else if (ix >= 0x7ff00000)
		return x - x;
	else
	{
		n = __ieee754_rem_pio2(x, y);
		switch (n & 3)
		{
		case 0:
			return __kernel_sin(y[0], y[1], 1);
		case 1:
			return __kernel_cos(y[0], y[1]);
		case 2:
			return -__kernel_sin(y[0], y[1], 1);
		default:
			return -__kernel_cos(y[0], y[1]);
		}
	}
}
