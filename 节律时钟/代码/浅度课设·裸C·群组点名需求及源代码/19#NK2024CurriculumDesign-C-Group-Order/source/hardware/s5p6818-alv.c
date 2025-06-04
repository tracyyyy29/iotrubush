/*
 * s5p6818-alv.c
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

#include <s5p6818-gpio.h>

struct s5p6818_gpiochip_alv_data_t
{
	physical_addr_t regbase;
};

static inline void s5p6818_gpiochip_alv_write_enable(struct gpiochip_t * chip)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	write32(phys_to_virt(dat->regbase + GPIOALV_PWRGATEREG), 0x1);
}

static inline void s5p6818_gpiochip_alv_write_disable(struct gpiochip_t * chip)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	write32(phys_to_virt(dat->regbase + GPIOALV_PWRGATEREG), 0x0);
}

static void s5p6818_gpiochip_alv_set_cfg(struct gpiochip_t * chip, int offset, int cfg)
{
}

static int s5p6818_gpiochip_alv_get_cfg(struct gpiochip_t * chip, int offset)
{
	return 0;
}

static void s5p6818_gpiochip_alv_set_pull(struct gpiochip_t * chip, int offset, enum gpio_pull_t pull)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	u32_t val = 0;

	if(offset >= chip->ngpio)
		return;

	s5p6818_gpiochip_alv_write_enable(chip);
	switch(pull)
	{
	case GPIO_PULL_UP:
		val |= 0x1 << offset;
		write32(phys_to_virt(dat->regbase + GPIOALV_PADPULLUPSETREG), val);
		break;

	case GPIO_PULL_DOWN:
		val |= 0x1 << offset;
		write32(phys_to_virt(dat->regbase + GPIOALV_PADPULLUPRSTREG), val);
		break;

	case GPIO_PULL_NONE:
		break;

	default:
		break;
	}
	s5p6818_gpiochip_alv_write_disable(chip);
}

static enum gpio_pull_t s5p6818_gpiochip_alv_get_pull(struct gpiochip_t * chip, int offset)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	u32_t val;

	if(offset >= chip->ngpio)
		return GPIO_PULL_NONE;

	val = read32(phys_to_virt(dat->regbase + GPIOALV_PADPULLUPREADREG));
	if(!((val >> offset) & 0x1))
		return GPIO_PULL_DOWN;
	else
		return GPIO_PULL_UP;

	return GPIO_PULL_NONE;
}

static void s5p6818_gpiochip_alv_set_drv(struct gpiochip_t * chip, int offset, enum gpio_drv_t drv)
{
}

static enum gpio_drv_t s5p6818_gpiochip_alv_get_drv(struct gpiochip_t * chip, int offset)
{
	return GPIO_DRV_LOW;
}

static void s5p6818_gpiochip_alv_set_rate(struct gpiochip_t * chip, int offset, enum gpio_rate_t rate)
{
}

static enum gpio_rate_t s5p6818_gpiochip_alv_get_rate(struct gpiochip_t * chip, int offset)
{
	return GPIO_RATE_SLOW;
}

static void s5p6818_gpiochip_alv_set_dir(struct gpiochip_t * chip, int offset, enum gpio_direction_t dir)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	u32_t val = 0;

	if(offset >= chip->ngpio)
		return;

	s5p6818_gpiochip_alv_write_enable(chip);
	switch(dir)
	{
	case GPIO_DIRECTION_INPUT:
		val |= 0x1 << offset;
		write32(phys_to_virt(dat->regbase + GPIOALV_PADOUTENBRSTREG), val);
		break;

	case GPIO_DIRECTION_OUTPUT:
		val |= 0x1 << offset;
		write32(phys_to_virt(dat->regbase + GPIOALV_PADOUTENBSETREG), val);
		break;

	default:
		break;
	}
	s5p6818_gpiochip_alv_write_disable(chip);
}

static enum gpio_direction_t s5p6818_gpiochip_alv_get_dir(struct gpiochip_t * chip, int offset)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	u32_t val, d;

	if(offset >= chip->ngpio)
		return GPIO_DIRECTION_UNKOWN;

	val = read32(phys_to_virt(dat->regbase + GPIOALV_PADOUTENBREADREG));
	d = (val >> offset) & 0x1;
	switch(d)
	{
	case 0x0:
		return GPIO_DIRECTION_INPUT;
	case 0x1:
		return GPIO_DIRECTION_OUTPUT;
	default:
		break;
	}
	return GPIO_DIRECTION_UNKOWN;
}

static void s5p6818_gpiochip_alv_set_value(struct gpiochip_t * chip, int offset, int value)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	u32_t val = 0;

	if(offset >= chip->ngpio)
		return;

	s5p6818_gpiochip_alv_write_enable(chip);
	if(value)
	{
		val |= 0x1 << offset;
		write32(phys_to_virt(dat->regbase + GPIOALV_PADOUTSETREG), val);
	}
	else
	{
		val |= 0x1 << offset;
		write32(phys_to_virt(dat->regbase + GPIOALV_PADOUTRSTREG), val);
	}
	s5p6818_gpiochip_alv_write_disable(chip);
}

static int s5p6818_gpiochip_alv_get_value(struct gpiochip_t * chip, int offset)
{
	struct s5p6818_gpiochip_alv_data_t * dat = (struct s5p6818_gpiochip_alv_data_t *)chip->priv;
	u32_t val;

	if(offset >= chip->ngpio)
		return 0;

	val = read32(phys_to_virt(dat->regbase + GPIOALV_GPIOINPUTVALUE));
	return !!(val & (1 << offset));
}

static struct s5p6818_gpiochip_alv_data_t gpiochip_alv_data = {
	.regbase	= S5P6818_GPIOALV_BASE,
};

struct gpiochip_t gpiochip_alv = {
	.name		= "GPIOALV",
	.base		= S5P6818_GPIOALV(0),
	.ngpio		= 6,
	.set_cfg	= s5p6818_gpiochip_alv_set_cfg,
	.get_cfg	= s5p6818_gpiochip_alv_get_cfg,
	.set_pull	= s5p6818_gpiochip_alv_set_pull,
	.get_pull	= s5p6818_gpiochip_alv_get_pull,
	.set_drv	= s5p6818_gpiochip_alv_set_drv,
	.get_drv	= s5p6818_gpiochip_alv_get_drv,
	.set_rate	= s5p6818_gpiochip_alv_set_rate,
	.get_rate	= s5p6818_gpiochip_alv_get_rate,
	.set_dir	= s5p6818_gpiochip_alv_set_dir,
	.get_dir	= s5p6818_gpiochip_alv_get_dir,
	.set_value	= s5p6818_gpiochip_alv_set_value,
	.get_value	= s5p6818_gpiochip_alv_get_value,
	.priv		= &gpiochip_alv_data,
};

void s5p6818_gpiochip_alv_init(void)
{
	register_gpiochip(&gpiochip_alv);
}

void s5p6818_gpiochip_alv_exit(void)
{
	unregister_gpiochip(&gpiochip_alv);
}
