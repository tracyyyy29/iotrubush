/*
 * hw-key.h
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
#ifndef __HW_KEY_H__
#define __HW_KEY_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

enum {
	KEY_NAME_VOL_UP		= 0x1 << 0,
	KEY_NAME_VOL_DOWN	= 0x1 << 1,
};

void key_initial(void);
bool_t get_key_status(u32_t * key);
bool_t get_key_event(u32_t * keyup, u32_t * keydown);

#ifdef __cplusplus
}
#endif

#endif /* __HW_KEY_H__ */
