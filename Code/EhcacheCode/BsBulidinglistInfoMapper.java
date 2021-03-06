package com.sitech.cbn.cust.mapper;

import com.sitech.cbn.cust.po.BsBulidinglistInfo;
import com.sitech.cbn.cust.po.BsBulidinglistInfoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BsBulidinglistInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int deleteByExample(BsBulidinglistInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int deleteByPrimaryKey(@Param("buildingNo") Long buildingNo, @Param("tenancyCode") String tenancyCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int insert(BsBulidinglistInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int insertSelective(BsBulidinglistInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    BsBulidinglistInfo selectByPrimaryKey(@Param("buildingNo") Long buildingNo, @Param("tenancyCode") String tenancyCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int updateByExampleSelective(@Param("record") BsBulidinglistInfo record, @Param("example") BsBulidinglistInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int updateByExample(@Param("record") BsBulidinglistInfo record, @Param("example") BsBulidinglistInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int updateByPrimaryKeySelective(BsBulidinglistInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bs_bulidinglist_info
     *
     * @mbg.generated Thu Sep 03 11:41:43 CST 2020
     */
    int updateByPrimaryKey(BsBulidinglistInfo record);

    List<BsBulidinglistInfo> qryBulidingListInfoDao(@Param("bulidingName") String bulidingName);

    void updateBulidingNo(String valueOf, Long valueof1);

    void deleteBulidingInfo(String s);
}