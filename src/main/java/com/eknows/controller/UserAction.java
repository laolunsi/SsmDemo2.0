package com.eknows.controller;

import com.eknows.logic.service.UserService;
import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(value = "list")
    @ResponseBody
    public JsonResult list(HttpServletRequest request) {
       User user = getUserFromSession(request);
       if (user == null) {
           return new JsonResult(false, "请先登录");
       }

       JsonResult jsonResult = new JsonResult(true);
       List<User> userList = userService.findAll();
       jsonResult.put("userList", userList);
       return jsonResult;
    }

    @PostMapping(value = "save")
    public JsonResult add(User user) {
        // 新增或编辑用户
        user =  userService.save(user);
        if (user != null && user.getId() != null) {
            return new JsonResult(true);
        }
        return new JsonResult(false, "保存失败");
    }

    @PutMapping(value = "change_password")
    public JsonResult changePassword(HttpServletRequest request, String oldPass, String newPass, String reNewPass) {
        User user = getUserFromSession(request);
        if (user == null) {
            return new JsonResult(false, "登录过期");
        }
        user = userService.findById(user.getId());
        if (user == null) {
            return new JsonResult(false, "用户不存在或已被删除");
        } else if (!user.getPassword().equals(oldPass)) {
            return new JsonResult(false, "原密码错误");
        }

        if (StringUtils.isEmpty(oldPass)) {
            return new JsonResult(false, "原密码不能为空");
        }

        if (StringUtils.isEmpty(newPass)) {
            return new JsonResult(false, "新密码不能为空");
        }

        if (!newPass.equals(reNewPass)) {
            return new JsonResult(false, "两次输入的密码不一致");
        }

        if (oldPass.equals(newPass)) {
            return new JsonResult(false, "新密码不能与旧密码相同");
        }

        boolean res = userService.updatePassword(user.getId(), newPass);
        return new JsonResult(res, res ? null : "修改失败");
    }

    @DeleteMapping(value = "delete/{id}")
    public JsonResult delete(@PathVariable Integer id, HttpServletRequest request) {
        if (id == null) {
            return new JsonResult(false, "操作错误");
        }
        User user = getUserFromSession(request);
        if (user == null || user.getRole() != 1) {
            return new JsonResult(false, "没有权限");
        }

        boolean res = userService.delete(id);
        return new JsonResult(res, res ? null : "删除失败");
    }

}
