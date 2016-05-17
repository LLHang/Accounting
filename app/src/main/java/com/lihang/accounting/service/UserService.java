package com.lihang.accounting.service;

import android.content.ContentValues;
import android.content.Context;

import com.lihang.accounting.database.dao.UserDAO;
import com.lihang.accounting.entitys.User;
import com.lihang.accounting.service.base.BaseService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class UserService extends BaseService {
    private UserDAO userDAO;

    /**
     * 构造方法
     * @param context
     */
    public UserService(Context context) {
        super(context);
        userDAO = new UserDAO(context);
    }

    /**
     * 从父类继承来的插入方法，可以对用户进行插入操作，其返回值是插入是否成功结果
     * @param user
     * @return
     */
    public boolean insertUser(User user) {
        boolean result = userDAO.insertUser(user);
        return result;
    }

    /**
     * 从父类继承来的删除方法，可以通过用户id来删除对应的用户，其返回值是删除的结果
     * @param userId
     * @return
     */
    public boolean deleteUserByUserId(int userId) {
        String condition = " and userid = " + userId;
        boolean result = userDAO.deleteUser(condition);
        return result;
    }

    /**
     * 从父类继承的对用户信息进行更新的方法，可以对用户进行更新，其返回值是更新是否成功的结果
     * @param user
     * @return
     */
    public boolean updateUser(User user) {
        String condition = " userid=" + user.getUserId();
        boolean result = userDAO.updateUser(condition, user);
        return result;
    }

    public List<User> getUsers(String condition) {
        return userDAO.getUsers(condition);
    }

    public User getUserByUserId(int userId) {
        List<User> list = userDAO.getUsers(" and userid = " + userId);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<User> getNotHideUser() {
        return userDAO.getUsers(" and state=1");
    }



    public boolean isExistUserByUserName(String userName, Integer userId) {
        String condition = "and username = '" + userName + "'";
        if (userId != null) {
            condition += "and userId <>" + userId;
        }

        List<User> list = userDAO.getUsers(condition);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    //根据用户ID隐藏该用户
    public boolean hideUserByUserId(int userId) {
        String condition = "userid = " + userId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        return userDAO.updateUser(condition, contentValues);
    }

    public String getUserNameByUserId(String payoutUserId) {
        String userId[] = payoutUserId.split(",");
        String name = "";
        for (int i = 0; i < userId.length; i++) {
            name += getUsers(" and userId = " + userId[i]).get(0).getUserName()+",";
        }
        return name;
    }

    public HashMap<String,String> getUserToHashMap() {
        List<User> users = userDAO.getUsers(" and state = 1");
        HashMap<String, String> hashUsers = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            hashUsers.put(users.get(i).getUserId() + "", users.get(i).getUserName());
        }
        return hashUsers;
    }
}
