package Entity;

import utils.ApiKeyStatus;
import utils.HttpMessageStatus;
import java.util.List;

public class LogEntity {

    // 상태코드
    HttpMessageStatus httpMessage;
    // URL
    String url;
    // APIService ID
    ApiKeyStatus apiServiceId;
    // APIKey
    String apiKey;
    // 브라우저
    String browser;
    //호출시간
    String callDateTime;

    public static LogEntity createLogData(List<String> records) {
        LogEntity log = new LogEntity();
        String httpMessage = records.get(0);
        String url = records.get(1);
        String browser = records.get(2);
        String callDateTime = records.get(3);

        log.changeData(httpMessage, url, browser, callDateTime);
        return log;
    }

    public void changeData(String httpMessage, String url, String browser, String callDateTime) {
        this.httpMessage = convertStringToHttpMessage(httpMessage);
        this.url = url;
        this.apiServiceId = extractApiServiceId(url);
        this.apiKey = extractApiKey(url);
        this.browser = browser;
        this.callDateTime = callDateTime;
    }

    public HttpMessageStatus convertStringToHttpMessage(String stringHttpMessage) {
        if ("200".equals(stringHttpMessage)) {
            return HttpMessageStatus.SUCCESS;
        } else if ("404".equals(stringHttpMessage)) {
            return HttpMessageStatus.PAGE_NOT_FOUND;
        } else {
            return HttpMessageStatus.MISS_PARAM;
        }
    }

    public ApiKeyStatus extractApiServiceId(String url) {
        String apiKeySubString = ApiKeySubString(url);
        ApiKeyStatus apiKeyStatus = convertStringToApiKeyStatus(apiKeySubString);
        return apiKeyStatus;
    }

    public String ApiKeySubString(String url) {
        int startIndex = url.indexOf("search/");
        int endIndexOf = url.indexOf('?');
        if (endIndexOf == -1 || startIndex == -1) {
            return "fail";
        }
        startIndex = startIndex + 7; // 앞글자 기준 => 단어 크기만큼 더해줘야된다.

        return url.substring(startIndex, endIndexOf);
    }

    public String extractApiKey(String url) {
        int startIndex = url.indexOf("apikey=");
        int endIndex = url.indexOf("&");
        if (startIndex == -1 || endIndex == -1) {
            return "fail";
        }
        return url.substring(startIndex + 7, endIndex);
    }

    public ApiKeyStatus convertStringToApiKeyStatus(String apiKeySubString) {
        ApiKeyStatus apiKeyStatus;
        if ("blog".equals(apiKeySubString)) {
            apiKeyStatus = ApiKeyStatus.BLOG;
        } else if ("news".equals(apiKeySubString)) {
            apiKeyStatus = ApiKeyStatus.NEWS;
        } else if ("knowledge".equals(apiKeySubString)) {
            apiKeyStatus = ApiKeyStatus.KNOWLEDGE;
        } else if ("book".equals(apiKeySubString)) {
            apiKeyStatus = ApiKeyStatus.BOOK;
        } else if ("vclip".equals(apiKeySubString)) {
            apiKeyStatus = ApiKeyStatus.VCLIP;
        } else if ("image".equals(apiKeySubString)) {
            apiKeyStatus = ApiKeyStatus.IMAGE;
        } else {
            apiKeyStatus = ApiKeyStatus.FAIL;
        }
        return apiKeyStatus;
    }

    public HttpMessageStatus getHttpMessage() {
        return httpMessage;
    }

    public ApiKeyStatus getApiServiceId() {
        return apiServiceId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBrowser() {
        return browser;
    }

}
