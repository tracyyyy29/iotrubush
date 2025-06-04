/*
 * clk-divider.h
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
#ifndef __CLK_DIVIDER_H__
#define __CLK_DIVIDER_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <stddef.h>
#include <malloc.h>
#include <stdlib.h>
#include <io.h>
#include <list.h>

enum clk_divider_type_t {
	CLK_DIVIDER_ZERO_BASED		= 0x0,
	CLK_DIVIDER_ONE_BASED		= 0x1,
};

struct clk_divider_t {
	const char * name;
	const char * parent;
	physical_addr_t reg;
	enum clk_divider_type_t type;
	int shift;
	int width;
};

bool_t clk_divider_register(struct clk_divider_t * dclk);
bool_t clk_divider_unregister(struct clk_divider_t * dclk);

#ifdef __cplusplus
}
#endif

#endif /* __CLK_DIVIDER_H__ */
