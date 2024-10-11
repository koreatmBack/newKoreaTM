package com.example.smsSpringTest.model.response;

import com.example.smsSpringTest.model.ad.AdRequest;
import com.example.smsSpringTest.model.ad.JobSite;
import com.example.smsSpringTest.model.ad.fmAd;
import com.example.smsSpringTest.model.ad.fmAdImage;
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
    private List<fmAdImage> fmAdImageList;
    private int totalPages;
}
