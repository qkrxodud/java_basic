package service;

import utils.ApiKeyStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {
    final private String readFileName = "resources/input.log";
    final private String writeFileName = "resources/output.log";

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
    public void fileWrite(String maxApiKey, List<Map.Entry<ApiKeyStatus, Integer>> top3ApiServiceId, List<Map.Entry<String, Double>> browserRatios) throws IOException {
        FileWriter file = new FileWriter(writeFileName);
        BufferedWriter bufferedWriter  = new BufferedWriter(file);
        try {
            // 파일 체크
            bufferedWriter.write("최다호출 API KEY");
            bufferedWriter.write("\n");
            bufferedWriter.write(maxApiKey);
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            bufferedWriter.write("상위 3개의 API Service ID와 각각의 요청수");
            bufferedWriter.write("\n");
            for (Map.Entry<ApiKeyStatus, Integer> apiServiceId : top3ApiServiceId) {
                bufferedWriter.write(apiServiceId.getKey() +" : " + apiServiceId.getValue());
                bufferedWriter.write("\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            bufferedWriter.write("웹브라우저별 사용 비율");
            bufferedWriter.write("\n");
            for (Map.Entry<String, Double> browserRatio : browserRatios) {
                bufferedWriter.write(browserRatio.getKey() + " : " + browserRatio.getValue() + "%");
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }
}
