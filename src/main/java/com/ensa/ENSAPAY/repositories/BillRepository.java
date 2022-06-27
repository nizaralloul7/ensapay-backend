package com.ensa.ENSAPAY.repositories;

import com.ensa.ENSAPAY.entities.Bill;
import com.ensa.ENSAPAY.entities.BillState;
import com.ensa.ENSAPAY.entities.Unpaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface BillRepository extends JpaRepository<Bill, Long>
{
    @Modifying
    @Query(value = "UPDATE bill SET state = :billState WHERE id = :billId", nativeQuery = true)
    @Transactional
    void changeBillStatus(@Param("billState") int billState, @Param("billId") Long billId);

    @Query(value = "SELECT * FROM bill WHERE state = :billState AND client_id = :clientId ", nativeQuery = true)
    Optional<List<Bill>> findPaidBills(@Param("billState") int billState, @Param("clientId") Long clientId);

    @Query(value="SELECT * FROM bill WHERE state=:billState and client_id = :clientId",nativeQuery = true)
    Optional<List<Bill>> billHistory(@Param("billState") BillState billState, @Param("clientId") Long clientId);

    @Modifying
    @Query(value = "UPDATE bill SET verification_code =:verificationCode WHERE id = :billId", nativeQuery = true)
    @Transactional
    void setVerificationCode(@Param("verificationCode") String verificationCode, @Param("billId") Long billId);

}
