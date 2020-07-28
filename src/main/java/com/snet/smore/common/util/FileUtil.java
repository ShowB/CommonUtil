package com.snet.smore.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
