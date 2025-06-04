/*
 * libc/string/strnstr.c
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

#include <types.h>
#include <stddef.h>
#include <string.h>

/*
 * Locate a substring in a strin
 */
char * strnstr(const char * s1, const char * s2, size_t n)
{
	size_t l2;

	l2 = strlen(s2);
	if (!l2)
		return (char *)s1;

	while (n >= l2)
	{
		n--;
		if (!memcmp(s1, s2, l2))
			return (char *)s1;
		s1++;
	}

	return NULL;
}
