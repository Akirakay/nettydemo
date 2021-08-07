package com.akira.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author akira
 * @date 2021-08-04 16:00:13
 * @description
 */
public class NIOFileChannel3 {
    public static void main(String[] args) {

        try {
//            NIOFileChannel3 demo = new NIOFileChannel3();

//            fileCopyMethod1(demo);

            // 文件输入
            File sourceFile = new File("./demo.txt");

            FileInputStream fis = new FileInputStream(sourceFile);
            FileChannel sourceChannel = fis.getChannel();

            // 文件输出
            File destFile = new File("./copy.txt");
            FileOutputStream fos = new FileOutputStream(destFile);
            FileChannel destChannel = fos.getChannel();

            // 缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while(true) {
                buffer.clear();
                int read = sourceChannel.read(buffer);
                if (read == -1) {
                    break;
                }
                buffer.flip();
                destChannel.write(buffer);

            }
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fileCopyMethod1(NIOFileChannel3 demo) throws IOException {
        File sourceFile = new File("./demo.txt");
        ByteBuffer buffer = ByteBuffer.allocate((int) sourceFile.length());
        demo.getByteBuffer(buffer, sourceFile);
        buffer.flip();
        File descFile = new File("./copy.txt");
        demo.writeBuffer(buffer, descFile);
    }

    private void writeBuffer(ByteBuffer buffer, File dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        FileChannel fosChannel = fos.getChannel();
        fosChannel.write(buffer);
        fos.close();
    }

    private void getByteBuffer(ByteBuffer buffer, File file) throws IOException {
        // 读取文件到buffer
        FileInputStream fis = new FileInputStream(file);
        FileChannel channel = fis.getChannel();
        channel.read(buffer);
        fis.close();
    }
}
