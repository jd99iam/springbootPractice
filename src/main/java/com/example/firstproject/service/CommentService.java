package com.example.firstproject.service;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@Slf4j //log  사용할 수 있게 해줌
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;


    public List<CommentDto> comments(Long articleId) {
        // 조회 : 댓글 목록
//        List<Comment> comments = commentRepository.findByArticleId(articleId);

//        // 변환 : 엔티티 -> DTO
//        List<CommentDto> dtos = new ArrayList<CommentDto>();
//        for (int i=0; i<comments.size(); i++){
//            Comment c = comments.get(i);
//            CommentDto dto = CommentDto.createCommentDto(c);
//            dtos.add(dto);
//        }

        // 반환
        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment->CommentDto.createCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Transactional //DB에 접근하므로 트랙잭션 어노테이션으로 문제가 발생하면 롤백되도록 해줘야함
    public CommentDto create(Long articleId, CommentDto dto) {

        // 게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다.")); //해당 id 없을경우 예외 발생

        // 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto,article);

        // 댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        // DTO로 변경하여 반환
        return CommentDto.createCommentDto(created);


    }

    @Transactional // DB 건드리니까 문제 발생시 롤백 되도록
    public CommentDto update(Long id, CommentDto dto) {

        //댓글 조회, 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));

        //댓글 수정
        target.patch(dto);

        //DB 갱신
        Comment updated = commentRepository.save(target);

        //댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);

    }

    @Transactional
    public CommentDto delete(Long id) {
        // 댓글 조회(및 예외 발생)
        Comment target = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다") );

        // DB에서 댓글 삭제
        commentRepository.delete(target); //delete는 반환값 없음

        // 삭제 댓글 DTO로 반환
        return CommentDto.createCommentDto(target);
    }
}
