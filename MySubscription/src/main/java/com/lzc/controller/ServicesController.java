package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.pojo.Services;
import com.lzc.service.ServicesService;
import com.lzc.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Liang Zhancheng
 */
@RestController
@RequestMapping("/services")
public class ServicesController {
    @Autowired
    ServicesService servicesService;

    @GetMapping("/queryServices")
    public String queryServices(Integer enabled) {
        List<Services> services = (enabled == null ? servicesService.queryServices() : servicesService.queryEnabledServices());
        return JSON.toJSONString(services);
    }

    @GetMapping("/queryServicesWithCrawlers")
    public String queryServicesWithCrawlers(HttpServletResponse response, String sSearch) {
        String msg = "";
        JSONObject data = new JSONObject();
        try {
            List<Object> list = servicesService.queryServicesWithCrawlers(sSearch);
            data.put("draw", list.size());
            data.put("aaData", list);
            data.put("sEcho", 3);
            data.put("iTotalRecords", list.size());
            data.put("iTotalDisplayRecords", list.size());
            msg = "success";
            return JSON.toJSONString(data);
        } catch (Exception ignored) {
            msg = "fail";
            System.out.println(ignored);
        }
        return JsonUtil.toJsonString(response.getStatus(), msg, data);
    }

    @GetMapping("/queryServicesById")
    public String queryServicesById(Integer id) {
        return JSON.toJSONString(servicesService.queryServicesById(id));
    }

    @PostMapping("/addService")
    public String addService(@RequestParam("serviceName") String serviceName, @RequestParam("note") String note, @RequestParam("email") String email) {
        Services service = new Services(serviceName, note, email, 1);
        return servicesService.addService(service) != null ? "success" : "fail";
    }

    @PostMapping("/deleteService")
    public String deleteService(HttpServletResponse response, @RequestParam("serviceId") Integer serviceId) {
        String msg = servicesService.deleteService(new Services(serviceId)) == 1 ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/updateService")
    public String updateService(@RequestParam("serviceId") Integer serviceId, @RequestParam("serviceName") String serviceName, @RequestParam("note") String note) {
        Services service = new Services(serviceId, serviceName, note);
        return servicesService.updateService(service) != null ? "success" : "fail";
    }

    @PostMapping("/updateServiceStatus")
    public String updateServiceStatus(HttpServletResponse response, @RequestParam("serviceId") Integer serviceId) {
        String msg = servicesService.updateServiceStatus(serviceId) != null ? "success" : "fail";
        return JsonUtil.toJsonString(response.getStatus(), msg);
    }

    @GetMapping("/validServiceName/{serviceName}")
    public String validServiceName(HttpServletResponse response, @PathVariable("serviceName") String serviceName) {
        JSONObject data = new JSONObject();
        data.put("res", servicesService.queryServicesByName(serviceName) == null);
        return JsonUtil.toJsonString(response.getStatus(), "success", data);
    }

    @PostMapping("/validServiceName")
    public String validServiceName1(HttpServletResponse response, @RequestParam("serviceName") String serviceName) {
        return servicesService.queryServicesByName(serviceName) == null ? "true" : "false";
    }
}
