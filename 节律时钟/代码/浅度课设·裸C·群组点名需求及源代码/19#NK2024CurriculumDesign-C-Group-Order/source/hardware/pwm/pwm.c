/*
 * driver/pwm/pwm.c
 *
 * Copyright(c) 2007-2015 Jianjun Jiang <8192542@qq.com>
 * Official site: http://xboot.org
 * Mobile phone: +86-18665388956
 * QQ: 8192542
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

#include <pwm/pwm.h>

struct pwm_list_t __pwm_list = {
	.entry = {
		.next	= &(__pwm_list.entry),
		.prev	= &(__pwm_list.entry),
	},
};

struct pwm_t * search_pwm(const char * name)
{
	struct pwm_list_t * pos, * n;

	if(!name)
		return NULL;

	list_for_each_entry_safe(pos, n, &(__pwm_list.entry), entry)
	{
		if(strcmp(pos->pwm->name, name) == 0)
			return pos->pwm;
	}

	return NULL;
}

bool_t register_pwm(struct pwm_t * pwm)
{
	struct pwm_list_t * pl;

	if(!pwm || !pwm->name)
		return FALSE;

	if(search_pwm(pwm->name))
		return FALSE;

	pl = malloc(sizeof(struct pwm_list_t));
	if(!pl)
		return FALSE;

	pwm->__enable = FALSE;
	pwm->__duty = 0;
	pwm->__period = 0;
	pwm->__polarity = FALSE;
	pl->pwm = pwm;
	list_add_tail(&pl->entry, &(__pwm_list.entry));

	return TRUE;
}

bool_t unregister_pwm(struct pwm_t * pwm)
{
	struct pwm_list_t * pos, * n;

	if(!pwm || !pwm->name)
		return FALSE;

	list_for_each_entry_safe(pos, n, &(__pwm_list.entry), entry)
	{
		if(pos->pwm == pwm)
		{
			list_del(&(pos->entry));
			free(pos);
			return TRUE;
		}
	}

	return FALSE;
}

void pwm_config(struct pwm_t * pwm, u32_t duty, u32_t period, bool_t polarity)
{
	if(pwm && pwm->config)
	{
		if(duty > period)
			duty = period;
		if((pwm->__duty != duty) || (pwm->__period != period) || (pwm->__polarity != polarity))
		{
			pwm->config(pwm, duty, period, polarity);
			pwm->__duty = duty;
			pwm->__period = period;
			pwm->__polarity = polarity;
		}
	}
}

void pwm_enable(struct pwm_t * pwm)
{
	if(pwm && pwm->enable && !pwm->__enable)
	{
		pwm->enable(pwm);
		pwm->__enable = TRUE;
	}
}

void pwm_disable(struct pwm_t * pwm)
{
	if(pwm && pwm->disable && pwm->__enable)
	{
		pwm->disable(pwm);
		pwm->__enable = FALSE;
	}
}
