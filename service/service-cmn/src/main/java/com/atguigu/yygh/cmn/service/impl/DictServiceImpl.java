package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

////    注入mapper
//    @Autowired
//    private DictMapper dictMapper;

    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
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
     * 导出数据字典接口
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
//        设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = "dict";
//        以下载方式打开
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        List<Dict> dictList = baseMapper.selectList(null);
//        创建的数据实体类在model中已经创建
//        Dict -- DictEevo,两个数据之间的转换
        List<DictEeVo> dictVoList = new ArrayList<>();
        for(Dict dict:dictList){
            DictEeVo dictEeVo = new DictEeVo();
//            操作的封装
//            dictEeVo.setId();dict.getId()
            BeanUtils.copyProperties(dict, dictEeVo);
            dictVoList.add(dictEeVo);
        }

//        调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * 导入数据字典
     * @param file
     */
    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    根据dictcode 和value查询
    @Override
    public String getDictName(String dictcode, String value) {
//        如果dictcode为空，直接根据value查询
        if(!StringUtils.hasLength(dictcode)){
//            直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {
//            如果dictcode不为空，根据dictcode和value查询
//            根据dictcode查询dict对象，得到dict的id值
            Dict dictbyDictCode = this.getDictbyDictCode(dictcode);
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", dictbyDictCode.getParentId())
                    .eq("value", dictbyDictCode.getValue());
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }
    }

    //    根据dictcode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictcode) {
//        根据dictcode获取对应id
        Dict dict = this.getDictbyDictCode(dictcode);
//        根据id获取子节点
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    private Dict getDictbyDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }
}
