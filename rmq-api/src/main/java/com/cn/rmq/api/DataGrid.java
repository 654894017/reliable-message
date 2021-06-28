package com.cn.rmq.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DataGrid implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4764349712734953813L;
    private static final List<Object> EMPTY_LIST = new ArrayList<>();
    private long total;
    private List<?> rows = EMPTY_LIST;
}
