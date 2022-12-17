package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.enums.AuthStatusEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.atguigu.yygh.vo.user.UserAuthVo;
import com.atguigu.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    //    用户手机号登陆接口
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
//        从loginVo获取输入的手机号，和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
//        判断手机号和验证码是否为空
        if(!StringUtils.hasLength(phone) || !StringUtils.hasLength(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
//        判断手机验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        assert redisCode != null;
        if(!redisCode.equals(code)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

//        绑定手机号
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())){
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if(null != userInfo){
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            }else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

//        如果userinfo为空，进行正常手机登录
        if(userInfo == null){
//        判断是否第一次登陆；根据手机号查询数据库，如果不存在相同手机号就是第一次登录
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(wrapper);
            if(null == userInfo){
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                this.save(userInfo);
            }

        }
//        校验是否被禁用
        if(userInfo.getStatus() == 0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
//        不是第一次,直接登录
//        返回登录信息
//        返回登录用户名
//        返回token信息
        HashMap<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name = userInfo.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        System.out.println("token: " + token);
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        return userInfo;
    }

    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

//    用户列表
    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
//        获取条件值
        String keyword = userInfoQueryVo.getKeyword();
        Integer status = userInfoQueryVo.getStatus();
        Integer authStatus = userInfoQueryVo.getAuthStatus();
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();
//        对条件值行判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(StringUtils.hasLength(keyword)){
            wrapper.eq("name", keyword);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status", status);
        }
        if(!StringUtils.isEmpty(authStatus)){
            wrapper.eq("auth_status", authStatus);
        }
        if (StringUtils.hasLength(createTimeBegin)){
            wrapper.ge("create_time", createTimeBegin);
        }
        if (StringUtils.hasLength(createTimeEnd)){
            wrapper.le("create_time", createTimeEnd);
        }
//        调用mapper的方法
        Page<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
//        编号变成对应值封装
        pages.getRecords().stream().forEach( item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }

//    编号变成对应值的封装
    private UserInfo packageUserInfo(UserInfo item) {
        item.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(item.getAuthStatus()));
//        处理用户状态
        String stautsString = item.getStatus() == 0 ? "锁定" : "正常";
        item.getParam().put("statusString" , stautsString);
        return item;
    }
}
