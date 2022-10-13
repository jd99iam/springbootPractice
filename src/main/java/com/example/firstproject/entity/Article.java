package com.example.firstproject.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity  // 이 어노테이션을 붙여야 DB가 이 객체를 인식한다
public class Article {

    @Id // 각 객체를 식별하기 위한 Id임 (주민번호 같은거)
    @GeneratedValue // id를 1, 2, 3 ,.. 자동생성하기 위함
    private Long id;

    @Column //DB가 필드를 인식할 수 있게 해줌
    private String title;
    @Column
    private String content;

    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
