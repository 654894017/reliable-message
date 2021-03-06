package com.damon.rmq.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.dal.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseServiceImpl<M extends BaseMapper, T, PK> {

    @Autowired
    protected M mapper;

    public int deleteByPrimaryKey(PK id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public int delete(T record) {
        return mapper.delete(record);
    }

    public int insert(T record) {
        return mapper.insert(record);
    }

    public int insertSelective(T record) {
        return mapper.insertSelective(record);
    }

    public T selectByPrimaryKey(PK id) {
        return (T) mapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return mapper.updateByPrimaryKey(record);
    }

    public int count(T record) {
        return mapper.count(record);
    }

    public T get(T record) {
        return (T) mapper.get(record);
    }

    public List<T> list(T record) {
        return mapper.list(record);
    }

    public List<T> listByCondition(Object record) {
        return mapper.listByCondition(record);
    }

    public Page<T> listPage(Object record) {
        // maven需依赖传入的Object对应的对象的jar包，否则会报NullPointException
        Map<String, Object> paramMap = BeanUtil.beanToMap(record);
        int pageNum = (int) paramMap.get(Constants.KEY_PAGE_NUM);
        int pageSize = (int) paramMap.get(Constants.KEY_PAGE_SIZE);
        boolean count = (boolean) paramMap.get(Constants.KEY_COUNT);
        String orderBy = (String) paramMap.get(Constants.KEY_ORDER_BY);
        Page<T> page = PageHelper.startPage(pageNum, pageSize, count).setOrderBy(orderBy);
        mapper.listByCondition(paramMap);
        return page;
    }
}
