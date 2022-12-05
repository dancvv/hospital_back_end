package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//@CrossOrigin
@RestController
@Schema(defaultValue = "数据字典接口")
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 导入数据字典
     */
    @PostMapping("importData")
    public Result importDict(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }

    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        System.out.println(id);
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    /**
     * 导出数据字典接口
     * @param response
     * @return
     */
    @GetMapping("exportData")
    // 传入参数
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
//        return Result.ok();
    }
//    根据dictcode 和value查询
    @GetMapping("getName/{parentDictCode}/{value}")
    public String getName(@PathVariable String parentDictCode,
                          @PathVariable String value){
        String dictname = dictService.getDictName(parentDictCode, value);
        return dictname;
    }
//    根据value查询
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value){
        String dictName = dictService.getDictName("", value);
        return dictName;
    }
//    根据dictcode获取下级节点
    @GetMapping(value = "findByDictCode/{dictcode}")
    public Result<List<Dict>> findByDictCode(@PathVariable String dictcode){
        List<Dict> list = dictService.findByDictCode(dictcode);
        return Result.ok(list);
    }
}
