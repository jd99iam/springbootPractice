package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service //서비스 선언 (서비스 객체를 스프링부트에 생성)
@Slf4j
public class ArticleService {

    @Autowired //서비스가 리포지토리와 협업할 수 있도록 멤버로 넣어준다 , 마찬가지로 @Autowired를 이용해서 DI
    private ArticleRepository articleRepository;

    public List<Article> index(){
        return articleRepository.findAll();
    }

    public Article show(Long id){
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto){
        Article article = dto.toEntitiy();
        if (article.getId()!=null) {return null;}
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        //1. 수정용 엔티티 생성
        Article article = dto.toEntitiy();
        log.info("id : {}, article : {}", id, article.toString());

        //2. 대상 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        //3. 잘못된 요청 처리
        if (target==null || id!=article.getId()){
            //잘못된 요청 응답코드 400
            log.info("Wrong Request! id : {}, article : {}",id,article.toString());
            return null;
        }

        //4. 업데이트
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {

        // 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리
        if (target==null){
            return null;
        }

        // 대상 삭제
        articleRepository.delete(target);

        // 데이터 반환
        return target;
    }

    public List<Article> createArticles(List<ArticleForm> dtos){
        // dto 묶음을 entitiy 묶음으로 변환, 스트림 문법 사용
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntitiy())
                .collect(Collectors.toList());

        // entity 묶음을 DB에 저장
        articleList.stream()
                .forEach(article->articleRepository.save(article));

        // 강제로 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                ()->new IllegalArgumentException("실패!")
        );

        // 결과값 반환
        return articleList;

    }
}
