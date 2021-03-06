package com.damon.rmq.dal.mapper;

import com.damon.rmq.api.admin.model.dto.system.SysResourceDTO;
import com.damon.rmq.api.admin.model.po.SysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResourceMapper extends BaseMapper<SysResource, String> {
    /**
     * <p>根据查询条件获取资源分页</p>
     *
     * @param sysResourceDTO 查询条件
     */
    List<SysResourceDTO> selectByConditionPage(@Param("resdto") SysResourceDTO sysResourceDTO);


    /**
     * <p>根据查询条件获取资源列表</p>
     *
     * @param sysResourceDTO 查询条件
     */
    List<SysResourceDTO> selectByConditionAll(SysResourceDTO sysResourceDTO);

    /**
     * <p>根据查询条件统计资源记录数</p>
     *
     * @param sysResourceDTO 查询条件
     */
    int countByCondition(SysResourceDTO sysResourceDTO);

    /**
     * <p>根据主键列表删除指定的资源记录</p>
     *
     * @param sysResourceIds 资源主键列表
     * @return
     */
    int deleteByPrimaryKeys(List<String> sysResourceIds);

    /**
     * <p>根据用户唯一标识获取用户的菜单</p>
     *
     * @param sysUserId 用户唯一标识
     * @return {@link List<SysResourceDTO>} 用户菜单列表
     */
    List<SysResourceDTO> selectMenuByUserId(String sysUserId);

    /**
     * <p>根据用户唯一标识获取用户的菜单</p>
     *
     * @param sysUserId 用户唯一标识
     * @return {@link List<SysResource>} 用户资源列表
     */
    List<SysResource> selectByUserId(String sysUserId);

    /**
     * <p>获取指定类型的资源</p>
     *
     * @param type 资源类型
     * @return {@link List<SysResourceDTO>} 资源列表
     */
    List<SysResourceDTO> selectByType(Byte type);

    /**
     * <p>根据资源名称获取资源列表</p>
     *
     * @param name 资源名称
     * @return 资源列表
     */
    List<SysResourceDTO> selectByName(String name);

    List<SysResource> selectByImsUserId(String imsUserId);

    List<SysResourceDTO> selectMenuByImsUserId(String imsUserId);
}