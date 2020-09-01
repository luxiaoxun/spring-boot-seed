package com.luxx.seed.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageModel<T> implements Serializable {
    private static final long serialVersionUID = -5388936993444570780L;

    private List<T> table;

    private Long total;
}
