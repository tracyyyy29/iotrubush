/*
 * string.h
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

#ifndef __STRING_H__
#define __STRING_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

/*
 * String manipulation
 */
char * strcpy(char * dest, const char * src);
char * strncpy(char * dest, const char * src, size_t n);
size_t strlcpy(char * dest, const char * src, size_t n);
char * strcat(char * dest, const char * src);
char * strncat(char * dest, const char * src, size_t n);
size_t strlcat(char * dest, const char * src, size_t n);

/*
 * String examination
 */
size_t strlen(const char * s);
size_t strnlen(const char * s, size_t n);
int strcmp(const char * s1, const char * s2);
int strncmp(const char * s1, const char * s2, size_t n);
int strnicmp(const char * s1, const char * s2, size_t n);
int strcasecmp(const char * s1, const char * s2);
int strncasecmp(const char * s1, const char * s2, size_t n);
int strcoll(const char * s1, const char * s2);
char * strchr(const char * s, int c);
char * strrchr(const char * s, int c);
char * strnchr(const char * s, size_t n, int c);
size_t strspn(const char * s, const char * accept);
size_t strcspn(const char * s, const char * reject);
char * strpbrk(const char * s1, const char * s2);
char * strstr(const char * s1, const char * s2);
char * strnstr(const char * s1, const char * s2, size_t n);
char * strdup(const char * s);
char * strsep(char ** s, const char * ct);

/*
 * Memory manipulation
 */
void * memset(void * s, int c, size_t n);
void * memcpy(void * dest, const void * src, size_t len);
void * memmove(void * dest, const void * src, size_t n);
void * memchr(const void * s, int c, size_t n);
void * memscan(void * addr, int c, size_t size);
int memcmp(const void * s1, const void * s2, size_t n);

/*
 * ffs - find first (least-significant) bit set
 */
static inline __attribute__((always_inline)) int ffs(int x)
{
	return __builtin_ffs(x);
}

/*
 * fls - find last (most-significant) bit set
 * Note fls(0) = 0, fls(1) = 1, fls(0x80000000) = 32.
 */
static inline __attribute__((always_inline)) int fls(int x)
{
	return x ? sizeof(x) * 8 - __builtin_clz(x) : 0;
}

/*
 * __ffs - find first bit in word.
 * Undefined if no bit exists, so code should check against 0 first.
 */
static inline __attribute__((always_inline)) unsigned long __ffs(unsigned long word)
{
	return __builtin_ctzl(word);
}

/*
 * __fls - find last (most-significant) set bit in a long word
 * Undefined if no set bit exists, so code should check against 0 first.
 */
static inline __attribute__((always_inline)) unsigned long __fls(unsigned long word)
{
	return (sizeof(word) * 8) - 1 - __builtin_clzl(word);
}

#ifdef __cplusplus
}
#endif

#endif /* __STRING_H__ */
