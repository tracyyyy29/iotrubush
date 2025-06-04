/*
 * reg-i2c.h
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
#ifndef __S5P6818_REG_I2C_H__
#define __S5P6818_REG_I2C_H__

#define S5P6818_I2C0_BASE	(0xC00A4000)
#define S5P6818_I2C1_BASE	(0xC00A5000)
#define S5P6818_I2C2_BASE	(0xC00A6000)

#define I2CCON				(0x00)
#define I2CSTAT				(0x04)
#define I2CADD				(0x08)
#define I2CDS				(0x0C)
#define I2CLC				(0x10)
#define I2CVR				(0x40)

#endif /* __S5P6818_REG_I2C_H__ */
