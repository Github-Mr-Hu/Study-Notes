package com.sitech.cbn.cust.dto.cust;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitech.cbn.cust.po.BsBulidinglistInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName QryBulidingListInfoRsp
 * @Description 楼宇查询出参
 * @Author hufw
 * @Date 2020/9/3 11:37
 */
@Data
@ApiModel("楼宇查询出参")
public class QryBulidingListInfoRsp {

    @JsonProperty("BULIDING_LIST")
    @ApiModelProperty(name = "楼宇列表", value = "楼宇列表", required = true)
    private List<BsBulidinglistInfo> bulidingList;
}
