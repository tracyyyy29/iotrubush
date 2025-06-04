/*
 * libc/ctype/ctype.c
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

#include <ctype.h>

const char __const_ctype[] = {
	0,
	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C,
	_C,	_C|_S,	_C|_S,	_C|_S,	_C|_S,	_C|_S,	_C,	_C,
	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C,
	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C,
	_S|(char)_B,	_P,	_P,	_P,	_P,	_P,	_P,	_P,
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P,
	_N,	_N,	_N,	_N,	_N,	_N,	_N,	_N,
	_N,	_N,	_P,	_P,	_P,	_P,	_P,	_P,
	_P,	_U|_X,	_U|_X,	_U|_X,	_U|_X,	_U|_X,	_U|_X,	_U,
	_U,	_U,	_U,	_U,	_U,	_U,	_U,	_U,
	_U,	_U,	_U,	_U,	_U,	_U,	_U,	_U,
	_U,	_U,	_U,	_P,	_P,	_P,	_P,	_P,
	_P,	_L|_X,	_L|_X,	_L|_X,	_L|_X,	_L|_X,	_L|_X,	_L,
	_L,	_L,	_L,	_L,	_L,	_L,	_L,	_L,
	_L,	_L,	_L,	_L,	_L,	_L,	_L,	_L,
	_L,	_L,	_L,	_P,	_P,	_P,	_P,	_C,

	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C, /* 80 */
	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C, /* 88 */
	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C, /* 90 */
	_C,	_C,	_C,	_C,	_C,	_C,	_C,	_C, /* 98 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* A0 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* A8 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* B0 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* B8 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* C0 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* C8 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* D0 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* D8 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* E0 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* E8 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P, /* F0 */
	_P,	_P,	_P,	_P,	_P,	_P,	_P,	_P  /* F8 */
};
