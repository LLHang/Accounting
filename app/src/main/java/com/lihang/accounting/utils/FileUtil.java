package com.lihang.accounting.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HARRY on 2016/3/16.
 */
public class FileUtil {
    /**
     * 复制文件
     */
    public static void copy(String from, String to) throws IOException {
        copy(new File(from), new File(to));
    }

    /**
     * 复制文件
     */
    public static void copy(File from, File to) throws IOException {
        copy(new FileInputStream(from), new FileOutputStream(to));
    }

    /**
     * 复制文件
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        // 1K byte 的缓冲区!
        byte[] buf = new byte[1024];
        int count;
        while ((count = in.read(buf)) != -1) {
            System.out.println(count);
            out.write(buf, 0, count);
        }
        in.close();
        out.close();
    }
}
