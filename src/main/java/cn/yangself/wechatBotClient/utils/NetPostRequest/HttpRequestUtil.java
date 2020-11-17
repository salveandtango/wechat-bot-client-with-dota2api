package cn.yangself.wechatBotClient.utils.NetPostRequest;

import cn.yangself.wechatBotClient.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

/**
 * 这个类用来发送Post请求
 */
@Component
@Slf4j
public class HttpRequestUtil {

    private RestTemplate restTemplate;
    private DateUtil dateUtil;

    @Autowired
    public HttpRequestUtil(RestTemplate restTemplate, DateUtil dateUtil) {
        this.restTemplate = restTemplate;
        this.dateUtil = dateUtil;
    }

    /**
     * @return com.alibaba.fastjson.JSONObject
     * @des 发送post请求
     * @author Fixed
     * @date 2020/8/31 10:05
     * @params [httpHeaders, url, postParam]
     */
    public JSONObject sendPost(HttpHeaders httpHeaders, String url, Map<String, String> postParam) throws RestClientException {
        String dateTime = dateUtil.now();
        try {
            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, generatePOSTParam(httpHeaders, postParam), JSONObject.class);
            log.info("Post request Start, dateTime: {}, url: {}, param: {}, response: {}", dateTime, url, postParam.toString(), Objects.requireNonNull(responseEntity.getBody()).toJSONString());
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Post request error, {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @return com.alibaba.fastjson.JSONObject
     * @des 发送get请求
     * @author Fixed
     * @date 2020/8/31 10:05
     * @params [url, getParam]
     */
    public JSONObject sendGet(String url, Map<String, String> getParam) throws RestClientException {
        String dateTime = dateUtil.now();
        try {
            ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(generateGETParam(url, getParam), JSONObject.class);
            log.info("Get request Start, dateTime: {}, url: {}, param: {}, response: {}", dateTime, url, getParam.toString(), Objects.requireNonNull(responseEntity.getBody()).toJSONString());
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Get request error, {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @return com.alibaba.fastjson.JSONObject
     * @des 发送携带请求头的get请求
     * @author Fixed
     * @date 2020/8/31 10:06
     * @params [headers, url, getParam]
     */
    public JSONObject sendGetWithHeader(HttpHeaders headers, String url, Map<String, String> getParam) {
        String dateTime = dateUtil.now();
        try {
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                    generateGETParam(url, getParam),
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    JSONObject.class
            );
            log.info("Get request with header start, dateTime: {}, headers: {}, url: {}, param: {}", dateTime, headers, url, getParam.toString());
            return responseEntity.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            String errorMessage = e.getMessage();
            int start = errorMessage.indexOf("{");
            int end = errorMessage.indexOf("]");
            String msgDetail = errorMessage.substring(start, end);
            log.error("Get http request error: {}", msgDetail);
            return null;
        }
    }


    /**
     * @return org.springframework.http.HttpEntity<java.util.Map < java.lang.String, java.lang.String>>
     * @des 组装Post请求
     * @author Fixed
     * @date 2020/8/31 10:04
     * @params [httpHeaders, jsonMap]
     */
    private HttpEntity<Map<String, String>> generatePOSTParam(HttpHeaders httpHeaders, Map<String, String> jsonMap) {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        return new HttpEntity<>(jsonMap, httpHeaders);
    }

    /**
     * @return java.lang.String
     * @des 组装get请求
     * @author Fixed
     * @date 2020/8/31 10:05
     * @params [url, params]
     */
    private String generateGETParam(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (String key : params.keySet()) {
            Object value = params.get(key);
            sb.append(key)
                    .append("=")
                    .append(value)
                    .append("&");
        }
        /*for (Map.Entry map : params.entrySet()) {
            url.append(map.getKey())
                    .append("=")
                    .append(map.getValue())
                    .append("&");
        }*/
        return sb.substring(0, url.length() - 1);
    }


}
