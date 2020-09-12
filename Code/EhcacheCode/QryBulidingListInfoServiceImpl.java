package com.sitech.cbn.cust.impl.cust;

import com.sitech.cbn.cust.dto.cust.QryBulidingListInfoReq;
import com.sitech.cbn.cust.inter.IQryBulidingListInfoService;
import com.sitech.cbn.cust.mapper.BsBulidinglistInfoMapper;
import com.sitech.cbn.cust.po.BsBulidinglistInfo;
import com.sitech.cbn.cust.po.BsBulidinglistInfoExample;
import com.sitech.ijcf.boot.core.error.exception.BusiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName QryBulidingListInfoServiceImpl
 * @Description 楼宇信息业务层实现类
 * @Author hufw
 * @Date 2020/9/3 13:17
 */
@Slf4j
@Component
public class QryBulidingListInfoServiceImpl implements IQryBulidingListInfoService {

    @Autowired
    private BsBulidinglistInfoMapper bsBulidinglistInfoMapper;

    @Cacheable(value="userCache",key="#busiInfo.bulidingName")
    @Override
    public List<BsBulidinglistInfo> qryBulidingListInfo(QryBulidingListInfoReq busiInfo) {

        String buildingName = busiInfo.getBulidingName();//楼宇名称
        List<BsBulidinglistInfo> list = new ArrayList<BsBulidinglistInfo>();
        try {
            log.info("去数据库查询了数据！");
            list = bsBulidinglistInfoMapper.qryBulidingListInfoDao(buildingName);
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return list;
    }

    @CachePut(value="userCache",key="#valueof1")
    @Override
    public void updateBulidingInfo(String valueOf, Long valueof1) {
        log.info("更新了数据库数据！！");
        bsBulidinglistInfoMapper.updateBulidingNo(valueOf, valueof1);
    }

    @CacheEvict(value = "userCache", key = "#s", beforeInvocation=true)
    @Override
    public void deleteBulidingInfo(String s) {
        log.info("删除了数据库数据！！");
        bsBulidinglistInfoMapper.deleteBulidingInfo(s);
    }
}