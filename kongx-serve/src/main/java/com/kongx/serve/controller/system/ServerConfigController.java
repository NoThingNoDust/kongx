package com.kongx.serve.controller.system;

import com.kongx.common.core.entity.UserInfo;
import com.kongx.common.jsonwrapper.JsonHeaderWrapper;
import com.kongx.common.utils.Jackson2Helper;
import com.kongx.serve.controller.BaseController;
import com.kongx.serve.entity.system.OperationLog;
import com.kongx.serve.entity.system.ServerConfig;
import com.kongx.serve.service.system.ServerConfigService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/ServerConfigController")
@RequestMapping("/system/server/")
public class ServerConfigController extends BaseController {
    @Autowired
    private ServerConfigService serverConfigService;

    @RequestMapping(value = "/configs", method = RequestMethod.GET)
    public JsonHeaderWrapper list() {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        List<ServerConfig> serverConfigs = this.serverConfigService.findAll();
        jsonHeaderWrapper.setData(serverConfigs);
        return jsonHeaderWrapper;
    }

    @RequestMapping(value = "/configs", method = RequestMethod.POST)
    public JsonHeaderWrapper add(@RequestBody ServerConfig serverConfig, UserInfo userInfo) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            this.serverConfigService.addServerConfig(serverConfig);
            this.log(userInfo, OperationLog.OperationType.OPERATION_ADD, OperationLog.OperationTarget.SERVER_CONFIG, serverConfig, serverConfig.getConfigKey());
        } catch (Exception e) {
            jsonHeaderWrapper.setErrmsg(e.getMessage());
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
        }
        return jsonHeaderWrapper;
    }

    @RequestMapping(value = "/configs/{id}", method = RequestMethod.POST)
    public JsonHeaderWrapper update(@PathVariable int id, @RequestBody ServerConfig serverConfig, UserInfo userInfo) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            this.serverConfigService.updateServerConfig(serverConfig);
            this.log(userInfo, OperationLog.OperationType.OPERATION_UPDATE, OperationLog.OperationTarget.SERVER_CONFIG, serverConfig, serverConfig.getConfigKey());
        } catch (Exception e) {
            jsonHeaderWrapper.setErrmsg(e.getMessage());
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
        }
        return jsonHeaderWrapper;
    }

    @RequestMapping(value = "/configs/{key}", method = RequestMethod.GET)
    public JsonHeaderWrapper findByKey(@PathVariable String key) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            jsonHeaderWrapper.setData(this.serverConfigService.findByKey(key));
        } catch (Exception e) {
            jsonHeaderWrapper.setErrmsg(e.getMessage());
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
        }
        return jsonHeaderWrapper;
    }

    @RequestMapping(value = "/configs/{key}/json", method = RequestMethod.GET)
    public JsonHeaderWrapper findJsonByKey(@PathVariable String key) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            ServerConfig serverConfig = this.serverConfigService.findByKey(key);
            jsonHeaderWrapper.setData(Jackson2Helper.parsonObject(serverConfig.getConfigValue().toString(), new TypeReference<Object>() {
            }));
        } catch (Exception e) {
            jsonHeaderWrapper.setErrmsg(e.getMessage());
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
        }
        return jsonHeaderWrapper;
    }
}
