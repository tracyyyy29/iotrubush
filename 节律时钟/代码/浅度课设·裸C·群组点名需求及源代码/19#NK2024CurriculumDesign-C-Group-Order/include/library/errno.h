/*
 * errno.h
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
#ifndef __ERRNO_H__
#define __ERRNO_H__

#ifdef __cplusplus
extern "C" {
#endif

enum {
	ENOERR			=  0,
	EDOM			= -1,
	ERANGE			= -2,
	ENOSYS			= -3,
	EINVAL			= -4,
	ESPIPE			= -5,
	EBADF			= -6,
	ENOMEM			= -7,
	EACCES			= -8,
	ENFILE			= -9,
	EMFILE			= -10,
	ENAMETOOLONG	= -11,
	ELOOP			= -12,
	ENOMSG			= -13,
	E2BIG			= -14,
	EINTR			= -15,
	EILSEQ			= -16,
	ENOEXEC			= -17,
	ENOENT			= -18,
	EPROTOTYPE		= -19,
	ESRCH			= -20,
	EPERM			= -21,
	ENOTDIR			= -22,
	ESTALE			= -23,
	EISDIR			= -24,
	EOPNOTSUPP		= -25,
	ENOTTY			= -26,
	EAGAIN			= -27,
	EIO				= -28,
	ENOSPC			= -29,
	EEXIST			= -30,
	EBUSY			= -31,
	EOVERFLOW		= -32,
};

/*
 * Internal function returning the address of the thread-specific errno
 */
extern volatile int * __runtime_errno_location(void);

/*
 * A macro expanding to the errno l-value
 */
#define errno 		(*__runtime_errno_location())

/*
 * Returns a string containing a message derived from an error code
 */
char * strerror(int num);

#ifdef __cplusplus
}
#endif

#endif /* __ERRNO_H__ */
