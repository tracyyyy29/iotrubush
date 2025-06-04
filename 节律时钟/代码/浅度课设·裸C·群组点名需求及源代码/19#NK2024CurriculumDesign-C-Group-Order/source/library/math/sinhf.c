/*
 * libm/sinhf.c
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

static const float one = 1.0, shuge = 1.0e37;

float sinhf(float x)
{
	float t, w, h;
	s32_t ix, jx;

	GET_FLOAT_WORD(jx,x);
	ix = jx & 0x7fffffff;

	if (ix >= 0x7f800000)
		return x + x;

	h = 0.5;
	if (jx < 0)
		h = -h;

	if (ix < 0x41100000)
	{
		if (ix < 0x39800000)
			if (shuge + x > one)
				return x;
		t = expm1f(fabsf(x));
		if (ix < 0x3f800000)
			return h * ((float) 2.0 * t - t * t / (t + one));
		return h * (t + t / (t + one));
	}

	if (ix < 0x42b17217)
		return h * expf(fabsf(x));

	if (ix <= 0x42b2d4fc)
	{
		w = expf((float) 0.5 * fabsf(x));
		t = h * w;
		return t * w;
	}

	return x * shuge;
}
