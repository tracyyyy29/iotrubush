/*
 * libc/stdlib/strntoumax.c
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

#include <stddef.h>
#include <ctype.h>
#include <stdlib.h>

static inline int digitval(int ch)
{
    unsigned d;

    d = (unsigned)(ch - '0');
    if (d < 10) return (int)d;

    d = (unsigned)(ch - 'a');
    if (d < 6) return (int)(d+10);

    d = (unsigned)(ch - 'A');
    if (d < 6) return (int)(d+10);

    return -1;
}

uintmax_t strntoumax(const char * nptr, char ** endptr, int base, size_t n)
{
	const unsigned char * p = (const unsigned char *) nptr;
	const unsigned char * end = p + n;
	int minus = 0;
	uintmax_t v = 0;
	int d;

	/* skip leading space */
	while (p < end && isspace(*p))
		p++;

	/* Single optional + or - */
	if (p < end)
	{
		char c = p[0];
		if (c == '-' || c == '+')
		{
			minus = (c == '-');
			p++;
		}
	}

	if (base == 0)
	{
		if (p + 2 < end && p[0] == '0' && (p[1] == 'x' || p[1] == 'X'))
		{
			p += 2;
			base = 16;
		}
		else if (p + 1 < end && p[0] == '0')
		{
			p += 1;
			base = 8;
		}
		else
		{
			base = 10;
		}
	}
	else if (base == 16)
	{
		if (p + 2 < end && p[0] == '0' && (p[1] == 'x' || p[1] == 'X'))
		{
			p += 2;
		}
	}

	while (p < end && (d = digitval(*p)) >= 0 && d < base)
	{
		v = v * base + d;
		p += 1;
	}

	if (endptr)
		*endptr = (char *) p;

	return minus ? -v : v;
}
