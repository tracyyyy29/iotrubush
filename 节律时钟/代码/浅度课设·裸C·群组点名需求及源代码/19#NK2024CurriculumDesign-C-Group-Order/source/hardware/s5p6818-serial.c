/*
 * s5p6818-serial.c
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

#include <s5p6818-serial.h>

static void s5p6818_serial_init(int ch)
{
	switch(ch)
	{
	case 0:
		s5p6818_ip_reset(RESET_ID_UART0, 0);
		clk_enable("GATE-UART0");
		gpio_set_cfg(S5P6818_GPIOD(18), 0x1);
		gpio_set_cfg(S5P6818_GPIOD(14), 0x1);
		gpio_set_direction(S5P6818_GPIOD(18), GPIO_DIRECTION_OUTPUT);
		gpio_set_direction(S5P6818_GPIOD(14), GPIO_DIRECTION_INPUT);
		break;

	case 1:
		s5p6818_ip_reset(RESET_ID_UART1, 0);
		clk_enable("GATE-UART1");
		gpio_set_cfg(S5P6818_GPIOD(19), 0x1);
		gpio_set_cfg(S5P6818_GPIOD(15), 0x1);
		gpio_set_direction(S5P6818_GPIOD(19), GPIO_DIRECTION_OUTPUT);
		gpio_set_direction(S5P6818_GPIOD(15), GPIO_DIRECTION_INPUT);
		break;

	case 2:
		s5p6818_ip_reset(RESET_ID_UART2, 0);
		clk_enable("GATE-UART2");
		gpio_set_cfg(S5P6818_GPIOD(20), 0x1);
		gpio_set_cfg(S5P6818_GPIOD(16), 0x1);
		gpio_set_direction(S5P6818_GPIOD(20), GPIO_DIRECTION_OUTPUT);
		gpio_set_direction(S5P6818_GPIOD(16), GPIO_DIRECTION_INPUT);
		break;

	case 3:
		s5p6818_ip_reset(RESET_ID_UART3, 0);
		clk_enable("GATE-UART3");
		gpio_set_cfg(S5P6818_GPIOD(21), 0x1);
		gpio_set_cfg(S5P6818_GPIOD(17), 0x1);
		gpio_set_direction(S5P6818_GPIOD(21), GPIO_DIRECTION_OUTPUT);
		gpio_set_direction(S5P6818_GPIOD(17), GPIO_DIRECTION_INPUT);
		break;

	default:
		return;
	}

	s5p6818_serial_setup(ch, B115200, DATA_BITS_8, PARITY_NONE, STOP_BITS_1);
}

void s5p6818_serial_initial(void)
{
	s5p6818_serial_init(0);
	s5p6818_serial_init(1);
	s5p6818_serial_init(2);
	s5p6818_serial_init(3);
}

