package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.lzc.pojo.Subscription;
import com.lzc.service.CommentService;
import com.lzc.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Subscription> queryAllSubscriptions() {
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
    public String insertSubscription(@RequestParam("userName") String userName, @RequestParam("email") String email, @RequestParam("serviceId") String serviceId, @RequestParam("sendTime") String sendTime, @RequestParam("allowSend") Integer allowSend, @RequestParam("cancelAlert") String cancelAlert, @RequestParam("comment") String comment) {
        if ("".equals(serviceId)) {
            return JSON.toJSONString("failed");
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
            if (!"".equals(sendTime)) {
                // 批量订阅服务
                for (String time : times) {
                    Subscription subscription = new Subscription(Integer.parseInt(id), time, allowSend);
                    msg1 = subscriptionService.insertSubscription(subscription, userName, email) == 1 ? "success" : "failed";
                    System.out.println(msg1);
                    if ("success".equals(msg1)) {
                        flag = false;
                    }
                }
            }
            if (!"".equals(cancelAlert)) {
                // 批量退订服务
                for (String time : cancel) {
                    msg2 = subscriptionService.deleteSubscription(email, Integer.parseInt(id), time) == 1 ? "success" : "failed";
                    System.out.println(msg2);
                    if ("success".equals(msg2)) {
                        flag = false;
                    }
                }
            }
        }

        if (comment != null && !"".equals(comment)) {
            System.out.println(commentService.addComment(email, comment) != null ? email + " 的comment提交成功" : email + " 的comment提交失败");
        }

        return JSON.toJSONString(flag ? "failed" : "success");
    }

    @PostMapping("/deleteSubscription")
    public String deleteSubscription(@RequestParam("email") String email, @RequestParam("serviceId") String serviceId, @RequestParam("sendTime") String sendTime) {
        email = email.trim();
        sendTime = sendTime.trim();

        String[] services = serviceId.split(",");
        String[] times = sendTime.split(",");
        String msg = "";
        for (String id : services) {
            for (String time : times) {
                msg = subscriptionService.deleteSubscription(email, Integer.parseInt(id), time) == 1 ? "success" : "failed";
                System.out.println(msg);
            }
        }
        return JSON.toJSONString(msg);
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
        if(id == null) {
            return JSON.toJSONString("failed");
        }
        String msg = subscriptionService.updateSubsState(id) == 1 ? "success" : "failed";
        return JSON.toJSONString(msg);
    }
}
