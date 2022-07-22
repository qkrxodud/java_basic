import Entity.LogEntity;
import service.FileService;
import service.LogService;
import utils.ApiKeyStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class APIAnalysis {

    public static void main(String args[]) throws IOException {
        FileService fileService = new FileService();
        // 파일 읽기
        List<List<String>> records = fileService.readFile();

        // 로그 엔티티 만들기
        LogService logService = new LogService();
        List<LogEntity> logEntity = logService.createLogEntity(records).orElseThrow(
                () -> new IllegalArgumentException("초기 데이터가 없습니다.")
        );

        // 최다 호출 APIKEY
        String maxApiKey = logService.selectMaxApiKey(logEntity).orElseThrow(
                () -> new IllegalArgumentException("최다호출 API Key 값이 없습니다.")
        );

        // 상위 3개의 API Service ID
        List<Map.Entry<ApiKeyStatus, Integer>> top3ApiServiceId = logService.selectTop3ApiServiceId(logEntity).orElseThrow(
                () -> new IllegalArgumentException("상위 3개의 API Service Id와 각각요청 수가 없습니다.")
        );

        // 웹 브라우저별 사용 비율
        List<Map.Entry<String, Double>> browserRatio = logService.selectBrowserRatio(logEntity).orElseThrow(
                () -> new IllegalArgumentException("웹 브라우저별 사용 비율값이 없습니다.")
        );

        // 파일 작성
        fileService.fileWrite(maxApiKey, top3ApiServiceId, browserRatio);

    }
}
