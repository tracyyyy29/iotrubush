/*
 * stdarg.h
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
#ifndef __STDARG_H__
#define __STDARG_H__

#ifdef __cplusplus
extern "C" {
#endif

typedef __builtin_va_list	va_list;

/*
 * prepare to access variable args
 */
#define va_start(v, l)		__builtin_va_start(v, l)

/*
 * the caller will get the value of current argument
 */
#define va_arg(v, l)		__builtin_va_arg(v, l)

/*
 * end for variable args
 */
#define va_end(v)			__builtin_va_end(v)

/*
 * copy variable args
 */
#define va_copy(d, s)		__builtin_va_copy(d, s)

#ifdef __cplusplus
}
#endif

#endif /* __STDARG_H__ */
