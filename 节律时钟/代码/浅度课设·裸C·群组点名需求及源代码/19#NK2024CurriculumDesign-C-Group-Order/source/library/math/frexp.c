/*
 * libm/frexp.c
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

static const double two54 = 1.80143985094819840000e+16;

double frexp(double x, int *eptr)
{
	s32_t hx, ix, lx;

	EXTRACT_WORDS(hx,lx,x);
	ix = 0x7fffffff & hx;
	*eptr = 0;

	if (ix >= 0x7ff00000 || ((ix | lx) == 0))
		return x;

	if (ix < 0x00100000)
	{
		x *= two54;
		GET_HIGH_WORD(hx,x);
		ix = hx & 0x7fffffff;
		*eptr = -54;
	}

	*eptr += (ix >> 20) - 1022;
	hx = (hx & 0x800fffff) | 0x3fe00000;
	SET_HIGH_WORD(x,hx);

	return x;
}
