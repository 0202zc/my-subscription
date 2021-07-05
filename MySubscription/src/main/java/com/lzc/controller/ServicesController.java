package com.lzc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.pojo.ServiceDO;
import com.lzc.service.ServicesService;
import com.lzc.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        List<ServiceDO> services = (enabled == null ? servicesService.queryServices() : servicesService.queryEnabledServices());
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
            msg = JsonUtils.SUCCESS_STRING;
            return JSON.toJSONString(data);
        } catch (Exception ignored) {
            msg = JsonUtils.FAIL_STRING;
            System.out.println(ignored);
        }
        return JsonUtils.toJsonString(response.getStatus(), msg, data);
    }

    @GetMapping("/queryServicesById")
    public String queryServicesById(Integer id) {
        return JSON.toJSONString(servicesService.queryServicesById(id));
    }

    @PostMapping("/addService")
    public String addService(@RequestParam("serviceName") String serviceName, @RequestParam("note") String note, @RequestParam("email") String email) {
        ServiceDO service = new ServiceDO(serviceName, note, email, 1);
        return servicesService.addService(service) != null ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
    }

    @PostMapping("/deleteService")
    public String deleteService(HttpServletResponse response, @RequestParam("serviceId") Integer serviceId) {
        String msg = servicesService.deleteService(new ServiceDO(serviceId)) == 1 ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @PostMapping("/updateService")
    public String updateService(@RequestParam("serviceId") Integer serviceId, @RequestParam("serviceName") String serviceName, @RequestParam("note") String note) {
        ServiceDO service = new ServiceDO(serviceId, serviceName, note);
        return servicesService.updateService(service) != null ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
    }

    @PostMapping("/updateServiceStatus")
    public String updateServiceStatus(HttpServletResponse response, @RequestParam("serviceId") Integer serviceId) {
        String msg = servicesService.updateServiceStatus(serviceId) != null ? JsonUtils.SUCCESS_STRING : JsonUtils.FAIL_STRING;
        return JsonUtils.toJsonString(response.getStatus(), msg);
    }

    @GetMapping("/validServiceName/{serviceName}")
    public String validServiceName(HttpServletResponse response, @PathVariable("serviceName") String serviceName) {
        JSONObject data = new JSONObject();
        data.put("res", servicesService.queryServicesByName(serviceName) == null);
        return JsonUtils.toJsonString(response.getStatus(), JsonUtils.SUCCESS_STRING, data);
    }

    @PostMapping("/validServiceName")
    public String validServiceName1(HttpServletResponse response, @RequestParam("serviceName") String serviceName) {
        return servicesService.queryServicesByName(serviceName) == null ? JsonUtils.TRUE_STRING : JsonUtils.FAIL_STRING;
    }
}
