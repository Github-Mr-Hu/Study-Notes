package com.sitech.cbn.cust.dto.cust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName QryBulidingListInfoReq
 * @Description 楼宇查询入参
 * @Author hufw
 * @Date 2020/9/3 11:32
 */
@Data
@ApiModel("楼宇查询入参")
public class QryBulidingListInfoReq {
    @JsonProperty("BULIDING_NAME")
    @ApiModelProperty(name = "楼宇名称", value = "楼宇名称", required = true)
    private String bulidingName;
}
