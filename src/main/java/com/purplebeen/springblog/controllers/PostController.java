package com.purplebeen.springblog.controllers;

import com.purplebeen.springblog.beans.Category;
import com.purplebeen.springblog.beans.Comment;
import com.purplebeen.springblog.beans.Post;
import com.purplebeen.springblog.repositories.CategoryDao;
import com.purplebeen.springblog.repositories.CommentDao;
import com.purplebeen.springblog.repositories.PostDao;
import com.purplebeen.springblog.utills.MarkdownRenderer;
import com.purplebeen.springblog.utills.UUIDUtill;
import com.purplebeen.springblog.utills.XSSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostDao postDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CommentDao commentDao;

    @RequestMapping(value="/write", method= RequestMethod.GET)
    public String form(Post Post, Model model, HttpSession session) {
        if((session.getAttribute("userid") != null)) {
            List<Category> categoryList = categoryDao.findAll();
            model.addAttribute("categoryList",categoryList);
            model.addAttribute("session",session);
            model.addAttribute("url","/post/write");
            return "form";
        } else {
            model.addAttribute("msg", "로그인 해 주세요!");
            model.addAttribute("url","/post/show/list");
            return "alertform";
        }
    }

    @RequestMapping(value="/write", method = RequestMethod.POST)
    public String write(Post post,HttpSession session, HttpServletRequest httpServletReq) throws UnsupportedEncodingException {
        String categoryName = httpServletReq.getParameter("categoryName");
        post.setId(UUIDUtill.getUUID());
        post.setUrl(URLEncoder.encode(post.getTitle(),"UTF-8"));
        post.setCategory(categoryDao.findOne(categoryName));
        post.setRegDate(new Date());
        post.setAurthor(session.getAttribute("userid").toString());
        if(post.getImageUrl().equals(""))
            post.setImageUrl("https://clsimplex.com/images/releases/headers/quality-code.jpg");
        postDao.save(post);
        return "redirect:/post/view/" + post.getUrl();
    }


    @RequestMapping(value="/write/comment", method=RequestMethod.POST)
    public String commentWrite(HttpSession session, HttpServletRequest httpServletRequest, Comment comment, Model model) throws UnsupportedEncodingException {
        String postId = httpServletRequest.getParameter("postId");
        Post post = null;
        if(!(comment.getContent().equals(""))) {
            comment.setName(session.getAttribute("userid").toString());
            comment.setContent(XSSFilter.filter(comment.getContent()));
            post = postDao.findOne(postId);
            post.getCommentList().add(comment);
            postDao.save(post);
            return "redirect:/post/view/" + post.getUrl();
        } else {
            model.addAttribute("msg","댓글 내용을 입력해주세요!");
            model.addAttribute("url","/post/"+URLEncoder.encode(post.getTitle(),"utf-8"));
            return "alertform";
        }
    }

    @RequestMapping("/aboutme")
    public String getAbout(Model model) {
        List<Category> categoryList = categoryDao.findAll();
        model.addAttribute("categoryList", categoryList);        
        return "aboutme";
    }

    @RequestMapping("/show/list")
    public String list(Model model,HttpSession session,
                       @RequestParam(value="category", required=false, defaultValue="0") int categoryId,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int pageNum) {
        Sort sort = new Sort(Sort.Direction.DESC, "regDate");
        List<Post> postList = postDao.findAll(sort);
        PageRequest pageRequest =  new PageRequest(pageNum, 5,sort);
        Page<Post> postPage = postDao.findAll(pageRequest);
        List<Category> categoryList = categoryDao.findAll();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("userid", session.getAttribute("userid"));
        model.addAttribute("postPage", postPage);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("session",session);
        return "blog";
    }
    @RequestMapping("/view/{id}")
    public String view(Model model, @PathVariable String id, HttpSession session) throws UnsupportedEncodingException {
        id = URLDecoder.decode(id,"UTF-8");
        Post post = postDao.findByTitle(id);
        post.setContent(MarkdownRenderer.render(post.getContent()));
        List<Category> categoryList = categoryDao.findAll();
        model.addAttribute("session",session);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("userid",session.getAttribute("userid"));
        if(post.getAurthor() != null && post.getAurthor().equals(session.getAttribute("userid"))) {
            model.addAttribute("id","true");
        } else
            model.addAttribute("id","false");
        model.addAttribute("post", post);
        return "post";
    }

    @RequestMapping("/{title}/delete")
    public String delete(@PathVariable String title, HttpSession session, Model model) throws UnsupportedEncodingException {
        title = URLDecoder.decode(title,"UTF-8");
        Post post = postDao.findByTitle(title);
        if(post.getAurthor() != null && post.getAurthor().equals(session.getAttribute("userid"))) {
            postDao.delete(post);;
            return "redirect:/post/show/list";
        } else {
            model.addAttribute("msg","권한이 없습니다!");
            model.addAttribute("url","/post/show/list");
            return "alertform";
        }

    }

    @RequestMapping(value = "/{title}/edit", method = RequestMethod.GET)
    public String editor(Model model, @PathVariable String title,HttpSession session) throws UnsupportedEncodingException {
        title = URLDecoder.decode(title,"UTF-8");
        Post post = postDao.findByTitle(title);
        if(post.getAurthor() != null && post.getAurthor().equals(session.getAttribute("userid"))) {
            model.addAttribute("post", post);
            List<Category> categoryList = categoryDao.findAll();
            model.addAttribute("categoryList",categoryList);
            model.addAttribute("url","/post/"+ post.getUrl() +"/edit");
            return "form";
        } else {
            model.addAttribute("msg", "권한이 없습니다!");
            model.addAttribute("url","/post/show/list");
            return "alertform";
        }
    }

    @RequestMapping(value = "{title}/edit", method = RequestMethod.POST)
    public String edit(@Valid Post post, HttpSession session, HttpServletRequest httpServletReq) throws UnsupportedEncodingException {
        String categoryName = httpServletReq.getParameter("categoryName");
        String id = httpServletReq.getParameter("id");

        Post save = postDao.findOne(id);
        save.setTitle(post.getTitle());
        save.setUrl(URLEncoder.encode(post.getTitle(),"UTF-8"));
        save.setContent(post.getContent());
        save.setCategory(categoryDao.findCategoryByName(categoryName));
        save.setAurthor(session.getAttribute("userid").toString());
        postDao.save(save);

        return "redirect:/post/view/" + save.getUrl();
    }
}
