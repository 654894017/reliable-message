package com.cn.rmq.api.admin.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class PageReq implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6032097556910324881L;
    private int page = 1;
    private int rows = 10;
}
