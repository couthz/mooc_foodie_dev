package com.zhc.service.impl;

import com.zhc.enums.Sex;
import com.zhc.mapper.StuMapper;
import com.zhc.mapper.UsersMapper;
import com.zhc.pojo.Stu;
import com.zhc.pojo.Users;
import com.zhc.pojo.bo.UserBo;
import com.zhc.service.StuService;
import com.zhc.service.UserService;
import com.zhc.utils.DateUtil;
import com.zhc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        /*构建条件*/
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);

        Users result = usersMapper.selectOneByExample(userExample);

        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBo userBO) {

        String userId = sid.nextShort();
        Users user = new Users();
        //全局唯一的id
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        //默认头像
        user.setFace(USER_FACE);
        //时间日期转换  jsr??
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //不推荐写死数据
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        //为了在页面显示一些基本信息
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        /*构建条件*/
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        try {
            userCriteria.andEqualTo("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersMapper.selectOneByExample(userExample);
    }
}
