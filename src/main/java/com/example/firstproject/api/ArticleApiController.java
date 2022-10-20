package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.util.List;

@Slf4j
@RestController //Rest API용 컨트롤러, 데이터(JSON)를 반환한다
public class ArticleApiController {

    @Autowired // DI : 의존성 주입, 외부에서 가져옴 (생성 객체를 가져와서 연결)
    private ArticleService articleService;

    //GET
    @GetMapping("/api/articles")
    public List<Article> index(){
        //리포지토리를 이용해서 모든 article을 가져옴
        return articleService.index();
    }

    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id){
        return articleService.show(id);
    }

    //POST
    @PostMapping("/api/articles")
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto){
        //dto로(ArticleForm) 클라이언트가 보낸 json을 받아서 엔티티인 Article로 변환함
        Article created = articleService.create(dto);

        return created!=null ?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        //build()는 내용 없이 반환, body(null)로 반환해도 된다

    }

    //PATCH
    @PatchMapping("/api/articles/{id}")  //ResponseEntity에 담아서 보내면 상태 코드를 같이 담을 수 있다
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody ArticleForm dto) {

        Article updated = articleService.update(id,dto);

        return updated!=null ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    //DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {

        Article deleted = articleService.delete(id);

        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    //트랜잭션 -> 실패 -> 롤백!
    @PostMapping("/api/transaction-test")
    @Transactional //해당 메소드를 트랜잭션으로 묶는다! 도중에 실패하면 롤백 (이전상태로)
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos){
        List<Article> createdList = articleService.createArticles(dtos);
        return (createdList!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
