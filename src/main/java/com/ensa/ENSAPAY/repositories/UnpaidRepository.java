package com.ensa.ENSAPAY.repositories;

import com.ensa.ENSAPAY.entities.BillState;
import com.ensa.ENSAPAY.entities.Unpaid;
import com.ensa.ENSAPAY.entities.UnpaidState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnpaidRepository extends JpaRepository<Unpaid,Long>
{

    @Query(value = "SELECT * FROM unpaid WHERE client_id =:id and creditor_id=:creditorId and state=0", nativeQuery = true)
    List<Unpaid> getUnpaidsForTheCreditorAndClient(@Param("creditorId") Long creditorId,@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE unpaid SET state =:state WHERE id in :unpaidIds", nativeQuery = true)
    @Transactional
    void setUnpaidState(@Param("state") int state, @Param("unpaidIds") List<Long> unpaidIds);
}
