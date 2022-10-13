package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
    @Autowired // 스프링부트가 미리 생성한 객체를 가져다가 연결
    private ArticleRepository articleRepository;


    @GetMapping("/articles/new")
    public String newArticleForm(){

        return "articles/new";
    }

    @PostMapping("articles/create")
    public String createArticle(ArticleForm form){
        System.out.println(form.toString());

        // 1. DTO를 Entitiy로 변환해야한다
        Article article = form.toEntitiy();
        System.out.println(article.toString());


        // 2. Repository에게 Entitiy를 DB안에 저장하게 한다
        Article saved = articleRepository.save(article);
        System.out.println(saved.toString());

        return "";
    }

}
