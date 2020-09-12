package com.sitech.cbn.cust.inter;

import com.sitech.cbn.cust.dto.cust.QryBulidingListInfoReq;
import com.sitech.cbn.cust.po.BsBulidinglistInfo;

import java.util.List;

/**
 * @ClassName IQryBulidingListInfoService
 * @Description 楼宇信息查询业务层接口
 * @Author hufw
 * @Date 2020/9/3 13:13
 */
public interface IQryBulidingListInfoService {
    List<BsBulidinglistInfo> qryBulidingListInfo(QryBulidingListInfoReq busiInfo);

    void updateBulidingInfo(String valueOf, Long valueof1);

    void deleteBulidingInfo(String s);
}
