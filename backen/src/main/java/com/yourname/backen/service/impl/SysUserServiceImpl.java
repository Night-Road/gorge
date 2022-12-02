package com.yourname.backen.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.backen.entity.SysUser;
import com.yourname.backen.mapper.SysUserMapper;
import com.yourname.backen.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    @DS("master")
    public SysUser getbyId() {
        return baseMapper.selectOne(null);
    }
    @DS("slave1")
    public SysUser getbyId2() {
        return baseMapper.selectById(1);
    }


}
