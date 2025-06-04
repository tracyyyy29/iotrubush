/*
 * s5p6818-serial-stdio.c
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

#include <malloc.h>
#include <stdio.h>
#include <sizes.h>
#include <s5p6818-serial-stdio.h>
#include <s5p6818-serial.h>

int serial_printf(int ch, const char * fmt, ...)
{
	va_list ap;
	char * buf;
	int len;
	int rv;

	va_start(ap, fmt);
	len = vsnprintf(NULL, 0, fmt, ap);
	if(len < 0)
		return 0;
	buf = malloc(len + 1);
	if(!buf)
		return 0;
	rv = vsnprintf(buf, len + 1, fmt, ap);
	va_end(ap);

	rv = (s5p6818_serial_write_string(ch, buf) < 0) ? 0 : rv;
	free(buf);

	return rv;
}
