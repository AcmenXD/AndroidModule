package com.acmenxd.logger;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.acmenxd.frame.utils.RandomUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 输出日志到文件
 */
public final class FileLog {

    public static void printFile(@NonNull LogTag tag, @NonNull String headString, @NonNull String className, @NonNull String msg, @NonNull File dirFile, @NonNull String fileName) {
        fileName = (TextUtils.isEmpty(fileName)) ? getFileName() : fileName;
        String str = headString;
        if (save(tag, dirFile, fileName, headString, className, msg)) {
            str += BaseLog.LINE_SEPARATOR + "save log success ! location is >> " + dirFile.getAbsolutePath() + "/" + fileName;
        } else {
            str += BaseLog.LINE_SEPARATOR + "save log fails !";
        }
        BaseLog.printLog(LogType.FILE, tag, str);
    }

    private static boolean save(@NonNull LogTag tag, @NonNull File dir, @NonNull String fileName, @NonNull String headString, @NonNull String className, @NonNull String msg) {
        String str = null;
        if (dir == null) {
            str = "Source must not be null";
        } else if (!dir.exists()) {
            dir.mkdirs();
            if (!dir.exists()) {
                str = "Source '" + dir.getAbsolutePath() + "' can't create";
            }
        }
        if (!TextUtils.isEmpty(str)) {
            BaseLog.printSub(LogType.FILE, tag, "║ " + str);
            return false;
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException pE) {
                pE.printStackTrace();
            }
        }
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        try {
            outputStream = new FileOutputStream(file, true);
            outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(headString);
            outputStreamWriter.write(BaseLog.LINE_SEPARATOR + "* AbsolutePath:" + className);
            outputStreamWriter.write(BaseLog.LINE_SEPARATOR + "* Logger : " + dateStr);
            outputStreamWriter.write(BaseLog.LINE_SEPARATOR + "* Details:" + msg + BaseLog.LINE_SEPARATOR + BaseLog.LINE_SEPARATOR);
            outputStreamWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException pE) {
                pE.printStackTrace();
            }
        }
        return true;
    }

    private static String getFileName() {
        StringBuilder stringBuilder = new StringBuilder("Log_");
        stringBuilder.append(RandomUtils.getRandomByTime());
        stringBuilder.append(".txt");
        return stringBuilder.toString();
    }

}
