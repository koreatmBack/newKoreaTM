package com.example.smsSpringTest.model.ad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-09-26
 * comment : 광고 이미지 결과 담을 vo ( page, size , offset 결과 리스트에 출력되지 않기 위해 )
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdImageRequest {

    private String aiId;
    private String aid;
    private String path;
    private String concept;

    private int page;
    private int size;
    private int offset;

}
