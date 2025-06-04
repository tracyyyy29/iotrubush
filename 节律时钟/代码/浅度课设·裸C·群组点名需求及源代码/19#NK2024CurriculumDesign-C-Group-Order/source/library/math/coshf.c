/*
 * libm/coshf.c
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

static const float one = 1.0, half = 0.5, huge = 1.0e30;

float coshf(float x)
{
	float t, w;
	s32_t ix;

	GET_FLOAT_WORD(ix,x);
	ix &= 0x7fffffff;

	if (ix >= 0x7f800000)
		return x * x;

	if (ix < 0x3eb17218)
	{
		t = expm1f(fabsf(x));
		w = one + t;
		if (ix < 0x39800000)
			return one;
		return one + (t * t) / (w + w);
	}

	if (ix < 0x41100000)
	{
		t = expf(fabsf(x));
		return half * t + half / t;
	}

	if (ix < 0x42b17217)
		return half * expf(fabsf(x));

	if (ix <= 0x42b2d4fc)
	{
		w = expf(half * fabsf(x));
		t = half * w;
		return t * w;
	}

	return huge * huge;
}
