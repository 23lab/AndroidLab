package us.stupidx.tools;

import java.io.File;

import android.os.Environment;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：FileUtil.java
 * Description：
 * History：
 * 1.0 turing 2013-7-17 Create
 */

public class FileUtils {
    private static final String DIR_EXT_MAIN = "MSDK";
    private static final String FILE_LOG = "log.dat";

    /**
     * Check to see whether external storage(sdcard) existed at present.
     */
    public static boolean hasExternalStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getLogFile() {
        return new File(getExternalRootDir(), FILE_LOG);
    }

    /**
     * 获得SDK存储目录
     * 
     * @return
     */
    public static File getExternalRootDir() {
        File childDir = null;
        // Environment.getExternalStorageDirectory() 返回/sdcard/
        // 使用系统方法，避免这个名字可能不同
        childDir = new File(Environment.getExternalStorageDirectory(), DIR_EXT_MAIN);
        if (!childDir.exists())
            childDir.mkdirs();

        return childDir;
    }

}
