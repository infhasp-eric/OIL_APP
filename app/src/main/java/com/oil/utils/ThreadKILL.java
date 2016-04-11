package com.oil.utils;
/**
 * 杀死线程
 * @author Administrator
 *
 */
public class ThreadKILL {

	public static void  killthread(Thread thread) {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
			thread = null;
		}
	}
}
