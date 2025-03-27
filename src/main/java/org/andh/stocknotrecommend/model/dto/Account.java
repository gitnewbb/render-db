package org.andh.stocknotrecommend.model.dto;

public record Account(
        Long account_id,
        String nickname,
        String password,
        String title,
        String body_data,
        java.sql.Timestamp created_at,
        int recommended,
        int notrecommended
) {}
