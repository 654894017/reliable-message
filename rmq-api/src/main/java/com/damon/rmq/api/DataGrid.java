package com.damon.rmq.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class DataGrid<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4764349712734953813L;
    //private static final List<Object> EMPTY_LIST = new ArrayList<>();
    private long total;
    private List<T> rows = new ArrayList<>();
}
