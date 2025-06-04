/*
 * libm/modff.c
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

static const float one = 1.0;

float modff(float x, float *iptr)
{
	s32_t i0, j0;
	u32_t i;

	GET_FLOAT_WORD(i0,x);
	j0 = ((i0 >> 23) & 0xff) - 0x7f;
	if (j0 < 23)
	{
		if (j0 < 0)
		{
			SET_FLOAT_WORD(*iptr,i0&0x80000000);
			return x;
		}
		else
		{
			i = (0x007fffff) >> j0;
			if ((i0 & i) == 0)
			{
				u32_t ix;
				*iptr = x;
				GET_FLOAT_WORD(ix,x);
				SET_FLOAT_WORD(x,ix&0x80000000);
				return x;
			}
			else
			{
				SET_FLOAT_WORD(*iptr,i0&(~i));
				return x - *iptr;
			}
		}
	}
	else
	{
		u32_t ix;
		*iptr = x * one;
		GET_FLOAT_WORD(ix,x);
		SET_FLOAT_WORD(x,ix&0x80000000);
		return x;
	}
}
