/*
 * libm/frexpf.c
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

static const float two25 = 3.3554432000e+07;

float frexpf(float x, int *eptr)
{
	s32_t hx, ix;

	GET_FLOAT_WORD(hx,x);
	ix = 0x7fffffff & hx;
	*eptr = 0;

	if (ix >= 0x7f800000 || (ix == 0))
		return x;

	if (ix < 0x00800000)
	{
		x *= two25;
		GET_FLOAT_WORD(hx,x);
		ix = hx & 0x7fffffff;
		*eptr = -25;
	}

	*eptr += (ix >> 23) - 126;
	hx = (hx & 0x807fffff) | 0x3f000000;
	SET_FLOAT_WORD(x,hx);

	return x;
}
