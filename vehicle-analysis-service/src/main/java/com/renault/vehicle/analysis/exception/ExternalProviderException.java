package com.renault.vehicle.analysis.exception;

import com.renault.vehicle.analysis.enums.ErrorCode;

public class ExternalProviderException extends BusinessException {

    public ExternalProviderException(String details) {

        super(
                ErrorCode.EXTERNAL_PROVIDER_UNAVAILABLE,
                details
        );
    }
}