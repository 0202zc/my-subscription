package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.lzc.pojo.SubscriptionDO;
import com.lzc.service.CommentService;
import com.lzc.service.SubscriptionService;
import com.lzc.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Liang Zhancheng
 */
@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {
    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/queryAllSubscriptions")
    public List<SubscriptionDO> queryAllSubscriptions() {
        return subscriptionService.queryAllSubscriptions();
    }

    @GetMapping("/querySubscriptionsByEmail/{email}")
    public String querySubscriptionsByEmail(@PathVariable("email") String email) {
        return JSON.toJSONString(subscriptionService.querySubscriptionsByEmail(email));
    }

    @GetMapping("/querySubscriptionsByUserId/{userId}")
    public String querySubscriptionsByUserId(@PathVariable("userId") Integer userId) {
        return JSON.toJSONString(subscriptionService.querySubscriptionsByUserId(userId));
    }

    @PostMapping("/insertSubscription")
    public String insertSubscription(HttpServletResponse response, @RequestParam("userName") String userName, @RequestParam("email") String email, @RequestParam("serviceId") String serviceId, @RequestParam("sendTime") String sendTime, @RequestParam("allowSend") Integer allowSend, @RequestParam("cancelAlert") String cancelAlert, @RequestParam("comment") String comment) {
        if (serviceId == null || serviceId.length() == 0) {
            return JSON.toJSONString(JsonUtils.FAIL_STRING);
        }

        userName = userName.trim();
        email = email.trim();
        comment = comment.trim();

        String[] services = serviceId.split(",");
        String[] times = sendTime.split(",");
        String[] cancel = cancelAlert.split(",");
        String msg1 = "", msg2 = "";
        boolean flag = true;

        for (String id : services) {
            if (sendTime != null && sendTime.length() > 0) {
                // 批量订阅服务
                for (String time : times) {
                    SubscriptionDO subscription = new SubscriptionDO(Integer.parseInt(id), time, allowSend);
                    msg1 = subscriptionService.insertSubscription(subscription, userName, email) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
                    if (JsonUtils.SUCCESS_STRING.equals(msg1)) {
                        flag = false;
                    }
                }
            }
            if (cancelAlert != null && cancelAlert.length() > 0) {
                // 批量退订服务
                for (String time : cancel) {
                    msg2 = subscriptionService.deleteSubscription(email, Integer.parseInt(id), time) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
                    if (JsonUtils.SUCCESS_STRING.equals(msg2)) {
                        flag = false;
                    }
                }
            }
        }

        if (comment != null && comment.length() > 0) {
            String msg = commentService.addComment(email, comment) != null ? email + " 的comment提交成功" : email + " 的comment提交失败";
            JsonUtils.toJsonString(response.getStatus(), msg);
        }

        return JSON.toJSONString(flag ? JsonUtils.FAIL_STRING : JsonUtils.SUCCESS_STRING);
    }

    @PostMapping("/deleteSubscription")
    public String deleteSubscription(@RequestParam("email") String email, @RequestParam("serviceId") String serviceId, @RequestParam("sendTime") String sendTime) {
        email = email.trim();
        sendTime = sendTime.trim();

        String[] services = serviceId.split(",");
        String[] times = sendTime.split(",");
        boolean flag = true;
        for (String id : services) {
            for (String time : times) {
                if (subscriptionService.deleteSubscription(email, Integer.parseInt(id), time) != 1){
                    flag = false;
                }
            }
        }
        return JSON.toJSONString(flag ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING);
    }


    @PostMapping("/updateSubscription")
    public String updateSubscription(@RequestParam("oldEmail") String oldEmail, @RequestParam("oldServiceId") Integer oldServiceId, @RequestParam("oldSendTime") String oldSendTime, @RequestParam("newEmail") String newEmail, @RequestParam("newServiceId") Integer newServiceId, @RequestParam("newSendTime") String newSendTime, @RequestParam("allowSend") Integer allowSend) {
        oldEmail = oldEmail.trim();
        oldSendTime = oldSendTime.trim();
        newEmail = newEmail.trim();
        newSendTime = newSendTime.trim();

        String msg = subscriptionService.updateSubscription(oldEmail, oldServiceId, oldSendTime, newServiceId, newSendTime, allowSend, newEmail) == 1 ? "修改成功" : "修改失败";
        System.out.println(msg);
        return JSON.toJSONString(msg);
    }

    @PostMapping("/updateSubsState")
    public String updateSubsState(@RequestParam("id") Integer id) {
        if (id == null) {
            return JSON.toJSONString(JsonUtils.FAIL_STRING);
        }
        String msg = subscriptionService.updateSubsState(id) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JSON.toJSONString(msg);
    }
}
