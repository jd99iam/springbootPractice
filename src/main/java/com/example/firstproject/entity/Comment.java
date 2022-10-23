package com.example.firstproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 댓글 엔티티 여러개가 하나의 Article에 연관됨
    @JoinColumn(name = "article_id") // article_id 컬럼에 연결된 Article의 대표값을 저장(필드명 지정)
    private Article article;

    @Column
    private String nickname;

    @Column
    private String  body;




}
