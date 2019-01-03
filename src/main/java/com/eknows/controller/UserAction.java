package com.eknows.controller;

import com.eknows.logic.service.UserService;
import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;
import com.eknows.model.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author zfh
 * @version 1.0
 * @date 2018/12/26 14:03
 */
@RestController
@RequestMapping(value = "user")
public class UserAction extends CommonAction {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
       User user = getUserFromSession(request);
       if (user == null) {
           return new JsonResult(false, "请先登录");
       }

       JsonResult jsonResult = new JsonResult(true);
       List<User> userList = userDAO.findAll();
       jsonResult.put("userList", userList);
       return jsonResult;
    }

    @PostMapping(value = "save")
    public JsonResult add(User user) {
        // 新增或编辑用户
        return userService.save(user);
    }

    @DeleteMapping(value = "delete")
    public JsonResult delete(Integer id, HttpServletRequest request) {
        if (id == null) {
            return new JsonResult(false, "操作错误");
        }
        User user = getUserFromSession(request);
        if (user == null || user.getRole() != 1) {
            return new JsonResult(false, "没有权限");
        }

        User itemUser = userDAO.findById(id);
        if (itemUser == null) {
            return new JsonResult(false, "用户不存在");
        } else if (itemUser.getId().equals(user.getId())) {
            return new JsonResult(false, "你是要自己删除自己嘛？");
        }

        boolean res = userDAO.deleteById(id);
        return new JsonResult(res, res ? null : "删除失败");
    }

}
