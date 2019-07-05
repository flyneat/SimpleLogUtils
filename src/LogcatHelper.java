package com.moranc.utils;

import com.moranc.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 将LogCat日志信息保存在外部指定logPath目录下
 * @author zpf
 */
public class LogcatHelper {
	private LogDumper mLogDumper;
	
	/**
	 * 日志文件保存天数
	 */
	private int mLogDuration;
	
	/**
	 * 应用进程号
	 */
	private int mPId; 
	

	/**
	 * 初始化目录
	 * */
	public void init(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 使用内部类，实现单例模式
	 */
	private static class InnerClass{
		private static final LogcatHelper INSTANCE = new LogcatHelper();
	}
	
	public static LogcatHelper getInstance() {
		return InnerClass.INSTANCE;
	}
	

	private LogcatHelper(){
		mPId = android.os.Process.myPid();
		// 默认保存最近的7天的日志文件
		mLogDuration = 7;
	}
	
	public void setLogDuration(int dayCount) {
		if(dayCount <= 0 || dayCount > 2 * 365) {
			mLogDuration = 7;
			return;
		}
		mLogDuration = dayCount;	
	}
	
	

	/**
	 * @param logPath
	 * @param level 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
	 */
	public void start(String logPath, String level) {
		if (mLogDumper == null) {
			mLogDumper = new LogDumper(logPath, level);
			mLogDumper.start();		
		} else {
			if(!mLogDumper.isAlive()){
				LogUtils.d("LogDumper is not alive");
				mLogDumper.start();
			}else{
				LogUtils.d("LogDumper is alive");
			}
		}
	}
	
	/**
	 * 默认打印debug级别日志
	 * @param logPath
	 */
	public void start(String logPath) {
		start(logPath, "*:d");
	}

	public void stop() {
		if (mLogDumper != null) {
			mLogDumper.stopLogs();
            mLogDumper = null;
		}
	}

	public boolean isAlive() {
		if (mLogDumper != null && mLogDumper.getState() != Thread.State.TERMINATED) {
			return true;
		}
		mLogDumper = null;
		return false;
	}
	

	/**
	 * 日志收集线程
	 * */
	private class LogDumper extends Thread {

		private Process logcatProc;
		private BufferedReader mReader = null;
		private volatile boolean mRunning = true;
		String cmds = null;
		private FileOutputStream out = null;

		public LogDumper(String dirPath, String level) {
			String logName = getLogName();
			String logDir = logName.substring(0, 6);
            init(dirPath + "/" + logDir);
            clearOutdatedLog(dirPath);
			try {
				out = new FileOutputStream(new File(dirPath + "/" + logDir, logName),true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			if(level == null || "".equals(level)){
				// 默认打印Verbose级别日志信息
				level = "*:v";
			}

			/**
			 * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s 
			 * v:Verbose d:Debug i:Info w:Warn e:Error f:Fatal s:Silent
			 * 根据项目调试需要在此调整日志输出级别,示例： cmds = logcat *:e *:w | grep "(mPID)
			 * 打印Error和Warn级别日志信息 cmds = logcat  | grep "(mPID)";
			 * //打印标签过滤信息
			 * 打印所有日志信息 cmds = logcat -s way 
			 */
			cmds = "logcat " + level + " | grep \"(" + mPId + ")\"";
		}

		public void stopLogs() {
			mRunning = false;
		}

		@Override
		public void run() {
			try {
				logcatProc = Runtime.getRuntime().exec(cmds);
				mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
				String line = null;
				while (mRunning && (line = mReader.readLine()) != null) {
					if (!mRunning) {
						break;
					}
					if (line.length() == 0) {
						continue;
					}
					if (out != null && line.contains(String.valueOf(mPId))) {
						out.write((getDateEN() + "  " + line + "\n").getBytes());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (logcatProc != null) {
					logcatProc.destroy();
					logcatProc = null;
				}
				if (mReader != null) {
					try {
						mReader.close();
						mReader = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					out = null;
				}
				if (mRunning) {
					LogcatHelper.this.stop();
				}
			}
		}
	}

    private String getLogName() {
       return new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".log";
    }

    private String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 清除过期日志文件
     * @param dirPath 日志目录
     */
    private void clearOutdatedLog(String logPath) {
        try {
            File pathFile = new File(logPath);
            File[] files = pathFile.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            Calendar beforeDay = Calendar.getInstance();
            beforeDay.add(Calendar.DAY_OF_MONTH, -mLogDuration);
            String upToDate = new SimpleDateFormat("yyyyMMdd").format(beforeDay.getTime());
            for (File file : files) {
                if (file.getName().compareTo(upToDate.substring(0,6)) < 0) {
                    deleteFile(file, true);
                } else if (file.getName().compareTo(upToDate) == 0) {
                    File[] logFiles = file.listFiles();
                    for (File logFile : logFiles) {
                        if (upToDate.compareTo(logFile.getName().replace(".log", "")) >= 0) {
                            logFile.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param isAll 是否删除根目录
     * @param file  要删除的根目录
     */
    public void deleteFile(File file, boolean isAll) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                if (isAll) {
                    file.delete();
                }
                return;
            }
            for (File f : childFile) {
                deleteFile(f, true);
            }
            if (isAll) {
                file.delete();
            }
        }
    }
}