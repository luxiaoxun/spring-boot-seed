package com.luxx.engine.model.event;


import com.luxx.engine.constant.Constant;
import com.luxx.engine.model.DataDoc;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class LogEvent extends DataDoc {
    public LogEvent() {
        super();
    }

    public LogEvent(Map map) {
        super(map);
    }

    public String getDataSource() {
        return getStringValue(Constant.DATA_SOURCE);
    }

    public long getEventTime() {
        return getLongValue(Constant.EVENT_TIME);
    }

    public String getDeviceIp() {
        return getStringValue(Constant.DEVICE_IP);
    }

    public String getDeviceId() {
        return getStringValue(Constant.DEVICE_ID);
    }

    public String getTenantId() {
        return getStringValue(Constant.TENANT_ID);
    }


}
