import Entity.LogEntity;
import controller.FileService;
import controller.LogService;

import java.io.IOException;
import java.util.List;

public class APIAnalysis {

    public static void main(String args[]) throws IOException {
        FileService fileService = new FileService();
        List<List<String>> records = fileService.readFile();

        // 로그 엔티티 만들기
        LogService logService = new LogService();
        List<LogEntity> logEntity = logService.createLogEntity(records).orElseThrow(
                () -> new IllegalArgumentException("초기 데이터가 없습니다.")
        );

        logService.selectMaxApiKey(logEntity);
        logService.selectTop3ApiServiceId(logEntity);
        logService.selectBrowserPercent(logEntity);
        fileService.fileWrite();

    }
}
