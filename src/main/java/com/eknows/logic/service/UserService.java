package com.eknows.logic.service;

import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;

public interface UserService {

    /**
     * 更新或保存用户
     * @param user
     * @return
     */
    JsonResult save(User user);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 更新密码
     * @param id
     * @param newPass
     * @return
     */
    boolean updatePassword(int id, String newPass);
}
