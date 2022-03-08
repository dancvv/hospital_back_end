package com.atguigu.yygh.cmn.service.impl;

import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        // 向list集合每个dict对象中设置hashchildren
        for(Dict dict:dicts){
            Long id1 = dict.getId();
            boolean isChildren = this.isChildren(id1);
            dict.setHasChildren(isChildren);
        }
        return dicts;
    }

    /**
     * 判断id下面是否有子节点
     */
    public boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        // 如果有数据，结果大于0
        Long aLong = baseMapper.selectCount(wrapper);
        return aLong>0;
    }
}
