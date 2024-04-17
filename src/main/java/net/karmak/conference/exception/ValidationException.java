package net.karmak.conference.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ValidationException extends RuntimeException {
    private final Integer code;

    public ValidationException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
