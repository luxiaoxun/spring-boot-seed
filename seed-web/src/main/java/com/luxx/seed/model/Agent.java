package com.luxx.seed.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agent {
    @ExcelIgnore
    private Long id;

    @ExcelProperty("IP")
    @NotEmpty
    private String ip;

    @ExcelProperty("版本")
    private String version;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("所属人")
    private String owner;

    @ExcelProperty("类型")
    @NotEmpty
    private String type;

    @ExcelProperty("租户ID")
    @NotEmpty
    private String tenantId;

    @ExcelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty("更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
