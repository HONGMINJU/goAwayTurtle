package com.turtle.goAwayTurtle.PredictAPI;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PredictAPIImpl implements PredictAPI{

    final public String predictURL = "http://3.37.105.34:5000/predict";

    public long getPredict(MultipartFile multipartFile) throws IOException {

        long predictReturn = 0;

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        RestTemplate restTemplate = new RestTemplate();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            byte [] byteArr = multipartFile.getBytes();
            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
                    .filename("filename").build();

            fileMap.add("Content-Type","image/jpeg");
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            HttpEntity<byte[]> entity = new HttpEntity<>(byteArr, fileMap);
            body = new LinkedMultiValueMap<>();
            body.add("file", entity);


        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(predictURL,requestEntity, String.class);

        try {
            JSONParser jsonParser = new JSONParser();
            Object obj =jsonParser.parse(response.getBody());
            JSONObject jsonObject = (JSONObject) obj;

            predictReturn = Math.toIntExact((long)jsonObject.get("label_id"));

            System.out.println("결과값 body : " + jsonObject.toJSONString());
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return predictReturn;

    }

}
