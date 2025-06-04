/*
 * libc/stdlib/strtod.c
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
#include <errno.h>
#include <limits.h>
#include <stdlib.h>

/*
 * Convert string to double
 */
double strtod(const char * nptr, char ** endptr)
{
	double number;
	int exponent;
	int negative;
	char * p = (char *)nptr;
	double p10;
	int n;
	int num_digits;
	int num_decimals;

	/* Skip leading whitespace */
	while(isspace(*p))
		p++;

	/* Handle optional sign */
	negative = 0;
	switch(*p)
	{
	case '-':
		negative = 1;
		p++;
		break;
	case '+':
		p++;
		break;
	}

	number = 0.;
	exponent = 0;
	num_digits = 0;
	num_decimals = 0;

	/* Process string of digits */
	while(isdigit(*p))
	{
		number = number * 10. + (*p - '0');
		p++;
		num_digits++;
	}

	/* Process decimal part */
	if(*p == '.')
	{
		p++;

		while(isdigit(*p))
		{
			number = number * 10. + (*p - '0');
			p++;
			num_digits++;
			num_decimals++;
		}

		exponent -= num_decimals;
	}

	if(num_digits == 0)
	{
		errno = ERANGE;
		return 0.0;
	}

	/* Correct for sign */
	if(negative)
		number = -number;

	/* Process an exponent string */
	if(*p == 'e' || *p == 'E')
	{
		/* Handle optional sign */
		negative = 0;
		switch(*++p)
		{
		case '-':
			negative = 1;
			p++;
			break;
		case '+':
			p++;
			break;
		}

		/* Process string of digits */
		n = 0;
		while(isdigit(*p))
		{
			n = n * 10 + (*p - '0');
			p++;
		}

		if(negative)
			exponent -= n;
		else
			exponent += n;
	}

	if(exponent < -307 || exponent > 308)
	{
		errno = ERANGE;
		return 0.0;
	}

	/* Scale the result */
	p10 = 10.;
	n = exponent;
	if(n < 0)
		n = -n;
	while(n)
	{
		if(n & 1)
		{
			if (exponent < 0)
				number /= p10;
			else
				number *= p10;
		}
		n >>= 1;
		p10 *= p10;
	}

	if(endptr)
		*endptr = p;

	return number;
}
