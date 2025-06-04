/*
 * libm/tanh.c
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

static const double one = 1.0, two = 2.0, tiny = 1.0e-300;

double tanh(double x)
{
	double t, z;
	s32_t jx, ix;

	GET_HIGH_WORD(jx,x);
	ix = jx & 0x7fffffff;

	if (ix >= 0x7ff00000)
	{
		if (jx >= 0)
			return one / x + one;
		else
			return one / x - one;
	}

	if (ix < 0x40360000)
	{
		if (ix < 0x3c800000)
			return x * (one + x);
		if (ix >= 0x3ff00000)
		{
			t = expm1(two * fabs(x));
			z = one - two / (t + two);
		}
		else
		{
			t = expm1(-two * fabs(x));
			z = -t / (t + two);
		}
	}
	else
	{
		z = one - tiny;
	}
	return (jx >= 0) ? z : -z;
}
