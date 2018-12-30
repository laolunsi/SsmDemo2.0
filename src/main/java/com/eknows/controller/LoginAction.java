package com.eknows.controller;

import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;
import com.eknows.model.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 注：由于功能比较简单，故省略了service层，
 * 由Controller直接调用DAO层
 * @author zfh
 * @version 1.0
 * @date 2018/12/26 14:33
 */
@Controller
@RequestMapping(value = "")
public class LoginAction extends CommonAction {

    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserFromSession(request);
        if (user != null && user.getId() != null) {
            try {
                response.sendRedirect(request.getContextPath() + "/admin"); // 重定向
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ModelAndView("index");
    }

    @PostMapping(value = "login")
    @ResponseBody
    public JsonResult login(String name, String password, HttpServletRequest request) {
        if (StringUtils.isEmpty(name)) {
            return new JsonResult(false, "用户名不能为空");
        } else if (StringUtils.isEmpty(password)) {
            return new JsonResult(false, "密码不能为空");
        }

        User user = userDAO.find(name, password);
        if (user != null) {
            user.setLastLoginTime(new Date());
            userDAO.update(user);
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("user", user);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(12 * 60 * 60);
            return jsonResult;
        }
        return new JsonResult(false, "用户名或密码错误");
    }

    @PostMapping(value = "register")
    @ResponseBody
    public JsonResult register(User user, HttpServletRequest request) {
        JsonResult jsonResult = verifyUserForm(user);
        if (!jsonResult.getSuccess()) {
            return jsonResult;
        }
        user.setRole(0);
        user.setLastLoginTime(new Date());
        userDAO.save(user);
        user = userDAO.findById(user.getId());
        if (user.getId() != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            jsonResult.put("user", user);
            return jsonResult;
        } else {
            return new JsonResult(false, "保存失败");
        }
    }

    @GetMapping(value = "logout")
    @ResponseBody
    public JsonResult logout(Integer id, HttpServletRequest request) {
        if (id == null) {
            return new JsonResult(false, "操作错误");
        }

        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") != null) {
            session.removeAttribute("user");
        }

        return new JsonResult(true);
    }

    @GetMapping(value = "admin")
    public ModelAndView admin(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserFromSession(request);
        if (user == null) {
            try {
                response.sendRedirect(request.getContextPath() + ""); // 重定向至登录页
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ModelAndView("admin");
    }

}
