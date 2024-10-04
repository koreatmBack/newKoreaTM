package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.S3Upload;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-25
 * comment : S3 업로드 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class S3UploadResponse extends ApiResponse{

    private String url;

    private List<S3Upload> s3UploadList;
    private List<String> urlList;
    // db 상관없이 현재 저장된 모든 url 갖고 오려면
    private List<String> allFileList;
}
