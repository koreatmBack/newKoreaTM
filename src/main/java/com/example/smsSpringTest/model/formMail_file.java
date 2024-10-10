package com.example.smsSpringTest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * author : 신기훈
 * date : 2024-10-10
 * comment : 광고 db + formMail_file db저장용 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class formMail_file {

    private String aid;

    private String cid;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;

    private int extensionDay;

    private int totalDay;

    private String heaven;

    private String albamon;

    private String telejob;

    private String adTypeM; // 유료 광고 타입 - 알바몬

    private String adTypeH; // 유료 광고 타입 - 알바천국

    private String adImg; // 광고 이미지 url

    private String logoImg; // 로고 이미지 url

    private String concept; // 컨셉

    private String type; // 광고 이미지인지 , 로고 이미지인지 타입.

}
