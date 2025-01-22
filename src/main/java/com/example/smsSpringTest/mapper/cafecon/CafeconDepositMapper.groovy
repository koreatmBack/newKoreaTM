package com.example.smsSpringTest.mapper.cafecon

import com.example.smsSpringTest.model.cafecon.Deposit
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface CafeconDepositMapper {

    // 입금 정보 등록
    @Insert("""
        INSERT INTO cafecon_deposit(
            num
            , user_id
            , charge_point
            , deposit_amount
            , depositor_name
            , status
            , charge_request
            , invoice
        ) VALUES (
            #{deposit.num}
            , #{deposit.userId}
            , #{deposit.chargePoint}
            , #{deposit.depositAmount}
            , #{deposit.depositorName}
            , '충전예정'
            , '신청완료'
            , #{deposit.invoice}
        )
    """)
    int addDepositInfo(@Param("deposit") Deposit deposit)

    // 거래번호 생성
    @Select("""
        SELECT SUBSTRING(dm.num, 10, 6)
        FROM cafecon_deposit dm
        WHERE dm.num LIKE CONCAT(#{datePrefix}, '%')
        ORDER BY dm.num DESC 
        LIMIT 1
    """)
    Integer findMaxSequenceForToday(@Param("datePrefix") String datePrefix)

    // 상태 변경하기 (보류확인 or 충전완료)
    @Update("""
<script>
        UPDATE cafecon_deposit
        <set>
            <if test="deposit.status == '보류확인'">
                status = #{deposit.status},
                charge_request = '재신청'
            </if>
            <if test="deposit.status == '충전완료'">
                status = #{deposit.status},
                charge_request = null
            </if>
            <if test="deposit.invoice != null">
                ,invoice = #{deposit.invoice}
            </if>
        </set>
        WHERE num = #{deposit.num}
</script>        
    """)
    int changeStatus(@Param("deposit") Deposit deposit)

    // 충전 완료시, 포인트 로그 찍기 위해 회원 id, 충전 포인트 찾아야함
    @Select("""
        SELECT user_id
               , charge_point
        FROM cafecon_deposit
        WHERE num = #{deposit.num}
    """)
    Deposit findOne(@Param("deposit") Deposit deposit)

}