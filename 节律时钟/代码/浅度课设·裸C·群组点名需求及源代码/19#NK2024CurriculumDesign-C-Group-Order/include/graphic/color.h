/*
 * color.h
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

#ifndef __COLOR_H__
#define __COLOR_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

struct color_t {
	u8_t r;
	u8_t g;
	u8_t b;
	u8_t a;
};

struct color_t * get_named_color(const char * name);
void color_init_rgb(struct color_t * color, u8_t r, u8_t g, u8_t b);
void color_init_rgba(struct color_t * color, u8_t r, u8_t g, u8_t b, u8_t a);
void color_init_colstr(struct color_t * color, const char * colstr);

#ifdef __cplusplus
}
#endif

#endif /* __COLOR_H__ */
