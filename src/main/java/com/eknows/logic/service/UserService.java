package com.eknows.logic.service;

import com.eknows.model.bean.common.JsonResult;
import com.eknows.model.bean.entity.User;

public interface UserService {

    /**
     * 更新或保存用户
     * @param user
     * @return
     */
    public JsonResult save(User user);
}
