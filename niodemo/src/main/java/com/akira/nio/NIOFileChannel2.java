package com.akira.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author akira
 * @date 2021-08-04 16:00:13
 * @description
 */
public class NIOFileChannel2 {
    public static void main(String[] args) {

        try {
            File file = new File("./demo.txt");
            FileInputStream fis = new FileInputStream(file);
            FileChannel channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

            channel.read(buffer);

            buffer.flip();

            System.out.println(new String(buffer.array()));

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
