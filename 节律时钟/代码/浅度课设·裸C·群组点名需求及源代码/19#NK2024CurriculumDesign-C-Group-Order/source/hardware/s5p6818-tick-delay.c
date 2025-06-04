/*
 * s5p6818-tick-delay.c
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

#include <s5p6818-tick-delay.h>

static volatile u32_t loops_per_jiffy = 0;

void __attribute__ ((noinline)) __delay(volatile u32_t loop)
{
	for(; loop > 0; loop--);
}

void udelay(u32_t us)
{
	u32_t hz = get_system_hz();

	if(hz)
		__delay(us * loops_per_jiffy / (1000000 / hz));
	else
		__delay(us);
}

void mdelay(u32_t ms)
{
	u32_t hz = get_system_hz();

	if(hz)
		__delay(ms * loops_per_jiffy / (1000 / hz));
	else
		__delay(ms * 1000);
}

static void calibrate_delay(void)
{
	u32_t ticks, loopbit;
	s32_t lps_precision = 8;
	u32_t hz = get_system_hz();

	if(hz > 0)
	{
		loops_per_jiffy = (1<<12);

		while((loops_per_jiffy <<= 1) != 0)
		{
			/* wait for "start of" clock tick */
			ticks = jiffies;
			while (ticks == jiffies);

			/* go ... */
			ticks = jiffies;
			__delay(loops_per_jiffy);
			ticks = jiffies - ticks;

			if(ticks)
				break;
		}

		loops_per_jiffy >>= 1;
		loopbit = loops_per_jiffy;

		while(lps_precision-- && (loopbit >>= 1))
		{
			loops_per_jiffy |= loopbit;
			ticks = jiffies;
			while(ticks == jiffies);

			ticks = jiffies;
			__delay(loops_per_jiffy);

			/* longer than 1 tick */
			if(jiffies != ticks)
				loops_per_jiffy &= ~loopbit;
		}
	}
	else
	{
		loops_per_jiffy = 0;
	}
}

void s5p6818_tick_delay_initial(void)
{
	calibrate_delay();
}
