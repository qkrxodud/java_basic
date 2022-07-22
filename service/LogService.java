package service;


import Entity.LogEntity;
import utils.ApiKeyStatus;
import utils.HttpMessageStatus;

import java.util.*;
import java.util.stream.Collectors;

import static Entity.LogEntity.createLogData;

/**
 * 최다 호출 APIKEY
 * 상위 3개의 API Service ID 와 각각 요청 수
 * 웹 브라우저별 사용 비율
 */
public class LogService {

    // 로그 엔티티 만들기
    public Optional<List<LogEntity>> createLogEntity(List<List<String>> records) {
        List<LogEntity> logs = new ArrayList<>();

        for (List<String> record : records) {
            logs.add(createLogData(record));
        }
        return Optional.of(logs);
    }

    // 최다 호출 APIKEY
    public Optional<String> selectMaxApiKey(List<LogEntity> logs) {
        HashMap<String, Integer> apiKeyMap = new HashMap<>();

        for (LogEntity log : logs) {
            if (log.getHttpMessage() != HttpMessageStatus.SUCCESS) continue;
            String apiKey = log.getApiKey();
            apiKeyMap.put(apiKey, apiKeyMap.getOrDefault(apiKey, 0) + 1);
        }

        List<Map.Entry<String, Integer>> collect = apiKeyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return Optional.of(collect.get(0).getKey());
    }

    // 상위 3개의 API Service ID 와 각각 요청 수
    public Optional<List<Map.Entry<ApiKeyStatus, Integer>>> selectTop3ApiServiceId(List<LogEntity> logs) {
        HashMap<ApiKeyStatus, Integer> apiKeyMap = new HashMap<>();

        for (LogEntity log : logs) {
            if (log.getHttpMessage() != HttpMessageStatus.SUCCESS) continue;
            ApiKeyStatus apiServiceId = log.getApiServiceId();
            apiKeyMap.put(apiServiceId, apiKeyMap.getOrDefault(apiServiceId, 0) + 1);
        }

        List<Map.Entry<ApiKeyStatus, Integer>> collect = apiKeyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return Optional.of(collect.subList(0, 3));
    }

    // 웹 브라우저별 사용 비율
    public Optional<List<Map.Entry<String, Double>>> selectBrowserRatio(List<LogEntity> logs) {
        HashMap<String, Integer> browserMap = new HashMap<>();
        HashMap<String, Double> browserRatioMap = new HashMap<>();

        int total = 0;
        for (LogEntity log : logs) {
            if (log.getHttpMessage() != HttpMessageStatus.SUCCESS) continue;
            String browser = log.getBrowser();
            browserMap.put(browser, browserMap.getOrDefault(browser, 0) + 1);
            total++;
        }

        for (String key : browserMap.keySet()) {
            double percent = ((double) browserMap.get(key) / total) * 100;
            browserRatioMap.put(key, Math.floor(percent * 1000) / 1000);
        }

        List<Map.Entry<String, Double>> collect = browserRatioMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return Optional.of(collect);
    }
}
