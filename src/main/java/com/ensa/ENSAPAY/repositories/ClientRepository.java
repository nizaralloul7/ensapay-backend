package com.ensa.ENSAPAY.repositories;

import com.ensa.ENSAPAY.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long>
{
    Optional<Client> findClientByUsername(String username);

    @Modifying
    @Query(value="UPDATE client SET password = :newPassword, is_password_changed = :isPasswordChanged WHERE id = :clientId", nativeQuery = true)
    @Transactional
    void changePasswordInFirstLogin(@Param("newPassword") String newPassword, @Param("isPasswordChanged") boolean isPasswordChanged, @Param("clientId") Long clientId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE client SET balance = :newBalance WHERE id = :clientId", nativeQuery = true)
    void changeClientBalanceById(@Param("newBalance")BigDecimal newBalance, @Param("clientId")Long clientId);

    @Query(value="SELECT * FROM client WHERE created_by = :myId", nativeQuery = true)
    List<Client> getRelatedClients(@Param("myId")Long myId);
}
