package com.ensa.ENSAPAY.repositories;

import com.ensa.ENSAPAY.entities.Creditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditorRepository extends JpaRepository<Creditor,Long>
{
    Optional<Creditor> findCreditorByTitle(String title);
}
