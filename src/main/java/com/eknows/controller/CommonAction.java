package com.eknows.controller;

import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author zfh
 * @version 1.0
 * @date 2018/12/28 15:16
 */
public class CommonAction {
    /**
     * 从session中获取user
     * @param request
     */
    public static User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return (User)session.getAttribute("user");
        }
        return null;
    }

    /**
     * 检验要保存的用户数据
     * 如果合法，则设置其他默认值
     * @param user
     * @return
     */
    public static JsonResult verifyUserForm(User user) {
        if (user == null) {
            return new JsonResult(false, "操作错误");
        }

        if (user.getId() == null) {
            // 仅对新增用户进行判定
            if (StringUtils.isEmpty(user.getName())) {
                return new JsonResult(false, "用户名不能为空");
            } else if (StringUtils.isEmpty(user.getPassword())) {
                return new JsonResult(false, "密码不能为空");
            }
            user.setRole(0); // 设置默认为普通用户
            user.setLastLoginTime(new Date()); // 设置最新的登录时间
        }
        return new JsonResult(true);
    }
}
