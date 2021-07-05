package com.lzc.mapper;

import com.lzc.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Liang Zhancheng
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * @description 查询符合条件的用户列表
     * @param user 属性内容为空，则为全搜索；否则为条件搜索
     * @return 符合条件的用户列表
     */
    List<User> queryUsers(User user);

    /**
     * @return 返回允许发送的邮件列表
     */
    List<String> queryAllowEmails();

    /**
     * @description 利用包装类查询 User，可设置的为Unique键：id 和 email
     * @param user 用户对象
     * @return User
     */
    User queryUser(User user);

    User queryUserByEmail(String email);

    User queryUserById(Integer userId);

    Integer addUser(User user);

    Integer deleteUser(User user);

    Integer deleteUserByEmail(String email);

    Integer updateUser(User user);

    Integer updateUserEmail(String oldEmail, String newEmail);
}
