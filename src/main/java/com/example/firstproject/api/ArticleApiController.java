package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController //Rest API용 컨트롤러, 데이터(JSON)를 반환한다
public class ArticleApiController {

    @Autowired // DI : 의존성 주입, 외부에서 가져옴
    private ArticleRepository articleRepository;

    //GET
    @GetMapping("/api/articles")
    public List<Article> index(){
        //리포지토리를 이용해서 모든 article을 가져옴
        return articleRepository.findAll();
    }

    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id){
        return articleRepository.findById(id).orElse(null);
    }

    //POST
    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto){
        //dto로(ArticleForm) 클라이언트가 보낸 json을 받아서 엔티티인 Article로 변환함
        Article article = dto.toEntitiy();

        return articleRepository.save(article);
    }

    //PATCH
    @PatchMapping("/api/articles/{id}")  //ResponseEntity에 담아서 보내면 상태 코드를 같이 담을 수 있다
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) {
        //1. 수정용 엔티티 생성
        Article article = dto.toEntitiy();
        log.info("id : {}, article : {}", id, article.toString());

        //2. 대상 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        //3. 잘못된 요청 처리
        if (target==null || id!=article.getId()){
            //잘못된 요청 응답코드 400
            log.info("Wrong Request! id : {}, article : {}",id,article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //4. 업데이트 및 정상 응답(200)
        target.patch(article);
        Article updated = articleRepository.save(target);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    //DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){

        // 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리
        if (target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 대상 삭제
        articleRepository.delete(target);

        // 데이터 반환
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
