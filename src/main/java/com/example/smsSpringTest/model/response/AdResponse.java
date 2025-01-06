package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.Regions;
import com.example.smsSpringTest.model.ad.*;
import com.example.smsSpringTest.model.findCompanyAndUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * author : 신기훈
 * date : 2024-09-26
 * comment : 광고 결과값 리턴
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdResponse extends ApiResponse{

    private List<fmAd> fmAdList;
    private List<AdRequest> findFmAdList;
    private List<JobSite> jobSiteList;
    private List<findCompanyAndUser> findCompanyAndUserList;
    private List<fmAdImage> fmAdImageList;
    private List<AdNearInfo> nearInfoList;
    private List<Regions> regionsList;
    private String applyMethod;
    private fmAd oneAd;


    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // 기본값(0)일 때 제외
    private int totalPages;


    private int totalCount; // 총 개수
}
