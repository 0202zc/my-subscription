package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.pojo.Comment;
import com.lzc.service.CommentService;
import com.lzc.util.JsonUtil;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Liang Zhancheng
 * @date 2021/7/4 10:13
 * @description 评论控制层
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;


    @GetMapping("/queryAllComments")
    public String queryAllComments(HttpServletResponse response) {
        List<Comment> comments = commentService.queryAllComments();
        if (comments == null) {
            return JsonUtil.toJsonString(response.getStatus(), "fail");
        } else {
            return JsonUtil.toJsonString(response.getStatus(), "success", comments);
        }
    }

    @GetMapping("/queryCommentsByUserId")
    public String queryCommentsByUserId(HttpServletResponse response, @RequestParam("userId") Integer userId) {
        List<Comment> comments = commentService.queryCommentsByUserId(userId);
        if (comments == null) {
            return JsonUtil.toJsonString(response.getStatus(), "未查询到数据");
        } else {
            return JsonUtil.toJsonString(response.getStatus(), "success", comments);
        }
    }

    @GetMapping("/queryCommentsByEmail")
    public String queryCommentsByEmail(HttpServletResponse response, @RequestParam("email") String email) {
        List<Comment> comments = commentService.queryCommentsByEmail(email);
        if (comments == null) {
            return JsonUtil.toJsonString(response.getStatus(), "未查询到数据");
        } else {
            return JsonUtil.toJsonString(response.getStatus(), "success", comments);
        }
    }

    @GetMapping("/queryCommentById")
    public String queryCommentById(HttpServletResponse response, @RequestParam("id") Integer id) {
        Comment comment = commentService.queryCommentById(id);
        if (comment == null) {
            return JsonUtil.toJsonString(response.getStatus(), "未查询到数据");
        } else {
            JSONObject data = new JSONObject();
            data.put(String.valueOf(id), comment);
            return JsonUtil.toJsonString(response.getStatus(), "success", data);
        }
    }

    @PostMapping("/addCommentByUserId")
    public String addComment(HttpServletResponse response, @RequestParam("userId") Integer userId, @RequestParam("note") String note) {
        Comment comment = new Comment(userId, note);
        String msg = commentService.addComment(comment) == 1 ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/addCommentByEmail")
    public String addComment(HttpServletResponse response, @RequestParam("email") String email, @RequestParam("note") String note) {
        String msg = commentService.addComment(email, note) == null ? "fail" : "success";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/deleteComment")
    public String deleteComment(HttpServletResponse response, @RequestParam("id") Integer id) {
        String msg = commentService.deleteComment(id) == 0 ? "fail" : "success";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/updateComment")
    public String updateComment(HttpServletResponse response, @RequestParam("id") Integer id, @RequestParam("userId") Integer userId, @RequestParam("note") String note) {
        String msg = commentService.updateComment(new Comment(id, userId, note)) == 0 ? "fail" : "success";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/deleteCommentByUserId")
    public String deleteCommentByUserId(HttpServletResponse response, @RequestParam("userId") Integer userId) {
        String msg = commentService.deleteCommentByUserId(userId) == 0 ? "fail" : "success";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }


}
