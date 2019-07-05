package com.moranc.utils;

import android.util.Log;

/**
 * Android Log Utils
 * @description:
 *   ①. 打印日志，直接填写需要打印的内容，不需要添加"tag"参数
 *   ②. 可调整日志的打印级别，日志级别分为：verbose、debug、info、warn、error、assert、none(不打印所有级别日志)
 */
public class LogUtils {
	public static String customTagPrefix = "";
	
	public static final int LEVEL_NONE = 7;
	public static final int LEVEL_V = 1;
	public static final int LEVEL_D = 2;
	public static final int LEVEL_I = 3;
	public static final int LEVEL_W = 4;
	public static final int LEVEL_E = 5;
	public static final int LEVEL_WTF = 6;

	private static int sLevel = LEVEL_D;

	public static LogUtils.CustomLogger customLogger;

	public LogUtils() {
	}


	/**
	 * 配置日志打印级别
	 * @pay_attention：不调用此方法，默认打印Debug及以上级别日志
	 * @param logLevel  取值范围{LEVEL_NONE = 7, LEVEL_V = 1, LEVEL_D = 2, LEVEL_I = 3,
	 * LEVEL_W = 4, LEVEL_E = 5, LEVEL_WTF = 6} </br>
	 * 其中LEVEL_NONE表示不打印日志
	 */
	public static void configPrint(int logLevel) {
		if (logLevel < LEVEL_V || logLevel > LEVEL_WTF) {
			sLevel = LEVEL_NONE;
		}else {
			sLevel = logLevel;
		}
	}

	private static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

	private static String generateTag(StackTraceElement caller) {
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
		tag = isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
		return tag;
	}

	public static void d(String content) {
		if (LEVEL_D >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.d(tag, content);
			} else {
				Log.d(tag, content);
			}
		}
	}

	public static void d(String content, Throwable tr) {
		if (LEVEL_D >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.d(tag, content, tr);
			} else {
				Log.d(tag, content, tr);
			}

		}
	}

	public static void e(String content) {
		if (LEVEL_E >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.e(tag, content);
			} else {
				Log.e(tag, content);
			}

		}
	}

	public static void e(String content, Throwable tr) {
		if (LEVEL_E >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.e(tag, content, tr);
			} else {
				Log.e(tag, content, tr);
			}

		}
	}

	public static void i(String content) {
		if (LEVEL_I >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.i(tag, content);
			} else {
				Log.i(tag, content);
			}

		}
	}

	public static void i(String content, Throwable tr) {
		if (LEVEL_I >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.i(tag, content, tr);
			} else {
				Log.i(tag, content, tr);
			}

		}
	}

	public static void v(String content) {
		if (LEVEL_V >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.v(tag, content);
			} else {
				Log.v(tag, content);
			}

		}
	}

	public static void v(String content, Throwable tr) {
		if (LEVEL_V >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.v(tag, content, tr);
			} else {
				Log.v(tag, content, tr);
			}

		}
	}

	public static void w(String content) {
		if (LEVEL_W >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.w(tag, content);
			} else {
				Log.w(tag, content);
			}

		}
	}

	public static void w(String content, Throwable tr) {
		if (LEVEL_W >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.w(tag, content, tr);
			} else {
				Log.w(tag, content, tr);
			}

		}
	}

	public static void w(Throwable tr) {
		if (LEVEL_W >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.w(tag, tr);
			} else {
				Log.w(tag, tr);
			}

		}
	}

	public static void wtf(String content) {
		if (LEVEL_WTF >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.wtf(tag, content);
			} else {
				Log.wtf(tag, content);
			}

		}
	}

	public static void wtf(String content, Throwable tr) {
		if (LEVEL_WTF >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.wtf(tag, content, tr);
			} else {
				Log.wtf(tag, content, tr);
			}

		}
	}

	public static void wtf(Throwable tr) {
		if (LEVEL_WTF >= sLevel) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			if (customLogger != null) {
				customLogger.wtf(tag, tr);
			} else {
				Log.wtf(tag, tr);
			}

		}
	}

	public interface CustomLogger {
		void d(String var1, String var2);

		void d(String var1, String var2, Throwable var3);

		void e(String var1, String var2);

		void e(String var1, String var2, Throwable var3);

		void i(String var1, String var2);

		void i(String var1, String var2, Throwable var3);

		void v(String var1, String var2);

		void v(String var1, String var2, Throwable var3);

		void w(String var1, String var2);

		void w(String var1, String var2, Throwable var3);

		void w(String var1, Throwable var2);

		void wtf(String var1, String var2);

		void wtf(String var1, String var2, Throwable var3);

		void wtf(String var1, Throwable var2);
	}

	private static boolean isEmpty(String content) {
		return content == null || "".equals(content);
	}
}
