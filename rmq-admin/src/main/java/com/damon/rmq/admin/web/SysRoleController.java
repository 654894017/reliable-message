package com.damon.rmq.admin.web;

import cn.hutool.core.util.IdUtil;
import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.admin.model.dto.system.SysRoleDTO;
import com.damon.rmq.api.admin.model.po.RoleResource;
import com.damon.rmq.api.admin.model.po.SysRole;
import com.damon.rmq.api.admin.model.po.SysUser;
import com.damon.rmq.api.admin.service.ISysRoleService;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.dto.RspBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>角色控制器</p>
 */
@Controller
@RequestMapping(value = "sys_role", method = RequestMethod.POST)
@Slf4j
public class SysRoleController {

    private static final String[] IGNORES = {"roleId", "createTime"};

    @DubboReference
    private ISysRoleService sysRoleService;

    /**
     * <p>获取管理页面</p>
     *
     * @return 管理页面路径
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public String page(String name) {
        return "sys-role/" + name;
    }

    /**
     * <p>新增</p>
     */
    @RequestMapping(value = "create")
    @ResponseBody
    public RspBase<SysRole> create(@ModelAttribute SysRole model, HttpSession session) {
        log.info("请求参数：" + model);

        RspBase<SysRole> rspBase = new RspBase<>();
        // 验证角色名称
        List<SysRole> roles = sysRoleService.selectByRoleName(model.getRoleName());
        if (null != roles && roles.size() > 0) {
            rspBase.code(Constants.CODE_FAILURE).msg("该角色已存在");
            log.info("应答内容：" + rspBase);
            return rspBase;
        }

        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        model.setCreateUser(sysUser.getUserName());

        // 新增角色
        SysRole newRole = new SysRole();
        BeanUtils.copyProperties(model, newRole);
        newRole.setRoleId(IdUtil.simpleUUID());
        newRole.setCreateTime(LocalDateTime.now());
        sysRoleService.insert(newRole);
        rspBase.code(Constants.CODE_SUCCESS).msg("新增成功").data(newRole);
        log.info("应答内容：" + rspBase);
        return rspBase;
    }

    /**
     * <p>删除</p>
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public RspBase<Void> delete(@RequestParam("roleIds") String roleIds) {
        log.debug("请求参数：" + roleIds);
        List<String> list = Arrays.asList(roleIds.split(","));
        sysRoleService.deleteByPrimaryKeys(list);
        RspBase<Void> rspBase = new RspBase<>();
        rspBase.code(Constants.CODE_SUCCESS).msg("删除成功");
        log.debug("应答内容：" + rspBase);
        return rspBase;
    }

    /**
     * <p>修改</p>
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public RspBase<SysRole> update(@ModelAttribute SysRole model, HttpSession session) {
        log.info("请求参数：" + model);

        RspBase<SysRole> rspBase = new RspBase<>();
        // 验证角色
        SysRole role = sysRoleService.selectByPrimaryKey(model.getRoleId());
        if (null == role) {
            rspBase.code(Constants.CODE_FAILURE).msg("修改失败：无对应角色信息");
            log.info("应答内容：" + rspBase);
            return rspBase;
        }

        // 更新角色信息
        if (!role.getRoleName().equals(model.getRoleName())) {
            // 用户名验证
            List<SysRole> roles = sysRoleService.selectByRoleName(model.getRoleName());
            if (null != roles && roles.size() > 0) {
                rspBase.code(Constants.CODE_FAILURE).msg("角色：" + model.getRoleName() + " 已存在");
                log.warn("应答内容：" + rspBase);
                return rspBase;
            }
        }

        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        model.setUpdateUser(sysUser.getUserName());

        BeanUtils.copyProperties(model, role, IGNORES);
        role.setUpdateTime(LocalDateTime.now());
        sysRoleService.updateByPrimaryKeySelective(role);
        BeanUtils.copyProperties(role, model);
        rspBase.code(Constants.CODE_SUCCESS).msg("修改成功").data(model);
        log.info("应答内容：" + rspBase);
        return rspBase;
    }

    /**
     * <p>获取角色-资源列表</p>
     */
    @RequestMapping(value = "{roleId}/resources")
    @ResponseBody
    public List<RoleResource> getRoleResources(@PathVariable("roleId") String roleId) {
        log.info("请求参数：roleId=" + roleId);
        List<RoleResource> roleResources = sysRoleService.getRoleResources(roleId);
        log.info("应答内容：" + roleResources);
        return roleResources;
    }

    /**
     * <p>获取列表</p>
     */
    @RequiresPermissions("role:search")
    @RequestMapping(value = "search")
    @ResponseBody
    public DataGrid<SysRoleDTO> search(@ModelAttribute SysRoleDTO model) {
        log.info("请求参数：" + model);
        DataGrid<SysRoleDTO> datagrid = sysRoleService.selectByConditionPage(model);
        return datagrid;
    }

    @RequestMapping(value = "ztree")
    @ResponseBody
    public List<SysRole> getZTree(@ModelAttribute SysRoleDTO model) {
        log.info("请求参数：" + model);
        List<SysRole> roles = sysRoleService.selectByConditionAll(model);
        log.info("应答内容：" + roles);
        return roles;
    }

    @RequestMapping(value = "{roleId}/resources/allot")
    @ResponseBody
    public RspBase<Void> allotRoleResources(@RequestParam(value = "resourceIds") String resourceIds, @PathVariable("roleId") String roleId) {
        log.info("请求参数：roleId=" + roleId + ", resourceIds=" + resourceIds);
        RspBase<Void> rspBase = new RspBase<Void>();
        int ret = sysRoleService.allotRoleResources(roleId, Arrays.asList(resourceIds.split(",")));
        if (ret <= 0) {
            rspBase.code(Constants.CODE_FAILURE).msg("分配角色资源失败");
            log.warn("应答内容：" + rspBase);
            return rspBase;
        }
        rspBase.code(Constants.CODE_SUCCESS).msg("分配角色资源成功");
        log.info("应答内容：" + rspBase);
        return rspBase;
    }
}
