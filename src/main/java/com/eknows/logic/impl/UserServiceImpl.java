package com.eknows.logic.impl;

import com.eknows.logic.service.UserService;
import com.eknows.model.bean.entity.User;
import com.eknows.model.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 原则上，service层将信任Controller层传递的数据
 * 数据验证将由controller层完成
 * @author zfh
 * @version 1.0
 * @date 2018/12/28 17:11
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            // 更新用户信息
            User oldUser = userDAO.findById(user.getId());
            if (oldUser == null) {
                return null;
            }
            return userDAO.update(user) ? userDAO.findById(user.getId()) : null; // 更新成功则返回user，失败则返回null
        } else {
            userDAO.insert(user);
            return user.getId() != null ? userDAO.findById(user.getId()) : null; // 保存成功则返回user，失败则返回null
        }
    }

    @Override
    public User handleLogin(String name, String password) {
        User user = userDAO.find(name, password);
        if (user != null) {
            user.setLastLoginTime(new Date());
            userDAO.update(user);
        }
        return user;
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean updatePassword(int id, String newPass) {
        User user = new User();
        user.setId(id);
        user.setPassword(newPass);
        return userDAO.update(user);
    }

    @Override
    public boolean delete(int id) {
        User user = userDAO.findById(id);
        if (user == null) {
            return false;
        }

        return userDAO.deleteById(id);
    }
}
