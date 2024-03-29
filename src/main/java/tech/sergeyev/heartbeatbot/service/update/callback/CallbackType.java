package tech.sergeyev.heartbeatbot.service.update.callback;

import tech.sergeyev.heartbeatbot.exception.CallbackTypeConversionError;

public enum CallbackType {
    UNSUBSCRIBE,
    LOG,
    UNKNOWN,
    RELEASE_INFO,
    STAGE_STATUS;

    public static CallbackType convertToCallbackType(String name) throws CallbackTypeConversionError {
        for (var type : CallbackType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new CallbackTypeConversionError("Unable to convert " + name + " to CallbackType");
    }
}
