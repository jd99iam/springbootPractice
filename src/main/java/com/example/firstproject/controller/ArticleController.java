package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j //로깅을 위한 어노테이션
public class ArticleController {
    @Autowired // 스프링부트가 미리 생성한 객체를 가져다가 연결
    private ArticleRepository articleRepository;


    @GetMapping("/articles/new")
    public String newArticleForm(){

        return "articles/new";
    }

    @PostMapping("articles/create")
    public String createArticle(ArticleForm form){
//        System.out.println(form.toString());
        // 로깅 기능으로 println 대체하자
        log.info(form.toString());



        // 1. DTO를 Entitiy로 변환해야한다
        Article article = form.toEntitiy();
        //System.out.println(article.toString());
        log.info(article.toString());

        // 2. Repository에게 Entitiy를 DB안에 저장하게 한다
        Article saved = articleRepository.save(article);
        //System.out.println(saved.toString());
        log.info(saved.toString());

        return "redirect:/articles/"+saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id = "+id);

        //1. id로 데이터를 가져옴
        //orElse로 해당 id값이 없다면 null을 반환
        Article articleEntitiy = articleRepository.findById(id).orElse(null);


        //2. 가져온 데이터를 모델에 등록
        model.addAttribute("article",articleEntitiy);



        //3. 보여줄 페이지를 설정
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){

        //1. 모든 Article을 가져온다
        List<Article> articleEntityList = articleRepository.findAll();

        //2. 가져온 Article 묶음을 뷰로 전달한다.
        model.addAttribute("articleList",articleEntityList);

        //3. 뷰페이지를 설정한다
        return "articles/index"; //articles/index.mustache가 보이도록
    }


}
