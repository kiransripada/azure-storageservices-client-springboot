package az.storage.services;

import az.storage.config.AzureStorageConfiguration;
import az.storage.util.SecurityUtil;
import az.storage.util.UtilityFactory;
import az.storage.util.UtilityType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 *
 */
@Slf4j
@Component
public class AzureStorageService {

    private static org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
    private final AzureStorageConfiguration storageConfiguration;
    private final RestTemplateBuilder restTemplateBuilder;
    private final UtilityFactory utilityFactory;

    public AzureStorageService(AzureStorageConfiguration storageConfiguration, RestTemplateBuilder restTemplateBuilder,
                               UtilityFactory utilityFactory) {
        this.storageConfiguration = storageConfiguration;
        this.restTemplateBuilder = restTemplateBuilder;
        this.utilityFactory =utilityFactory;
    }

    /**
     *
     * @return
     */
    private String getRequestTime() {
        Instant instant = Instant.now();
        String formatted = DateTimeFormatter.RFC_1123_DATE_TIME
                .withZone(ZoneOffset.UTC)
                .format(instant);
        return formatted;
    }

    /**
     *
     * @return
     */
    private HttpHeaders getHeaders() {
        // Prepare header
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(acceptableMediaTypes);
        headers.set("x-ms-date", getRequestTime());
        headers.set("x-ms-version", storageConfiguration.getApiVersion());
        headers.set("Authorization", "SharedKey " + storageConfiguration.getStorageAccountName() + ":" + getAuthorizationHeader());
        return headers;
    }

    private String getURIParameters() {
        String RESOURCE_URL = "https://" + storageConfiguration.getStorageAccountName() + ".blob.core.windows.net/test-container";

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(RESOURCE_URL)
                .queryParam("restype", "container")
                .queryParam("comp", "list");
        System.out.println("RESOURCE_URL::" + uriComponentsBuilder.toUriString());
        return uriComponentsBuilder.toUriString();
    }
    /**
     * @return
     */
    private Map<String, String> paramsMap() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("verb", "GET");
        paramMap.put("Content-Encoding", "");
        paramMap.put("Content-Language", "");
        paramMap.put("Content-Length", "");
        paramMap.put("Content-MD5", "");
        paramMap.put("Content-Type", "");
        paramMap.put("Date", "");
        paramMap.put("If-Modified-Since", "");
        paramMap.put("If-Match", "");
        paramMap.put("If-None-Match", "");
        paramMap.put("If-Unmodified-Since", "");
        paramMap.put("Range", "");
        paramMap.put("CanonicalizedHeaders", getCanonicalizedHeaders());
        paramMap.put("CanonicalizedResource", getCanonicalizedResource());
        return paramMap;
    }

    private String getCanonicalizedResource() {
        StringJoiner canonicalResource = new StringJoiner("", "", "");
        canonicalResource.add("/test-sa/testcont-container\ncomp:list\nrestype:container");
        return canonicalResource.toString();
    }

    /**
     * @return
     */
    private String getCanonicalizedHeaders() {
        //'CanonicalizedHeaders': 'x-ms-date:' + request_time + '\nx-ms-version:' + api_version + '\n',
        StringJoiner canonicalHeader = new StringJoiner("", "", "");
        canonicalHeader.add("x-ms-date:" + getRequestTime()).add("\nx-ms-version:" + storageConfiguration.getApiVersion());
        return canonicalHeader.toString();
    }

    /***
     * // This is the raw representation of the message signature.
     * // Now turn it into a byte array.
     * // Create the HMACSHA256 version of the storage key
     * // Compute the hash of the SignatureBytes and convert it to a base64 string.
     * // This is the actual header that will be added to the list of request headers.
     * // You can stop the code here and look at the value of 'authHV' before it is returned.
     * @return
     */
    @SneakyThrows
    private String getAuthorizationHeader() {
        String stringToSign = String.join("\n"
                , paramsMap().get("verb")
                , paramsMap().get("Content-Encoding")
                , paramsMap().get("Content-Language")
                , paramsMap().get("Content-Length")
                , paramsMap().get("Content-MD5")
                , paramsMap().get("Content-Type")
                , paramsMap().get("Date")
                , paramsMap().get("If-Modified-Since")
                , paramsMap().get("If-Match")
                , paramsMap().get("If-None-Match")
                , paramsMap().get("If-Unmodified-Since")
                , paramsMap().get("Range")
                , paramsMap().get("CanonicalizedHeaders")
                , paramsMap().get("CanonicalizedResource"));
        System.out.println("stringToSign=" + stringToSign);

        SecurityUtil securityUtil = (SecurityUtil) utilityFactory.getUtility(UtilityType.SECURITY_UTILITY.getType());
        String authKey= securityUtil.createSignature(stringToSign);

        return authKey;
    }
    /**
     * @return
     */
    public String listAllFilesInStorageContainer() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpEntity requestEntity = new HttpEntity(null, getHeaders());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        log.info("Azure BLOB endpoint ::" + getURIParameters());
        ResponseEntity<String> response = restTemplate.exchange(getURIParameters(), HttpMethod.GET, requestEntity, String.class);
        log.info("response {}", response);
        return response.getBody();
    }
}
