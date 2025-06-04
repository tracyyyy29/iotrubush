/*
 * interrupt.h
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

#ifndef __INTERRUPT_H__
#define __INTERRUPT_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <stddef.h>
#include <malloc.h>
#include <stdlib.h>
#include <string.h>
#include <io.h>
#include <list.h>

enum irq_type_t {
	IRQ_TYPE_NONE			= 0,
	IRQ_TYPE_LEVEL_LOW		= 1,
	IRQ_TYPE_LEVEL_HIGH		= 2,
	IRQ_TYPE_EDGE_FALLING	= 3,
	IRQ_TYPE_EDGE_RISING	= 4,
	IRQ_TYPE_EDGE_BOTH		= 5,
};

struct irq_handler_t {
	void (*func)(void * data);
	void * data;
};

struct irq_t {
	const char * name;
	const int no;
	struct irq_handler_t * handler;

	void (*enable)(struct irq_t * irq);
	void (*disable)(struct irq_t * irq);
	void (*set_type)(struct irq_t * irq, enum irq_type_t type);
};

bool_t irq_register(struct irq_t * irq);
bool_t irq_unregister(struct irq_t * irq);
bool_t request_irq(const char * name, void (*func)(void *), enum irq_type_t type, void * data);
bool_t free_irq(const char * name);
void enable_irq(const char * name);
void disable_irq(const char * name);

#ifdef __cplusplus
}
#endif

#endif /* __INTERRUPT_H__ */