bool_t s5p6818_serial_setup(int ch, enum baud_rate_t baud, enum data_bits_t data, enum parity_bits_t parity, enum stop_bits_t stop)
{
	u32_t base = S5P6818_UART0_BASE;
	const u32_t udivslot_code[16] = {0x0000, 0x0080, 0x0808, 0x0888,
									 0x2222, 0x4924, 0x4a52, 0x54aa,
									 0x5555, 0xd555, 0xd5d5, 0xddd5,
									 0xdddd, 0xdfdd, 0xdfdf, 0xffdf};
	u32_t ibaud, baud_div_reg, baud_divslot_reg;
	u8_t data_bit_reg, parity_reg, stop_bit_reg;
	u64_t rate;

	switch(ch)
	{
	case 0:
		base = S5P6818_UART0_BASE;
		break;
	case 1:
		base = S5P6818_UART1_BASE;
		break;
	case 2:
		base = S5P6818_UART2_BASE;
		break;
	case 3:
		base = S5P6818_UART3_BASE;
		break;
	default:
		break;
	}

	switch(baud)
	{
	case B50:
		ibaud = 50;
		break;
	case B75:
		ibaud = 75;
		break;
	case B110:
		ibaud = 110;
		break;
	case B134:
		ibaud = 134;
		break;
	case B200:
		ibaud = 200;
		break;
	case B300:
		ibaud = 300;
		break;
	case B600:
		ibaud = 600;
		break;
	case B1200:
		ibaud = 1200;
		break;
	case B1800:
		ibaud = 1800;
		break;
	case B2400:
		ibaud = 2400;
		break;
	case B4800:
		ibaud = 4800;
		break;
	case B9600:
		ibaud = 9600;
		break;
	case B19200:
		ibaud = 19200;
		break;
	case B38400:
		ibaud = 38400;
		break;
	case B57600:
		ibaud = 57600;
		break;
	case B76800:
		ibaud = 76800;
		break;
	case B115200:
		ibaud = 115200;
		break;
	case B230400:
		ibaud = 230400;
		break;
	case B380400:
		ibaud = 380400;
		break;
	case B460800:
		ibaud = 460800;
		break;
	case B921600:
		ibaud = 921600;
		break;
	default:
		return FALSE;
	}

	switch(data)
	{
	case DATA_BITS_5:
		data_bit_reg = 0x0;
		break;
	case DATA_BITS_6:
		data_bit_reg = 0x1;
		break;
	case DATA_BITS_7:
		data_bit_reg = 0x2;
		break;
	case DATA_BITS_8:
		data_bit_reg = 0x3;
		break;
	default:
		return FALSE;
	}

	switch(parity)
	{
	case PARITY_NONE:
		parity_reg = 0x0;
		break;
	case PARITY_EVEN:
		parity_reg = 0x2;
		break;
	case PARITY_ODD:
		parity_reg = 0x1;
		break;
	default:
		return FALSE;
	}

	switch(stop)
	{
	case STOP_BITS_1:
		stop_bit_reg = 0;		break;
	case STOP_BITS_1_5:
		return -1;
	case STOP_BITS_2:
		stop_bit_reg = 1;		break;
	default:
		return -1;
	}

	switch(ch)
	{
	case 0:
		rate = clk_get_rate("GATE-UART0");
		break;
	case 1:
		rate = clk_get_rate("GATE-UART1");
		break;
	case 2:
		rate = clk_get_rate("GATE-UART2");
		break;
	case 3:
		rate = clk_get_rate("GATE-UART3");
		break;
	default:
		return FALSE;
	}

	baud_div_reg = (u32_t)((rate / (ibaud * 16))) - 1;
	baud_divslot_reg = udivslot_code[( (u32_t)((rate % (ibaud*16)) / ibaud) ) & 0xf];

	write32(base + UART_UBRDIV, baud_div_reg);
	write32(base + UART_UFRACVAL, baud_divslot_reg);
	write32(base + UART_ULCON, (data_bit_reg<<0 | stop_bit_reg<<2 | parity_reg<<3));

	return TRUE;
}

ssize_t s5p6818_serial_read(int ch, u8_t * buf, size_t count)
{
	u32_t base = S5P6818_UART0_BASE;
	ssize_t i;

	switch(ch)
	{
	case 0:
		base = S5P6818_UART0_BASE;
		break;
	case 1:
		base = S5P6818_UART1_BASE;
		break;
	case 2:
		base = S5P6818_UART2_BASE;
		break;
	case 3:
		base = S5P6818_UART3_BASE;
		break;
	default:
		break;
	}

	for(i = 0; i < count; i++)
	{
		if( (read32(phys_to_virt(base + UART_UTRSTAT)) & UART_UTRSTAT_RXDR) )
			buf[i] = read8(phys_to_virt(base + UART_URXH));
		else
			break;
	}

	return i;
}

ssize_t s5p6818_serial_write(int ch, u8_t * buf, size_t count)
{
	u32_t base = S5P6818_UART0_BASE;
	ssize_t i;

	switch(ch)
	{
	case 0:
		base = S5P6818_UART0_BASE;
		break;
	case 1:
		base = S5P6818_UART1_BASE;
		break;
	case 2:
		base = S5P6818_UART2_BASE;
		break;
	case 3:
		base = S5P6818_UART3_BASE;
		break;
	default:
		break;
	}

	for(i = 0; i < count; i++)
	{
		while( !(read32(phys_to_virt(base + UART_UTRSTAT)) & UART_UTRSTAT_TXFE) );
		write8(phys_to_virt(base + UART_UTXH), buf[i]);
	}

	return i;
}

ssize_t s5p6818_serial_write_string(int ch, const char * buf)
{
	return s5p6818_serial_write(ch, (u8_t *)buf, strlen(buf));
}
