package com.mdh.owner.waiting.presentation.dto;

import com.mdh.common.waiting.domain.WaitingStatus;

public record WaitingInfoRequestForOwner(
        WaitingStatus waitingStatus
) {
}