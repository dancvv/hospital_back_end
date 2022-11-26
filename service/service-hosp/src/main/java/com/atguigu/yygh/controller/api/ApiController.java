package com.atguigu.yygh.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.service.HospitalSetService;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
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
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;
//    删除排班接口
    @PostMapping("schedule/remove")
    public Result<Object> remove(HttpServletRequest request){
        //        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        获取医院编号和排班编号
        String hoscode = paramMap.get("hoscode").toString();
        String hosScheduleId = paramMap.get("hosScheduleId").toString();
//        签名校验
        String hospSign = paramMap.get("sign").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        if(!signKeyMd5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }
//    查询排班接口
    @PostMapping("schedule/list")
    public Result<Object> findSchedule(HttpServletRequest request){
         //        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        获取医院编号
        String hoscode = paramMap.get("hoscode").toString();
//        获取科室编号,(接口并没有传)
//        String depcode = paramMap.get("depcode").toString();
//        当前页和每页记录数
        int page = !StringUtils.hasLength(String.valueOf(paramMap.get("page"))) ? 1 : Integer.parseInt(paramMap.get("page").toString());
        int limit = !StringUtils.hasLength(String.valueOf(paramMap.get("limit"))) ? 1 : Integer.parseInt(paramMap.get("limit").toString());
//        签名校验
        String hospSign = paramMap.get("sign").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        if(!signKeyMd5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
//        scheduleQueryVo.setDepcode(depcode);
//        调用service方法
        Page<Schedule> pageModel = scheduleService.selectPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }
//    上传排班接口
    @PostMapping("saveSchedule")
    public Result<Object> saveSchedule(HttpServletRequest request){
        //        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        签名校验
        String hoscode = paramMap.get("hoscode").toString();
        String hospSign = paramMap.get("sign").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        if(!signKeyMd5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();

    }
//    删除科室接口
    @PostMapping("/department/remove")
    public Result<Object> removeDepartment(HttpServletRequest request){
        //        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        获取医院编号
        String hoscode = paramMap.get("hoscode").toString();
        String depcode = paramMap.get("depcode").toString();
        if(!StringUtils.hasLength(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
//        签名校验
        String hospSign = paramMap.get("sign").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        if(!signKeyMd5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hoscode, depcode);
        return Result.ok();

    }
//    查询科室接口
    @PostMapping("department/list")
    public Result<Object> findDepartment(HttpServletRequest request){
 //        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        获取医院编号
        String hoscode = paramMap.get("hoscode").toString();
//        当前页和每页记录数
        int page = !StringUtils.hasLength(String.valueOf(paramMap.get("page"))) ? 1 : Integer.parseInt(paramMap.get("page").toString());
        int limit = !StringUtils.hasLength(String.valueOf(paramMap.get("limit"))) ? 1 : Integer.parseInt(paramMap.get("limit").toString());
//        签名校验
        String hospSign = paramMap.get("sign").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        if(!signKeyMd5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }
//    上传科室接口
    @PostMapping("saveDepartment")
    public Result<Object> saveDepartment(HttpServletRequest request){
//        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
//        获取医院编号
        String hoscode = paramMap.get("hoscode").toString();
        String hospSign = paramMap.get("sign").toString();
//        参数校验
        if(!StringUtils.hasLength(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
//        查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
//        3 把数据库查询到的签名进行md5加密
        String signKeyMd5 = MD5.encrypt(signKey);
//        4 判断签名是否一致
//        签名校验
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR.getMessage(), ResultCodeEnum.SIGN_ERROR.getCode());
        }
        departmentService.save(paramMap);
        return Result.ok();
    }
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
