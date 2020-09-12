package com.sitech.cbn.cust.impl.cust;

import com.sitech.cbn.cust.dto.cust.QryBulidingListInfoReq;
import com.sitech.cbn.cust.dto.cust.QryBulidingListInfoRsp;
import com.sitech.cbn.cust.inter.IQryBuildingListInfoController;
import com.sitech.cbn.cust.inter.IQryBulidingListInfoService;
import com.sitech.cbn.pub.PubReq;
import com.sitech.ijcf.boot.core.error.exception.BusiException;
import com.sitech.ijcf.message6.dt.in.InDTO;
import com.sitech.ijcf.message6.dt.out.OutDTO;
import com.sitech.ijcf.message6.wrapper.support.OutDTOBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName IQryBuildingListInfoControllerImpl
 * @Description 楼宇查询控制层接口实现类
 * @Author hufw
 * @Date 2020/9/3 13:05
 */
@Slf4j
@RequestMapping("/api/v1/acct/")
@Api(tags = "客户资料")
@Service("qryBuildingListInfoControllerSvc")
public class QryBuildingListInfoControllerImpl implements IQryBuildingListInfoController {

    @Autowired
    private IQryBulidingListInfoService qryBulidingListInfoService;

    @PostMapping("qryBulidingListInfo")
    @ApiOperation("楼宇信息查询")
    @ResponseBody
    @Override
    public OutDTO<QryBulidingListInfoRsp> qryBulidingListInfo(@RequestBody InDTO<PubReq<QryBulidingListInfoReq>> inDTO) {

        QryBulidingListInfoReq busiInfo = inDTO.getBody().getBusiInfo();

        QryBulidingListInfoRsp qryBulidingListInfoRsp = new QryBulidingListInfoRsp();
        try {
            //第一次修改
//            qryBulidingListInfoService.updateBulidingInfo("1", Long.valueOf("10001"));
            //第一次查询
//            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
//            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");

            //第二次修改
//            qryBulidingListInfoService.updateBulidingInfo("2", Long.valueOf("10001"));
            //第二次查询
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");

            //删除数据
            qryBulidingListInfoService.deleteBulidingInfo("工商银行-丰台支行-镇国寺支行（语音专线）");
            //第三次查询
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return OutDTOBuild.builder().build(qryBulidingListInfoRsp);
    }
}
