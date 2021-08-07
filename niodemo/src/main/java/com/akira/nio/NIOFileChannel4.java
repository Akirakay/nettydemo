package com.akira.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 *
 * @author 陶真凯
 * @date 2021-08-04 16:00:13
 * @description
 */
public class NIOFileChannel4 {
    public static void main(String[] args) {

        try {

            // 文件输入
            File sourceFile = new File("./demo.txt");

            FileInputStream fis = new FileInputStream(sourceFile);
            FileChannel sourceChannel = fis.getChannel();

            // 文件输出
            File destFile = new File("./copy.txt");
            FileOutputStream fos = new FileOutputStream(destFile);
            FileChannel destChannel = fos.getChannel();

            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
