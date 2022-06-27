package com.ensa.ENSAPAY.repositories;

import com.ensa.ENSAPAY.entities.Unpaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnpaidRepository extends JpaRepository<Unpaid,Long>
{
    @Query(value = "SELECT * FROM unpaid WHERE client_id =:creditorId and creditor_id=:id", nativeQuery = true)
    List<Unpaid> getUnpaidsForTheCreditorAndClient(@Param("creditorId") Long creditorId,@Param("id") Long id);
}
