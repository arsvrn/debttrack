// File: com.debttrack.platform.repository.ConfirmationTokenRepository.java
package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
}