package com.example.firstproject.entity;

import com.example.firstproject.dto.CommentDto;
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


    public static Comment createComment(CommentDto dto, Article article) {
        //예외 처리
        if (dto.getId() != null){ //id 생성할때는 json에 id 를 넣으면 안되는데 넣었을 경우
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        }
        if (dto.getArticleId()!=article.getId()){
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못되었습니다.");
        }

        //엔티티 생성 및 반환
        return new Comment(
                dto.getId(),
                article,
                dto.getNickname(),
                dto.getBody()
        );
    }

    public void patch(CommentDto dto) {
        //예외 발생
        if (this.id != dto.getId()){
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력되었습니다.");
        }

        //객체 갱신
        if (dto.getNickname()!=null)
            this.nickname = dto.getNickname();

        if (dto.getBody()!=null){
            this.body = dto.getBody();
        }

    }
}
