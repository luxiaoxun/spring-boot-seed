package com.luxx.seed.service.os;

import com.luxx.seed.constant.os.OsEnum;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;

@Service
public class OsEnumConvertService implements AttributeConverter<OsEnum, String> {
    @Override
    public String convertToDatabaseColumn(OsEnum osEnum) {
        return osEnum.name();
    }

    @Override
    public OsEnum convertToEntityAttribute(String osDesc) {
        if (osDesc.toLowerCase().contains(OsEnum.windows.name())) {
            return OsEnum.windows;
        }
        return OsEnum.linux;
    }
}
