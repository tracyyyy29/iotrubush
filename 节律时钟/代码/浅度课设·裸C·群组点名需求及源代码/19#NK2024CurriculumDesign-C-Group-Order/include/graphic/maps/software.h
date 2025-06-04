/*
 * software.h
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

#ifndef __GRAPHIC_MAPS_SOFTWARE_H__
#define __GRAPHIC_MAPS_SOFTWARE_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <graphic/surface.h>

inline u8_t * surface_sw_get_pointer(struct surface_t * surface, s32_t x, s32_t y);
inline u32_t surface_sw_get_pixel(struct surface_t * surface, s32_t x, s32_t y);
inline void surface_sw_set_pixel(struct surface_t * surface, s32_t x, s32_t y, u32_t c);
inline void surface_sw_set_pixel_with_alpha(struct surface_t * surface, s32_t x, s32_t y, u32_t c);

bool_t map_software_point(struct surface_t * surface, s32_t x, s32_t y, u32_t c, enum blend_mode mode);
bool_t map_software_hline(struct surface_t * surface, s32_t x0, s32_t y0, u32_t x1, u32_t c, enum blend_mode mode);
bool_t map_software_vline(struct surface_t * surface, s32_t x0, s32_t y0, u32_t y1, u32_t c, enum blend_mode mode);
bool_t map_software_fill(struct surface_t * surface, struct rect_t * rect, u32_t c, enum blend_mode mode);
bool_t map_software_blit(struct surface_t * dst, struct rect_t * dst_rect, struct surface_t * src, struct rect_t * src_rect, enum blend_mode mode);
struct surface_t * map_software_scale(struct surface_t * surface, struct rect_t * rect, u32_t w, u32_t h);
struct surface_t * map_software_rotate(struct surface_t * surface, struct rect_t * rect, enum rotate_type type);
struct surface_t * map_software_transform(struct surface_t * surface);

#ifdef __cplusplus
}
#endif

#endif /* __GRAPHIC_MAPS_SOFTWARE_H__ */
