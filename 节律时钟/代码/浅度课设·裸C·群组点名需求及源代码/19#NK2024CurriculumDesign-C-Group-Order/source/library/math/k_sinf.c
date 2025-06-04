/*
 * libm/k_sinf.c
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
	S1 = -0x15555554cbac77.0p-55,
	S2 = 0x111110896efbb2.0p-59,
	S3 = -0x1a00f9e2cae774.0p-65,
	S4 = 0x16cd878c3b46a7.0p-71;

float __kernel_sindf(double x)
{
	double r, s, w, z;

	z = x * x;
	w = z * z;
	r = S3 + z * S4;
	s = z * x;
	return (x + s * (S1 + z * S2)) + s * w * r;
}
