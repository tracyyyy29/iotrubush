/*
 * endian.h
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
#ifndef __ARM32_ENDIAN_H__
#define __ARM32_ENDIAN_H__

#ifdef __cplusplus
extern "C" {
#endif

#if ( !defined(__LITTLE_ENDIAN) && !defined(__BIG_ENDIAN) )
# define __LITTLE_ENDIAN
#endif

#if defined(__LITTLE_ENDIAN)
# define LITTLE_ENDIAN	1234
# define BIG_ENDIAN		4321
# define BYTE_ORDER		LITTLE_ENDIAN
#elif defined(__BIG_ENDIAN)
# define LITTLE_ENDIAN	1234
# define BIG_ENDIAN		4321
# define BYTE_ORDER		BIG_ENDIAN
#else
# error "Unknown byte order!"
#endif

#ifdef __cplusplus
}
#endif

#endif /* __ARM32_ENDIAN_H__ */
