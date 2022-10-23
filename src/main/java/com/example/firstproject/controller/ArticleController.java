package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j //로깅을 위한 어노테이션
public class ArticleController {
    @Autowired // 스프링부트가 미리 생성한 객체를 가져다가 연결
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;


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
        List<CommentDto> commentDtos = commentService.comments(id);


        //2. 가져온 데이터를 모델에 등록
        model.addAttribute("article",articleEntitiy);
        model.addAttribute("commentDtos",commentDtos);



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

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        //수정할 데이터 가져와야한다
        Article articleEntitiy = articleRepository.findById(id).orElse(null);

        //모델에 데이터 등록
        model.addAttribute("article",articleEntitiy);

        return "articles/edit";
    }


    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());


        //1. DTO를 엔티티로 변환한다
        Article articleEntity  = form.toEntitiy();
        log.info(articleEntity.toString());

        //2. 엔티티를 DB로 저장한다
        //2-1 : DB에서 기존 데이터를 가져온다
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        //2-2 : 기존 데이터 값을 수정한다
        if (target!=null) {
            articleRepository.save(articleEntity); //엔티티가 db로 갱신

        }

        //3. 수정 결과 페이지로 리다이렉트 한다

        return "redirect:/articles/"+articleEntity.getId();
    }


    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다.");

        //1. 삭제 대상을 가져온다
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        //2. 대상을 삭제한다.
        if (target!=null){
            articleRepository.delete(target);
            // 한번 쓰고 사라지는 휘발성
            rttr.addFlashAttribute("msg","삭제가 완료되었습니다");
        }



        //3. 결과 페이지로 리다이렉트한다
        return "redirect:/articles";
    }

}
