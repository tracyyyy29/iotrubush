/*
 * libm/modf.c
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

static const double one = 1.0;

double modf(double x, double *iptr)
{
	s32_t i0, i1, j0;
	u32_t i;

	EXTRACT_WORDS(i0,i1,x);
	j0 = ((i0 >> 20) & 0x7ff) - 0x3ff;
	if (j0 < 20)
	{
		if (j0 < 0)
		{
			INSERT_WORDS(*iptr,i0&0x80000000,0);
			return x;
		}
		else
		{
			i = (0x000fffff) >> j0;
			if (((i0 & i) | i1) == 0)
			{
				u32_t high;
				*iptr = x;
				GET_HIGH_WORD(high,x);
				INSERT_WORDS(x,high&0x80000000,0);
				return x;
			}
			else
			{
				INSERT_WORDS(*iptr,i0&(~i),0);
				return x - *iptr;
			}
		}
	}
	else if (j0 > 51)
	{
		u32_t high;
		*iptr = x * one;
		GET_HIGH_WORD(high,x);
		INSERT_WORDS(x,high&0x80000000,0);
		return x;
	}
	else
	{
		i = ((u32_t)(0xffffffff)) >> (j0 - 20);
		if ((i1 & i) == 0)
		{
			u32_t high;
			*iptr = x;
			GET_HIGH_WORD(high,x);
			INSERT_WORDS(x,high&0x80000000,0);
			return x;
		}
		else
		{
			INSERT_WORDS(*iptr,i0,i1&(~i));
			return x - *iptr;
		}
	}
}
