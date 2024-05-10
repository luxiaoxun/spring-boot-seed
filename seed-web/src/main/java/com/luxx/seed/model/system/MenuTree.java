package com.luxx.seed.model.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuTree {
    private Long id;

    private String name;

    private String code;

    private String path;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuTree> children = new ArrayList<>();

    public MenuTree(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
    }
}
