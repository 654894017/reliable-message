package com.cn.rmq.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.rmq.api.admin.model.dto.DataGrid;
import com.cn.rmq.api.admin.model.dto.system.SysResourceDTO;
import com.cn.rmq.api.admin.model.dto.system.SysUserDTO;
import com.cn.rmq.api.admin.model.po.SysUser;
import com.cn.rmq.api.admin.model.po.UserRole;
import com.cn.rmq.api.admin.service.ISysUserService;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.dal.mapper.SysResourceMapper;
import com.cn.rmq.dal.mapper.SysUserMapper;
import com.cn.rmq.dal.mapper.UserRoleMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.hutool.core.util.IdUtil;

/**
 * 系统用户服务实现类
 */
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser, String> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private UserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByPrimaryKey(String id) {
        return sysUserMapper.deleteByPrimaryKey(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(SysUser record) {
        return sysUserMapper.insert(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insertSelective(SysUser record) {
        return sysUserMapper.insertSelective(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SysUser selectByPrimaryKey(String id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateByPrimaryKeySelective(SysUser record) {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateByPrimaryKey(SysUser record) {
        return sysUserMapper.updateByPrimaryKey(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SysUser selectByUserNameAndPassWord(String username, String password) {
        return sysUserMapper.selectByUserNameAndPassWord(username, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysUser> selectByUserName(String username) {
        return sysUserMapper.selectByUserName(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByPrimaryKeys(List<String> userIds) {
        sysUserRoleMapper.deleteByUserIds(userIds);
        return sysUserMapper.deleteByPrimaryKeys(userIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataGrid selectByConditionPage(SysUserDTO model) {
        PageHelper.startPage(model.getPage(), model.getRows());
        List<SysUserDTO> list = mapper.selectByConditionPage(model);
        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list);
        dataGrid.setTotal((int) ((Page<SysUserDTO>) list).getTotal());
        return dataGrid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByCondition(SysUserDTO model) {
        return sysUserMapper.countByCondition(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserRole> selectUserRoleByUserId(String userId) {
        return sysUserRoleMapper.selectByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int allotUserRole(String userId, List<String> roleIds) {
        sysUserRoleMapper.deleteByUserIds(Arrays.asList(userId));
        int ret = 0;
        for (String roleId : roleIds) {
            if (StringUtils.isNotBlank(roleId)) {
                UserRole userRole = new UserRole();
                userRole.setId(IdUtil.simpleUUID());
                userRole.setSysUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(LocalDateTime.now());
                sysUserRoleMapper.insert(userRole);
                ret++;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResourceDTO> selectMenuByUserId(String userId) {
        return sysResourceMapper.selectMenuByUserId(userId);
    }

    @Override
    public List<SysResourceDTO> selectMenuByImsUserId(String imsUserId) {
        return null;
    }
}
