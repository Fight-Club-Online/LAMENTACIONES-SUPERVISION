package com.lamentaciones.supervision.domain.repository;

import com.lamentaciones.supervision.domain.model.UserBan;
import java.util.Optional;
import java.util.List;

public interface UserBanRepository {
    UserBan save(UserBan userBan);
    Optional<UserBan> findActiveByUserId(String userId);
    Optional<UserBan> findMostRecentByUserId(String userId);
    List<UserBan> findHistoryByUserId(String userId);
    void deleteByUserId(String userId);
}