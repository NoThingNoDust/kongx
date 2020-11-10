package com.kongx.serve.controller.gateway;

import com.kongx.common.core.entity.UserInfo;
import com.kongx.common.jsonwrapper.JsonHeaderWrapper;
import com.kongx.serve.controller.BaseController;
import com.kongx.serve.entity.gateway.CaCertificate;
import com.kongx.serve.entity.gateway.KongEntity;
import com.kongx.serve.entity.system.OperationLog;
import com.kongx.serve.service.gateway.CaCertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController("CaCertificateController")
@RequestMapping("/kong/api/")
@Slf4j
public class CaCertificateController extends BaseController {
    private static final String CERTIFICATES_URI = "/ca_certificates";
    private static final String CERTIFICATES_URI_ID = "/ca_certificates/{id}";

    @Autowired
    private CaCertificateService caCertificateService;

    /**
     * 查询所有sni
     *
     * @return
     */
    @RequestMapping(value = CERTIFICATES_URI, method = RequestMethod.GET)
    public JsonHeaderWrapper findAll(UserInfo userInfo) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            KongEntity<CaCertificate> upstreamKongEntity = caCertificateService.findAll(systemProfile(userInfo));
            jsonHeaderWrapper.setData(upstreamKongEntity.getData());
        } catch (Exception e) {
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
            jsonHeaderWrapper.setErrmsg(e.getMessage());
        }
        return jsonHeaderWrapper;
    }

    /**
     * 新增upstream
     *
     * @param sni
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = CERTIFICATES_URI, method = RequestMethod.POST)
    public JsonHeaderWrapper addUpstream(UserInfo userInfo, @RequestBody CaCertificate sni) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            CaCertificate results = this.caCertificateService.add(systemProfile(userInfo), sni.trim());
            jsonHeaderWrapper.setData(results);
            this.log(userInfo, OperationLog.OperationType.OPERATION_ADD, OperationLog.OperationTarget.CaCertificate, sni);
        } catch (Exception e) {
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
            jsonHeaderWrapper.setErrmsg(e.getMessage());
        }
        return jsonHeaderWrapper;
    }

    /**
     * 更新consumer
     *
     * @param id
     * @param sni
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = CERTIFICATES_URI_ID, method = RequestMethod.POST)
    public JsonHeaderWrapper update(UserInfo userInfo, @PathVariable String id, @RequestBody CaCertificate sni) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            CaCertificate results = this.caCertificateService.update(systemProfile(userInfo), id, sni.trim());
            jsonHeaderWrapper.setData(results);
            this.log(userInfo, OperationLog.OperationType.OPERATION_UPDATE, OperationLog.OperationTarget.CaCertificate, sni, sni.getId());
        } catch (Exception e) {
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
            jsonHeaderWrapper.setErrmsg(e.getMessage());
        }
        return jsonHeaderWrapper;
    }

    /**
     * 删除sni
     *
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = CERTIFICATES_URI_ID, method = RequestMethod.DELETE)
    public JsonHeaderWrapper remove(UserInfo userInfo, @PathVariable String id) throws Exception {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            CaCertificate sni = this.caCertificateService.findEntity(systemProfile(userInfo), id);
            KongEntity<CaCertificate> upstreamKongEntity = this.caCertificateService.remove(systemProfile(userInfo), id);
            this.log(userInfo, OperationLog.OperationType.OPERATION_DELETE, OperationLog.OperationTarget.CaCertificate, sni);
            jsonHeaderWrapper.setData(upstreamKongEntity.getData());
        } catch (Exception e) {
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
            jsonHeaderWrapper.setErrmsg(e.getMessage());
        }
        return jsonHeaderWrapper;
    }

    /**
     * 查询单个sni的信息
     *
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = CERTIFICATES_URI_ID, method = RequestMethod.GET)
    public JsonHeaderWrapper findSni(UserInfo userInfo, @PathVariable String id) {
        JsonHeaderWrapper jsonHeaderWrapper = this.init();
        try {
            CaCertificate results = this.caCertificateService.findEntity(systemProfile(userInfo), id);
            jsonHeaderWrapper.setData(results);
        } catch (Exception e) {
            jsonHeaderWrapper.setStatus(JsonHeaderWrapper.StatusEnum.Failed.getCode());
            jsonHeaderWrapper.setErrmsg(e.getMessage());
        }
        return jsonHeaderWrapper;
    }
}
