/*
 * s5p6818-serial.h
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
#ifndef __S5P6818_SERIAL_H__
#define __S5P6818_SERIAL_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>
#include <types.h>
#include <string.h>
#include <io.h>
#include <stddef.h>
#include <s5p6818/reg-uart.h>
#include <s5p6818-gpio.h>
#include <s5p6818-clk.h>

enum baud_rate_t {
	B50,
	B75,
	B110,
	B134,
	B200,
	B300,
	B600,
	B1200,
	B1800,
	B2400,
	B4800,
	B9600,
	B19200,
	B38400,
	B57600,
	B76800,
	B115200,
	B230400,
	B380400,
	B460800,
	B921600
};

enum data_bits_t {
	DATA_BITS_5,
	DATA_BITS_6,
	DATA_BITS_7,
	DATA_BITS_8,
};

enum parity_bits_t {
	PARITY_NONE,
	PARITY_EVEN,
	PARITY_ODD,
};

enum stop_bits_t {
	STOP_BITS_1,
	STOP_BITS_1_5,
	STOP_BITS_2,
};

void s5p6818_serial_initial(void);
bool_t s5p6818_serial_setup(int ch, enum baud_rate_t baud, enum data_bits_t data, enum parity_bits_t parity, enum stop_bits_t stop);
ssize_t s5p6818_serial_read(int ch, u8_t * buf, size_t count);
ssize_t s5p6818_serial_write(int ch, u8_t * buf, size_t count);
ssize_t s5p6818_serial_write_string(int ch, const char * buf);

#ifdef __cplusplus
}
#endif

#endif /* __S5P6818_SERIAL_H__ */
