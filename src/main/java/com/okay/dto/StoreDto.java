package com.okay.dto;

import com.okay.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class StoreDto {
    private Long storeNo;
    private Long views;
    private User userNo;
    private String fileName;   //이미지이름
    private String mapId; //api
    private Long size; // 이미지크기
    private String name;
    private String pw;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
