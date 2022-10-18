package com.example.firstproject.api;

import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    //PATCH

    //DELETE

}
