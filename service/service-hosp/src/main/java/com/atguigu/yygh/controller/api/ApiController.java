package com.atguigu.yygh.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.service.HospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
//    查询医院
    @PostMapping("/hospital/show")
    public Result<Object> getHospital(HttpServletRequest request){
//        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        获取医院编号
        String hoscode = paramMap.get("hoscode").toString();
//        查询签名
        String hospSign = paramMap.get("sign").toString();
//        查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
//        3 把数据库查询到的签名进行md5加密
        String signKeyMd5 = MD5.encrypt(signKey);
//        4 判断签名是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR.getMessage(), ResultCodeEnum.SIGN_ERROR.getCode());
        }
//        调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }
//    上传医院接口
    @PostMapping("saveHospital")
    public Result<Object> saveHosp(HttpServletRequest request){
//        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        1 获取医院系统传递过来的签名, 签名进行md5加密
        String hospSign = paramMap.get("sign").toString();
//        2 查数据库，获取签名
        String hoscode = paramMap.get("hoscode").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
//        3 把数据库查询到的签名进行md5加密
        String signKeyMd5 = MD5.encrypt(signKey);
//        4 判断签名是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR.getMessage(), ResultCodeEnum.SIGN_ERROR.getCode());
        }
//        调用service的方法
//        图片传输过程中“+”转换为了“ ”,因此需要转换回来
        String logoData = paramMap.get("logoData").toString();
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        hospitalService.save(paramMap);
        return Result.ok();
    }
}
