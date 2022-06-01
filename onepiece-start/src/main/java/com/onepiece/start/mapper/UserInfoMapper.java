package com.onepiece.start.mapper;

import com.onepiece.common.pojo.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * @描述 用户基本信息相关的Mapper接口
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-01
 */
@Repository
public interface UserInfoMapper {

    UserInfo getUserInfo();
}
