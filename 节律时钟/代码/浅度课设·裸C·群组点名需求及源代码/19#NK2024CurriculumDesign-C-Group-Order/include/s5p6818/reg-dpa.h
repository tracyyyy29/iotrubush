/*
 * reg-dpa.h
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
#ifndef __S5P6818_REG_DPA_H__
#define __S5P6818_REG_DPA_H__

#define S5P6818_DPA_BASE		(0x00000000)

#define DPA_HDMI_MUXCTRL		(0x1004)
#define DPA_LVDS_MUXCTRL		(0x100c)
#define DPA_HDMI_SYNCCTRL0		(0x1014)
#define DPA_HDMI_SYNCCTRL1		(0x1018)
#define DPA_HDMI_SYNCCTRL2		(0x101c)
#define DPA_HDMI_SYNCCTRL3		(0x1020)
#define DPA_TFT_MUXCTRL			(0x1024)

#endif /* __S5P6818_REG_DPA_H__ */
