package com.purplebeen.springblog.controllers;

import com.purplebeen.springblog.beans.Category;
import com.purplebeen.springblog.beans.Post;
import com.purplebeen.springblog.repositories.CategoryDao;
import com.purplebeen.springblog.repositories.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private PostDao postDao;

    @ResponseBody
    @RequestMapping(value="/category/add", method= RequestMethod.POST)
    public Category add(@RequestParam(value="categoryName", required=true) String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        category.setRegDate(new Date());;
        return categoryDao.save(category);
    }

    @RequestMapping(value="/category/{categoryId}", method=RequestMethod.GET)
    public String categorySearch(@PathVariable String categoryId, Model model, HttpSession session,
                                 @PageableDefault(sort = { "Id" }, direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                                 @RequestParam(value = "page", required = false, defaultValue = "0") int pageNum) throws UnsupportedEncodingException {
        System.out.println(categoryId);
        List<Post> postList = postDao.findByCategory(categoryDao.findOne(categoryId));
        Sort sort = new Sort(Sort.Direction.DESC, "regDate");
        PageRequest pageRequest = new PageRequest(pageNum,5,sort);
        Page<Post> page = postDao.findAll(pageRequest);

        if(postList == null) {
            model.addAttribute("msg", "카테고리에 글이 없습니다");
            model.addAttribute("url", "/post/show/list");
            return "Error";
        }

        Page<Post> postPage = new PageImpl<Post>(postList,pageable,postList.size());
        List<Category> categoryList = categoryDao.findAll();
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("session",session);
        model.addAttribute("userid", session.getAttribute("userid"));
        model.addAttribute("postPage", postPage);
        model.addAttribute("categoryId", categoryId);
        return "blog";
    }
}
