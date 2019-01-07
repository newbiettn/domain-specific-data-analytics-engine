package planner.jshop;

import java.io.IOException;

/**
 * @author newbiettn on 19/6/18
 * @project DiabetesDiscoveryV2
 */

public class Test2 {
    public static void main(String[] args) throws IOException {
        byte[] content = (" implements ProblemInterface\n").getBytes();

//        insert("diabetes-engine/src/main/java/planner/jshop/problem.java", 90,content );
    }

//    static void insert(String filename, long offset, byte[] content) throws IOException {
//        RandomAccessFile r = new RandomAccessFile(filename, "rw");
//        RandomAccessFile rtemp = new RandomAccessFile(filename+"Temp", "rw");
//        long fileSize = r.length();
//        FileChannel sourceChannel = r.getChannel();
//        FileChannel targetChannel = rtemp.getChannel();
//        sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
//        sourceChannel.truncate(offset);
//        r.seek(offset);
//        r.write(content);
//        long newOffset = r.getFilePointer();
//        targetChannel.position(0L);
//        sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
//        sourceChannel.close();
//        targetChannel.close();
//        rtemp.close();
//        r.close();
//
//    }

}
