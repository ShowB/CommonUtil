package com.snet.smore.common.util;

import org.junit.Test;

import java.io.File;
import java.util.List;

public class EnvironmentTest {
    @Test
    public void test() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Environment env = EnvManager.getEnvironment();
//            System.out.println(env.getProperty("AA"));
//            System.out.println(EnvManager.getProperty("AA"));
//            System.out.println(EnvManager.getInstance().equals(EnvManager.getInstance()));
//            System.out.println(env.equals(EnvManager.getEnvironment()));
        }
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);


//        String uri = "D:\\GET_FILE\\TCMS\\TC_ROTEMDRV\\20200708162741735-35628412.conversion";
//        Path path = Paths.get(uri);
//        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
////            System.out.println(bufferedReader.readLine());
//            final Stream<String> lines = bufferedReader.lines();
//            final List<String> collect = lines.collect(Collectors.toList());
//            for (String e : collect) {
//                System.out.println(e);
//            }
//        } catch (Exception e) {
//
//        }
//        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
//            int fullSize = (int) Files.size(path);
//            ByteBuffer buffer = ByteBuffer.allocate(fullSize);
//
//            int bufferSize = 1024;
//
//            buffer.limit(bufferSize);
//
//            StringBuffer sb = new StringBuffer();
//
//            while (buffer.hasRemaining()) {
//                channel.read(buffer);
//                buffer.flip();
//                sb.append(Charset.defaultCharset().decode(buffer).toString());
//                buffer.position(buffer.limit());
//                buffer.limit(Math.min(buffer.limit() + bufferSize, fullSize));
//
//                System.out.println("position -> " + buffer.position() + "\t" + "limit -> " + buffer.limit() + "\t" + "fullSize -> " + fullSize);
//            }
//
//            System.out.println("sb.toString() size -> " + sb.toString().getBytes().length);
//            System.out.println("fullSize -> " + fullSize);
//            System.out.println("buffer.limit() -> " + buffer.limit());
//
//
//
//            String  a = Charset.defaultCharset().decode(buffer).toString();
//            System.out.println(buffer);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void fileTest() {
        List<File> files = FileUtil.findFiles(Constant.WORK_DIR, "", "");

        files.forEach(System.out::println);
    }

}
