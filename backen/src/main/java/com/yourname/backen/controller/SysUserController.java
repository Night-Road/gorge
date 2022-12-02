package com.yourname.backen.controller;


import com.yourname.backen.entity.SysUser;
import com.yourname.backen.service.impl.SysUserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Autowired
    private SysUserServiceImpl sysUserService;

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/info")
    public SysUser getInfo() {
        SysUser s = sysUserService.getbyId();
        return s;
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/info2")
    public SysUser getInfo2() {
        SysUser s = sysUserService.getbyId2();
        System.out.println(s.toString());
        return s;
    }

}

