package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.lzc.pojo.User;
import com.lzc.service.UserService;
import com.lzc.util.JsonUtil;
import com.lzc.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Liang Zhancheng
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/operateUser")
    public String operateUser(HttpServletResponse response, @RequestParam("oper") String op, String id, String email, String isAllowed, String userName, Integer role) {
        if ("add".equals(op)) {
            return this.addUser(email, isAllowed, userName.trim(), role);
        } else if ("edit".equals(op)) {
            if ("".equals(id) || !JsonUtil.isNumeric(id)) {
                return JsonUtil.toJsonString(response.getStatus(), "id 不正确");
            }
            return this.updateUser(Integer.parseInt(id), userName == null ? null : userName.trim(), email == null ? null : email.trim(), role, isAllowed);
        } else if ("del".equals(op)) {
            String[] ids = id.split(",");
            String msg = "";
            for (String i : ids) {
                msg = this.deleteUser(Integer.parseInt(i));
            }
            return msg;
        }
        return null;
    }

    @GetMapping("/queryUsers")
    public String queryUsers(HttpServletResponse response, String id, String userName, String email, String isAllowed, String role) {
        if (id != null && !JsonUtil.isNumeric(id)) {
            return JsonUtil.toJsonString(response.getStatus(), "'id' 格式不正确");
        }
        if (isAllowed != null && !("0".equals(isAllowed) || "1".equals(isAllowed))) {
            return JsonUtil.toJsonString(response.getStatus(), "'isAllowed' 格式不正确");
        }
        if (role != null && !("0".equals(role) || "1".equals(role))) {
            return JsonUtil.toJsonString(response.getStatus(), "'role' 格式不正确");
        }

        List<User> users = userService.queryUsers(new User(id == null ? null : Integer.parseInt(id), userName == null ? null : userName.trim(), email == null ? null : email.trim(), isAllowed == null ? null : Integer.parseInt(isAllowed), role == null ? null : Integer.parseInt(role)));

        return JSON.toJSONString(users);
    }

    @GetMapping("/queryUser/{userId}")
    public String queryUser(@PathVariable("userId") Integer userId) {
        User user = new User(userId);
        return JSON.toJSONString(userService.queryUser(user));
    }

    @GetMapping("/queryUserByEmail/{email}")
    public String queryUserByEmail(@PathVariable("email") String email) {
        return JSON.toJSONString(userService.queryUserByEmail(email.trim()));
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam("email") String email, @RequestParam("isAllowed") String flag, @RequestParam("userName") String userName, @RequestParam("role") Integer role) {
        if (userService.queryUserByEmail(email.trim()) != null) {
            return "该邮箱已存在！";
        }
        Integer isAllowed = ("Yes".equals(flag) || "1".equals(flag)) ? 1 : 0;
        User user = new User(userName.trim(), email.trim(), isAllowed, role);
        return userService.addUser(user) != null ? "添加成功" : "添加失败";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Integer id) {
        User user = new User();
        user.setId(id);
        return userService.deleteUser(user) == 1 ? "删除成功" : "删除失败";
    }

    @PostMapping("/deleteUserByEmail")
    public String deleteUserByEmail(@RequestParam("email") String email) {
        return userService.deleteUserByEmail(email.trim(), userService.queryUserByEmail(email).getId()) == 1 ? "删除成功" : "删除失败";
    }

    @PostMapping("/updateUserByEmail")
    public String updateUser(@RequestParam("oldEmail") String oldEmail, @RequestParam("userName") String userName, @RequestParam("newEmail") String newEmail, @RequestParam("role") Integer role, @RequestParam("isAllowed") Integer isAllowed) {
        User user = userService.queryUserByEmail(oldEmail.trim());
        if (user == null) {
            return "此邮箱尚未注册";
        } else if (userService.queryUserByEmail(newEmail.trim()) != null) {
            return "此邮箱已被注册";
        }
        user.setUserName(userName.trim());
        user.setEmail(newEmail.trim());
        user.setIsAllowed(isAllowed);
        user.setRole(role);
        return userService.updateUser(user) == null ? "修改失败" : "修改成功";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("id") Integer id, @RequestParam("userName") String userName, @RequestParam("email") String newEmail, @RequestParam("role") Integer role, @RequestParam("isAllowed") String flag) {
        User user = userService.queryUser(new User(id));
        User validUser = userService.queryUserByEmail(newEmail.trim());
        if (user == null) {
            return "查无此用户";
        } else if (validUser != null && !validUser.getId().equals(id)) {
            return "此邮箱已被注册";
        }
        Integer isAllowed = ("Yes".equals(flag) || "1".equals(flag)) ? 1 : 0;
        user.setUserName(userName);
        user.setEmail(newEmail.trim());
        user.setIsAllowed(isAllowed);
        user.setRole(role);
        return userService.updateUser(user) == null ? "修改失败" : "修改成功";
    }

    @PostMapping("/updateUserEmail")
    public String updateUserEmail(@RequestParam("oldEmail") String oldEmail, @RequestParam("newEmail") String newEmail) {
        oldEmail = oldEmail.trim();
        newEmail = newEmail.trim();

        if ("".equals(oldEmail) || "".equals(newEmail)) {
            return "请输入非空字符";
        }

        if (userService.queryUserByEmail(oldEmail) == null) {
            return "此邮箱尚未注册";
        } else if (userService.queryUserByEmail(newEmail) != null) {
            return "此邮箱已被注册";
        }
        User user = userService.updateUserEmail(oldEmail, newEmail);
        return user != null ? "修改成功" : "修改失败";
    }

    @GetMapping("/validateEmail/{email}")
    public String validateEmail(@PathVariable("email") String email) {
        boolean result = userService.queryUserByEmail(email.trim()) == null;
        return result ? "true" : "false";
    }

    @PostMapping("/validateEmail")
    public String validateEmail1(@RequestParam("email") String email) {
        boolean result = userService.queryUserByEmail(email.trim()) == null;
        return result ? "true" : "false";
    }
}
