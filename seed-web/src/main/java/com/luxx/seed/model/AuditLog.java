package com.luxx.seed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luxx.seed.constant.Constant;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuditLog {

    @JsonProperty("product")
    private String product = "Spring-boot-seed";

    @JsonProperty("version")
    private String version = Constant.PRODUCT_VERSION;

    @JsonProperty("log_type")
    private String logType = "audit_log";

    @JsonProperty("request_type")
    private String requestType = "seed-web-service";

    @JsonProperty("customer_info")
    private String customerInfo;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("request_ip")
    private String requestIp;

    @JsonProperty("request_uri")
    private String requestUri;

    @JsonProperty("request_name")
    private String requestName;

    @JsonProperty("request_time")
    private String requestTime;

    @JsonProperty("request_result")
    private int requestResult;

}
