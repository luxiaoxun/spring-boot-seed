package com.luxx.engine.model.event;


import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.luxx.engine.model.DataDoc;
import com.luxx.util.JsonUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class RingBufferEvent implements Serializable {
    private DataDoc data;

    public String toJson() {
        return JsonUtil.encode(data);
    }

    public static final EventFactory<RingBufferEvent> FACTORY = new EventFactory<RingBufferEvent>() {
        @Override
        public RingBufferEvent newInstance() {
            return new RingBufferEvent();
        }
    };

    public static final EventTranslatorOneArg<RingBufferEvent, DataDoc> TRANSLATOR = new EventTranslatorOneArg<RingBufferEvent, DataDoc>() {
        @Override
        public void translateTo(RingBufferEvent event, long sequence, DataDoc data) {
            event.setData(data);
        }
    };

}
