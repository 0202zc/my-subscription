package com.lzc.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzc.pojo.CommentDO;
import com.lzc.service.CommentService;
import com.lzc.util.JsonUtils;
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
        List<CommentDO> comments = commentService.queryAllComments();
        if (comments == null) {
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.FAIL_STRING);
        } else {
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, comments);
        }
    }

    @GetMapping("/queryCommentsByUserId")
    public String queryCommentsByUserId(HttpServletResponse response, @RequestParam("userId") Integer userId) {
        List<CommentDO> comments = commentService.queryCommentsByUserId(userId);
        if (comments == null) {
            return JsonUtils.toJsonString(response.getStatus(), "未查询到数据");
        } else {
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, comments);
        }
    }

    @GetMapping("/queryCommentsByEmail")
    public String queryCommentsByEmail(HttpServletResponse response, @RequestParam("email") String email) {
        List<CommentDO> comments = commentService.queryCommentsByEmail(email);
        if (comments == null) {
            return JsonUtils.toJsonString(response.getStatus(), "未查询到数据");
        } else {
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, comments);
        }
    }

    @GetMapping("/queryCommentById")
    public String queryCommentById(HttpServletResponse response, @RequestParam("id") Integer id) {
        CommentDO comment = commentService.queryCommentById(id);
        if (comment == null) {
            return JsonUtils.toJsonString(response.getStatus(), "未查询到数据");
        } else {
            JSONObject data = new JSONObject();
            data.put(String.valueOf(id), comment);
            return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
        }
    }

    @PostMapping("/addCommentByUserId")
    public String addComment(HttpServletResponse response, @RequestParam("userId") Integer userId, @RequestParam("note") String note) {
        CommentDO comment = new CommentDO(userId, note);
        String msg = commentService.addComment(comment) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/addCommentByEmail")
    public String addComment(HttpServletResponse response, @RequestParam("email") String email, @RequestParam("note") String note) {
        String msg = commentService.addComment(email, note) == null ? JsonUtils.FAIL_STRING : JsonUtils.SUCCESS_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/deleteComment")
    public String deleteComment(HttpServletResponse response, @RequestParam("id") Integer id) {
        String msg = commentService.deleteComment(id) == 0 ? JsonUtils.FAIL_STRING : JsonUtils.SUCCESS_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/updateComment")
    public String updateComment(HttpServletResponse response, @RequestParam("id") Integer id, @RequestParam("userId") Integer userId, @RequestParam("note") String note) {
        String msg = commentService.updateComment(new CommentDO(id, userId, note)) == 0 ? JsonUtils.FAIL_STRING : JsonUtils.SUCCESS_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/deleteCommentByUserId")
    public String deleteCommentByUserId(HttpServletResponse response, @RequestParam("userId") Integer userId) {
        String msg = commentService.deleteCommentByUserId(userId) == 0 ? JsonUtils.FAIL_STRING : JsonUtils.SUCCESS_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }


}
