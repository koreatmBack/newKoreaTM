package com.example.smsSpringTest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : 신기훈
 * date : 2024-09-24
 * comment : 페이징 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paging {

    private int page;
    private int size;
    private int offset;
//    private String searchKeyword;

}
