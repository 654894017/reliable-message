package com.cn.rmq.api.cms.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/3/14.
 */
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
