/*
 * maps.h
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

#ifndef __GRAPHIC_MAPS_H__
#define __GRAPHIC_MAPS_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <graphic/rect.h>

struct surface_t;
struct surface_maps;

enum blend_mode {
	BLEND_MODE_REPLACE	= 0x0,
	BLEND_MODE_ALPHA	= 0x1,
};

enum rotate_type {
	/* rotate 0 degrees */
    ROTATE_DEGREE_0		= 0x0,

    /* rotate 90 degrees */
    ROTATE_DEGREE_90	= 0x1,

    /* rotate 180 degrees */
    ROTATE_DEGREE_180	= 0x2,

    /* rotate 270 degrees */
    ROTATE_DEGREE_270	= 0x3,

    /* horizontally mirror */
    ROTATE_MIRROR_H 	= 0x4,

    /* vertically mirror */
    ROTATE_MIRROR_V 	= 0x5,
};

struct surface_maps {
	bool_t (*point)(struct surface_t * surface, s32_t x, s32_t y, u32_t c, enum blend_mode mode);
	bool_t (*hline)(struct surface_t * surface, s32_t x0, s32_t y0, u32_t x1, u32_t c, enum blend_mode mode);
	bool_t (*vline)(struct surface_t * surface, s32_t x0, s32_t y0, u32_t y1, u32_t c, enum blend_mode mode);
	bool_t (*fill)(struct surface_t * surface, struct rect_t * rect, u32_t c, enum blend_mode mode);
	bool_t (*blit)(struct surface_t * dst, struct rect_t * dst_rect, struct surface_t * src, struct rect_t * src_rect, enum blend_mode mode);
	struct surface_t * (*scale)(struct surface_t * surface, struct rect_t * rect, u32_t w, u32_t h);
	struct surface_t * (*rotate)(struct surface_t * surface, struct rect_t * rect, enum rotate_type type);
};

bool_t surface_set_maps(struct surface_maps * maps);

#ifdef __cplusplus
}
#endif

#endif /* __GRAPHIC_MAPS_H__ */
