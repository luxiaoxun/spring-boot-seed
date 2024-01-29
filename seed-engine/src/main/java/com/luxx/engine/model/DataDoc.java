package com.luxx.engine.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.luxx.engine.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataDoc extends ConcurrentHashMap<String, Object> {
    private final static Logger log = LoggerFactory.getLogger(DataDoc.class);

    public DataDoc() {
        super();
    }

    public DataDoc(Map c) {
        super(c);
    }

    public String getId() {
        return getStringValue(Constant.ID);
    }

    public void setId(String id) {
        put(Constant.ID, id);
    }

    //时间
    public long getEventTime() {
        return getLongValue(Constant.EVENT_TIME);
    }

    public void setEventTime(long time) {
        put(Constant.EVENT_TIME, time);
    }

    //ES时间戳
    public void setTimestamp(String time) {
        put(Constant.TIMESTAMP, time);
    }

    //租户组织ID
    public String getTenantId() {
        return getStringValue(Constant.TENANT_ID);
    }

    public void setTenantId(String tenantId) {
        put(Constant.TENANT_ID, tenantId);
    }

    //次数
    public long getCount() {
        return getLongValue(Constant.COUNT);
    }

    public void setCount(long count) {
        put(Constant.COUNT, count);
    }

    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            return null;
        }
        return super.put(key, value);
    }

    public boolean getBooleanVal(String key) {
        return getBooleanVal(key, false);
    }

    public boolean getBooleanVal(String key, boolean defaultValue) {
        try {
            Object value = get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof String) {
                return Boolean.parseBoolean(value.toString());
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            log.error("{}", e);
            return defaultValue;
        }
    }

    public int getIntValue(String key) {
        return getIntValue(key, -1);
    }

    public int getIntValue(String key, int defaultValue) {
        try {
            Object value = get(key);
            if (value == null) {
                return defaultValue;
            }
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            log.error("{}", e);
        }
        return defaultValue;
    }

    public String getStringValue(String key) {
        return getStringValue(key, "");
    }

    public String getStringValue(String key, String defaultValue) {
        try {
            Object value = get(key);
            if (value == null) {
                return defaultValue;
            }
            return value.toString();
        } catch (Exception e) {
            log.error("{}", e);
        }
        return defaultValue;
    }

    public long getLongValue(String key) {
        return getLongValue(key, 0L);
    }

    public long getLongValue(String key, long defaultValue) {
        try {
            Object value = get(key);
            if (value == null) {
                return defaultValue;
            }
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            log.error("{}", e);
        }
        return defaultValue;
    }

    public Map<String, Object> getMapObject(String key) {
        try {
            Object value = get(key);
            if (value != null) {
                return (Map<String, Object>) value;
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        Map<String, Object> map = new HashMap<>();
        put(key, map);
        return map;
    }

    public <T> Set<T> getSetValue(String key, Class<T> clz) {
        try {
            Object o = get(key);
            if (o == null) {
                Set s = new HashSet<>();
                put(key, s);
                return s;
            }
            if (o instanceof Set) {
                return (Set<T>) o;
            } else if (o instanceof Collection) {
                Set s = new HashSet<T>((Collection) o);
                put(key, s);
                return s;
            } else if (o.getClass().equals(clz)) {
                Set set = new HashSet();
                set.add(o);
                put(key, set);
                return set;
            } else if (o instanceof JsonArray) {
                Set s = new HashSet<>(new Gson().fromJson((JsonElement) o, Collection.class));
                put(key, s);
                return s;
            } else {
                //throw new RuntimeException("unsupported type");
                log.warn("unsupported type, key={}, class={}", key, clz.getCanonicalName());
                Set s = new HashSet<>();
                put(key, s);
                return s;
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        Set s = new HashSet<>();
        put(key, s);
        return s;
    }

    public String[] getStrArrayValue(String key) {
        try {
            Object o = get(key);
            if (o == null) {
                String[] s = new String[0];
                put(key, s);
                return s;
            }
            if (o instanceof String[]) {
                return (String[]) o;
            } else if (o instanceof Collection) {
                Collection<String> s = (Collection) o;
                String[] array = s.toArray(new String[s.size()]);
                put(key, array);
                return array;
            } else if (o instanceof JsonArray) {
                String[] s = new Gson().fromJson((JsonElement) o, String[].class);
                put(key, s);
                return s;
            } else {
                log.warn("unsupported type, key={}", key);
                String[] s = new String[0];
                put(key, s);
                return s;
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        String[] s = new String[0];
        put(key, s);
        return s;
    }

    public Integer[] getIntArrayValue(String key) {
        try {
            Object o = get(key);
            if (o == null) {
                Integer[] s = new Integer[0];
                put(key, s);
                return s;
            }
            if (o instanceof Integer[]) {
                return (Integer[]) o;
            } else if (o instanceof Collection) {
                Collection<String> s = (Collection) o;
                Integer[] array = s.toArray(new Integer[s.size()]);
                put(key, array);
                return array;
            } else if (o instanceof JsonArray) {
                Integer[] s = new Gson().fromJson((JsonElement) o, Integer[].class);
                put(key, s);
                return s;
            } else {
                log.warn("unsupported type, key={}", key);
                Integer[] s = new Integer[0];
                put(key, s);
                return s;
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        Integer[] s = new Integer[0];
        put(key, s);
        return s;
    }
}
