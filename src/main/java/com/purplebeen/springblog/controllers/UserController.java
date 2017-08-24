package com.purplebeen.springblog.controllers;

import com.purplebeen.springblog.beans.Category;
import com.purplebeen.springblog.beans.User;
import com.purplebeen.springblog.repositories.CategoryDao;
import com.purplebeen.springblog.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping("/resume")
    public String resume(Model model, HttpSession session) {
        model.addAttribute("categoryList",categoryDao.findAll());
        model.addAttribute("session",session);
        return "resume";
    }

    @RequestMapping(value = "/signin", method= RequestMethod.POST)
    public String signin(HttpServletRequest httpServletReq, User user, Model model) {
        String password1 = httpServletReq.getParameter("password");
        String password2 = httpServletReq.getParameter("password2");
        if(password1.equals(password2)) {
            userDao.save(user);
            return "redirect:/user/login";
        } else {
            model.addAttribute("msg", "패스워드가 일치하지 않습니다.");
            model.addAttribute("url", "/user/resume");
            return "Error";
        }

    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginForm(Model model,HttpSession session) {
        List<Category> categoryList = categoryDao.findAll();
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("session",session);
        return "loginForm";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login(HttpSession session, HttpServletRequest httpServletReq, Model model) {
        String url="";
        String username = httpServletReq.getParameter("username");
        String password = httpServletReq.getParameter("password");

        try {
            User user = userDao.findAll().stream()
                    .filter(u -> u.getUserID().equals(username) && u.getPassword().equals(password))
                    .collect(Collectors.toList()).get(0);
            session.setAttribute("userid", username);
            url = "redirect:/post/show/list";
        } catch(IndexOutOfBoundsException e) {
            model.addAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
            model.addAttribute("url", "/user/loginForm");
            url = "Error";
        }
        return url;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userid");
        return "redirect:/post/show/list";
    }

}
