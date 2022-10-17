package com.example.firstproject.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity  // 이 어노테이션을 붙여야 DB가 이 객체를 인식한다
@AllArgsConstructor
@NoArgsConstructor //디폴트 생성자 추가
@ToString
public class Article {

    @Id // 각 객체를 식별하기 위한 Id임 (주민번호 같은거)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id를 자동생성함
    private Long id;

    @Column //DB가 필드를 인식할 수 있게 해줌
    private String title;
    @Column
    private String content;

}
