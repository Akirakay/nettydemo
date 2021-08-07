package com.akira.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author 陶真凯
 * @date 2021-08-04 16:00:13
 * @description
 */
public class NIOFileChannel1 {
    public static void main(String[] args) {

        try {
            File file = new File("./demo.txt");
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            buffer.put("hello,akira!".getBytes(StandardCharsets.UTF_8));

            buffer.flip();

            channel.write(buffer);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
