package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {
    final private String readFileName = "input.log";
    final private String writeFileName = "output.log";

    //파일 읽기
    public List<List<String>> readFile() throws IOException {
        BufferedReader bufferedReader = null;
        List<List<String>> records = new ArrayList<>();

        try {
            // 파일 체크
            File file = new File(readFileName);
            fileCheck(file);

            // 파일 버퍼로 읽기
            bufferedReader = new BufferedReader(new FileReader(file.getCanonicalPath()));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                // 레코드 생성
                records.add(createRecord(readLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bufferedReader.close();
        }

        return records;
    }

    void fileCheck(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
    }

    List<String> createRecord(String readLine) {
        List<String> record = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<=\\[).+?(?=\\])");
        Matcher matcher = pattern.matcher(readLine);
        while (matcher.find()) {
            record.add(matcher.group());
        }
        return record;
    }

    //파일 쓰기
    public void fileWrite() throws IOException {
        FileWriter file = new FileWriter(writeFileName, true);
        BufferedWriter writer  = new BufferedWriter(file);
        writer.write("test.....");
        writer.close();
    }
}
