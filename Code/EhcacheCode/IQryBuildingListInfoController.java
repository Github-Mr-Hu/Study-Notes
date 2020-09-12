package com.sitech.cbn.cust.inter;

import com.sitech.cbn.cust.dto.cust.QryBulidingListInfoReq;
import com.sitech.cbn.cust.dto.cust.QryBulidingListInfoRsp;
import com.sitech.cbn.pub.PubReq;
import com.sitech.ijcf.message6.dt.in.InDTO;
import com.sitech.ijcf.message6.dt.out.OutDTO;

/**
 * @ClassName IQryBuildingListInfoController
 * @Description 楼宇查询控制层接口
 * @Author hufw
 * @Date 2020/9/3 11:20
 */
public interface IQryBuildingListInfoController {
    OutDTO<QryBulidingListInfoRsp> qryBulidingListInfo(InDTO<PubReq<QryBulidingListInfoReq>> inDTO);
}
