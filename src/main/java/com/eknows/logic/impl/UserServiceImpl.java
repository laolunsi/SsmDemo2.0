package com.eknows.logic.impl;

import com.eknows.logic.service.UserService;
import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;
import com.eknows.model.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.xml.ws.Action;
import java.util.Date;

/**
 * @author zfh
 * @version 1.0
 * @date 2018/12/28 17:11
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public JsonResult save(User user) {
        JsonResult jsonResult = verifyUserForm(user);
        if (!jsonResult.getSuccess()) {
            return jsonResult;
        }

        if (user.getId() != null) {
            // 更新用户信息
            User oldUser = userDAO.findById(user.getId());
            if (oldUser == null) {
                return new JsonResult(false, "该用户不存在或已被删除");
            }
            boolean res = userDAO.update(user);
            return new JsonResult(res);
        } else {
            userDAO.insert(user);
            return new JsonResult(user.getId() != null, user.getId() != null ? "保存成功" : "保存失败");
        }
    }

    //
    /**
     * 检验要保存的用户数据
     * 如果合法，则设置其他默认值
     * @param user
     * @return
     */
    private static JsonResult verifyUserForm(User user) {
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
