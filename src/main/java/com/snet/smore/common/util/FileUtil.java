package com.snet.smore.common.util;

import com.snet.smore.common.constant.Constant;
import com.snet.smore.common.constant.FileStatusPrefix;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtil {

    /**
     * 컨버전 대상 파일을 찾음
     *
     * @param source source
     * @param filePrefix filePrefix
     * @return List<File>
     * @throws Exception Exception
     */
    public static List<File> findFiles(String source, String filePrefix, String fileExt) {
        return getTargetFilesHierarchy(source, filePrefix, fileExt, new ArrayList<>());
    }

    /**
     * 하위 디렉토리를 모두 돌면서 컨버전 대상 파일을 찾음
     *
     * @param uri uri
     * @param filePrefix filePrefix
     * @param files files
     * @return List<File>
     * @throws Exception Exception
     */
    private static List<File> getTargetFilesHierarchy(String uri, String filePrefix, String fileExt, List<File> files) {
        try {
            File rootFoldor = new File(uri);

            if (!rootFoldor.exists()) return files;

            File[] fileList = rootFoldor.listFiles();

            if (fileList == null) return files;

            for (File e : fileList) {
                if (e.isFile()) {
                    if (StringUtil.isNotBlank(filePrefix) && StringUtil.isNotBlank(fileExt)) {
                        if (e.getName().startsWith(filePrefix) && e.getName().endsWith(fileExt)) {
                            files.add(e);
                        }
                    } else if (StringUtil.isNotBlank(filePrefix)) {
                        if (e.getName().startsWith(filePrefix)) {
                            files.add(e);
                        }
                    } else if (StringUtil.isNotBlank(fileExt)) {
                        if (e.getName().endsWith(fileExt)) {
                            files.add(e);
                        }
                    } else {
                        files.add(e);
                    }
                } else {
                    // Directory일 경우, 자기 자신 함수 재귀 호출
                    getTargetFilesHierarchy(e.getCanonicalPath(), filePrefix, fileExt, files);
                }
            }
        } catch (IOException ioe) {
            log.error(uri + " 하위 디렉토리 조회 중 오류 발생 !!");
        }

        return files;
    }

    /**
     * 디렉토리 생성
     *
     * @param source source
     */
    public static void makeDirectory(String source) {
        new File(source).mkdirs();
    }

    public static void mergeFiles() {
        log.debug("**************** File Merge Start ...");

        Environment env = EnvManager.getEnvironment();

        File[] files = new File("convert").listFiles();

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String line;
        int cnt = 0;

        if (files == null || files.length < 1) {
            log.debug("**************** File Merge Finished !!!");
            return;
        }

        List<File> dawonRunFiles = new ArrayList<>();
        List<File> dawonTrblFiles = new ArrayList<>();
        List<File> rotemRunFiles = new ArrayList<>();

        for (File e : files) {
            if (e.getName().endsWith(env.getProperty("CONVERSION_TEMP_EXT_DAWON_RUN")))
                dawonRunFiles.add(e);
            else if (e.getName().endsWith(env.getProperty("CONVERSION_TEMP_EXT_DAWON_TRBL")))
                dawonTrblFiles.add(e);
            else if (e.getName().endsWith(env.getProperty("CONVERSION_TEMP_EXT_ROTEM_RUN")))
                rotemRunFiles.add(e);
        }

        File mergedFile;
        FileWriter fileWriter = null;

        try {

            // 1. 다원 운행기록 merge
            if (dawonRunFiles.size() > 0) {
                cnt = 0;
                mergedFile = new File(env.getProperty("CONVERSION_DESTINATION_DAWON_RUN")
                        + Constant.FILE_SEPARATOR
                        + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis())
                        + "-" + UUID.randomUUID().toString().substring(0, 8) + ".conversion");
                fileWriter = new FileWriter(mergedFile);

                for (File e : dawonRunFiles) {
                    if (!e.isFile())
                        continue;

                    fileReader = new FileReader(e);
                    bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        if (cnt > 0 && line.startsWith("1|2|3|4|5"))
                            continue;

                        fileWriter.write(line);
                        fileWriter.write("\r\n");
                    }

                    fileReader.close();
                    bufferedReader.close();

                    cnt++;
                    System.out.println(cnt + " / " + dawonRunFiles.size());

                    Thread.sleep(10);
                    e.delete();
                }

                fileWriter.flush();
                fileWriter.close();
            }

            // 2. 다원 고장기록 merge
            if (dawonTrblFiles.size() > 0) {
                cnt = 0;
                mergedFile = new File(env.getProperty("CONVERSION_DESTINATION_DAWON_TRBL")
                        + Constant.FILE_SEPARATOR
                        + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis())
                        + "-" + UUID.randomUUID().toString().substring(0, 8) + ".conversion");
                fileWriter = new FileWriter(mergedFile);

                for (File e : dawonTrblFiles) {
                    if (!e.isFile())
                        continue;

                    fileReader = new FileReader(e);
                    bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        if (cnt > 0 && line.startsWith("1|2|3|4|5"))
                            continue;

                        fileWriter.write(line);
                        fileWriter.write("\r\n");
                    }

                    fileReader.close();
                    bufferedReader.close();

                    cnt++;
                    System.out.println(cnt + " / " + dawonTrblFiles.size());

                    Thread.sleep(10);
                    e.delete();
                }

                fileWriter.flush();
                fileWriter.close();
            }

            // 3. 로템 운행기록 merge
            if (rotemRunFiles.size() > 0) {
                cnt = 0;
                mergedFile = new File(env.getProperty("CONVERSION_DESTINATION_ROTEM_RUN")
                        + Constant.FILE_SEPARATOR
                        + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis())
                        + "-" + UUID.randomUUID().toString().substring(0, 8) + ".conversion");
                fileWriter = new FileWriter(mergedFile);

                for (File e : rotemRunFiles) {
                    if (!e.isFile())
                        continue;

                    fileReader = new FileReader(e);
                    bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        if (cnt > 0 && line.startsWith("1|2|3|4|5"))
                            continue;

                        fileWriter.write(line);
                        fileWriter.write("\r\n");
                    }

                    fileReader.close();
                    bufferedReader.close();

                    cnt++;
                    System.out.println(cnt + " / " + rotemRunFiles.size());

                    Thread.sleep(10);
                    e.delete();
                }

                fileWriter.flush();
                fileWriter.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("**************** File Merge Finished !!!");
    }

    public static Path changeFileStatus(Path originPath, FileStatusPrefix prefix) throws IOException {
        String fileName = originPath.getFileName().toString();

        for (FileStatusPrefix f : FileStatusPrefix.values()) {
            if (fileName.startsWith(f.getPrefix()))
                fileName = fileName.replace(f.getPrefix(), "");
        }

        fileName = prefix.getPrefix() + fileName;

        Path changePath = Paths.get(originPath.getParent().toAbsolutePath().toString(), fileName);
        return Files.move(originPath, changePath);
    }

    public static List<Path> findFiles(Path root, String glob) {
        List<Path> files = new ArrayList<>();

        if (StringUtil.isBlank(glob))
            glob = "*.*";

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

        try (Stream<Path> pathStream = Files.find(root, Integer.MAX_VALUE,
                (p, a) -> matcher.matches(p.getFileName())
                        && !p.getFileName().toString().startsWith(FileStatusPrefix.COMPLETE.getPrefix())
                        && !p.getFileName().toString().startsWith(FileStatusPrefix.ERROR.getPrefix())
                        && !p.getFileName().toString().startsWith(FileStatusPrefix.TEMP.getPrefix())
                        && !a.isDirectory()
                        && a.isRegularFile())) {
            files = pathStream.collect(Collectors.toList());
        } catch (Exception e) {
            log.error("An error occurred while finding source files.", e);
        }

        return files;
    }

    public static void initFiles(Path root) {
        List<Path> list = new ArrayList<>();

        FileStatusPrefix[] values = FileStatusPrefix.values();
        for (FileStatusPrefix prefix : values) {
            try (Stream<Path> temp = Files.find(root, Integer.MAX_VALUE, (p, a)
                    -> p.getFileName().toString().startsWith(prefix.getPrefix())
                    && !a.isDirectory())) {
                list = temp.collect(Collectors.toList());
            } catch (Exception e) {
                log.error("An error occurred while initializing files.", e);
            }

            int total = list.size();
            int curr = 0;

            for (Path p : list) {
                try {
                    System.out.println("[" + (++curr) + " / " + total + "]" + "\t" + p);
                    Files.move(p, Paths.get(p.getParent().toAbsolutePath().toString()
                            , p.getFileName().toString().replace(prefix.getPrefix(), "")));
                } catch (IOException e) {
                    log.error("An error occurred while initializing files. {}", p, e);
                }
            }
        }
    }
}
