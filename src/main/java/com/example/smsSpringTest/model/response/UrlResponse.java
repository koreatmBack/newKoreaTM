package com.example.smsSpringTest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * author : 신기훈
 * date : 2024-12-06
 * comment : url 변환용 vo
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlResponse extends ApiResponse{
    private String originalUrl;
    private String shortUrl;
}
