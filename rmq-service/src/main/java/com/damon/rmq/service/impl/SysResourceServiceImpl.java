package com.damon.rmq.service.impl;

import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.admin.model.dto.system.SysResourceDTO;
import com.damon.rmq.api.admin.model.po.SysResource;
import com.damon.rmq.api.admin.service.ISysResourceService;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.dal.mapper.RoleResourceMapper;
import com.damon.rmq.dal.mapper.SysResourceMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 资源服务实现类
 *
 * @author xianping_lu
 */
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceMapper, SysResource, String> implements ISysResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public DataGrid<SysResourceDTO> selectByConditionPage(SysResourceDTO model) {
        PageHelper.startPage(model.getPage(), model.getRows());
        List<SysResourceDTO> list = mapper.selectByConditionPage(model);
        DataGrid<SysResourceDTO> dataGrid = new DataGrid<>();
        dataGrid.setRows(list);
        dataGrid.setTotal((int) ((Page<SysResourceDTO>) list).getTotal());
        return dataGrid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResourceDTO> selectByConditionAll(SysResourceDTO resourceReq) {
        return sysResourceMapper.selectByConditionAll(resourceReq);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByCondition(SysResourceDTO resourceReq) {
        return sysResourceMapper.countByCondition(resourceReq);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByPrimaryKeys(List<String> sysResourceIds) {
        roleResourceMapper.deleteByResourceIds(sysResourceIds);
        return sysResourceMapper.deleteByPrimaryKeys(sysResourceIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByPrimaryKey(String id) {
        return sysResourceMapper.deleteByPrimaryKey(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(SysResource record) {
        return sysResourceMapper.insert(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insertSelective(SysResource record) {
        return sysResourceMapper.insertSelective(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SysResource selectByPrimaryKey(String id) {
        return sysResourceMapper.selectByPrimaryKey(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateByPrimaryKeySelective(SysResource record) {
        return sysResourceMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateByPrimaryKey(SysResource record) {
        return sysResourceMapper.updateByPrimaryKey(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResourceDTO> selectMenuByUserId(String sysUserId) {
        return sysResourceMapper.selectMenuByUserId(sysUserId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResourceDTO> selectByType(Byte type) {
        return sysResourceMapper.selectByType(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResource> selectByUserId(String sysUserId) {
        return sysResourceMapper.selectByUserId(sysUserId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResourceDTO> selectByName(String name) {
        return sysResourceMapper.selectByName(name);
    }

    @Override
    public List<SysResource> selectByImsUserId(String imsUserId) {
        return sysResourceMapper.selectByImsUserId(imsUserId);
    }

}
