package com.damon.rmq.admin.web;

import cn.hutool.core.util.IdUtil;
import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.admin.enums.SysResourceTypeEnum;
import com.damon.rmq.api.admin.model.dto.system.SysResourceDTO;
import com.damon.rmq.api.admin.model.po.SysResource;
import com.damon.rmq.api.admin.model.po.SysUser;
import com.damon.rmq.api.admin.service.ISysResourceService;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.dto.RspBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>资源控制器</p>
 */
@Controller
@RequestMapping(value = "sys_resource", method = RequestMethod.POST)
@Slf4j
public class SysResourceController {

    private static final String[] IGNORES = {"resourceId", "createTime"};

    @DubboReference
    private ISysResourceService sysResourceService;

    /**
     * <p>获取管理页面</p>
     *
     * @return 管理页面路径
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public String page(String name) {
        return "sys-resource/" + name;
    }

    /**
     * <p>新增资源</p>
     */
    @RequestMapping(value = "create")
    @ResponseBody
    @RequiresPermissions("resource:create")
    public RspBase<SysResourceDTO> create(@ModelAttribute SysResourceDTO model, HttpSession session) {
        log.info("请求参数：" + model);
        // 资源名称验证
        List<SysResourceDTO> resources = sysResourceService.selectByName(model.getName());
        RspBase<SysResourceDTO> rspBase = new RspBase<>();
        if (null != resources && resources.size() > 0) {
            rspBase.code(Constants.CODE_FAILURE).msg("该资源已存在");
            log.warn("应答内容：" + rspBase);
        } else {
            SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
            model.setCreateUser(sysUser.getUserName());

            // 新增资源
            SysResource resource = new SysResource();
            BeanUtils.copyProperties(model, resource);
            resource.setResourceId(IdUtil.simpleUUID());
            resource.setCreateTime(LocalDateTime.now());
            sysResourceService.insertSelective(resource);
            BeanUtils.copyProperties(resource, model);
            rspBase.code(Constants.CODE_SUCCESS).msg("新增成功").data(model);
            log.info("应答内容：" + rspBase);
        }
        return rspBase;
    }

    /**
     * <p>删除资源</p>
     *
     * @param resourceIds
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public RspBase<Void> delete(@RequestParam("resourceIds") String resourceIds) {
        log.info("请求参数：resourceIds=" + resourceIds);
        List<String> list = Arrays.asList(resourceIds.split(","));
        int ret = sysResourceService.deleteByPrimaryKeys(list);
        RspBase<Void> rspBase = new RspBase<>();
        if (ret <= 0) {
            rspBase.code(Constants.CODE_FAILURE).msg("删除失败");
            log.warn("应答内容：" + rspBase);
        } else {
            rspBase.code(Constants.CODE_SUCCESS).msg("删除成功");
            log.info("应答内容：" + rspBase);
        }
        return rspBase;
    }

    /**
     * <p>更新资源</p>
     *
     * @param model {@link SysResourceDTO} 更新内容
     * @return 更新操作应答
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public RspBase<SysResourceDTO> update(@ModelAttribute SysResourceDTO model, HttpSession session) {
        log.info("请求参数：" + model);
        // 资源验证
        SysResource resource = sysResourceService.selectByPrimaryKey(model.getResourceId());
        RspBase<SysResourceDTO> rspBase = new RspBase<>();
        if (null == resource) {
            rspBase.code(Constants.CODE_FAILURE).msg("资源不存在");
            log.warn("应答内容：" + rspBase);
            return rspBase;
        }

        // 更新用户
        if (!resource.getName().equals(model.getName())) {
            // 用户名验证
            List<SysResourceDTO> resources = sysResourceService.selectByName(model.getName());
            if (null != resources && resources.size() > 0) {
                rspBase.code(Constants.CODE_FAILURE).msg("资源：" + model.getName() + "已存在");
                log.warn("应答内容：" + rspBase);
                return rspBase;
            }
        }

        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        model.setUpdateUser(sysUser.getUserName());

        BeanUtils.copyProperties(model, resource, IGNORES);
        resource.setUpdateTime(LocalDateTime.now());
        sysResourceService.updateByPrimaryKey(resource);
        BeanUtils.copyProperties(resource, model);
        rspBase.code(Constants.CODE_SUCCESS).msg("修改成功").data(model);
        log.info("应答内容：" + rspBase);
        return rspBase;
    }

    /**
     * <p>查询资源</p>
     */
    @RequestMapping(value = "search")
    @ResponseBody
    public DataGrid<SysResourceDTO> searchResources(@ModelAttribute SysResourceDTO model) {
        log.info("请求参数：" + model);
        DataGrid<SysResourceDTO> datagrid = sysResourceService.selectByConditionPage(model);
        return datagrid;
    }

    @RequestMapping(value = "menu")
    @ResponseBody
    public List<SysResourceDTO> getMenu() {
        List<SysResourceDTO> resources = sysResourceService.selectByType(SysResourceTypeEnum.MENU.getValue());

//        log.info("应答内容：" + resources);
        return resources;
    }

    @RequestMapping(value = "ztree")
    @ResponseBody
    public List<SysResourceDTO> getZTree(@ModelAttribute SysResourceDTO model, HttpServletResponse response) {
        log.info("请求参数：" + model);
        model.setStatus((byte) 1);
        List<SysResourceDTO> resources = sysResourceService.selectByConditionAll(model);
//        log.info("应答内容：status=" + response.getStatus() + ", entity=" + resources);
        return resources;
    }
}
