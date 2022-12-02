package com.yourname.backen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yourname.backen.entity.SysUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser getbyId2();
    SysUser getbyId();
}
