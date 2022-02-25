package com.damon.rmq.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RmqException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 911418878965576268L;
    private int code = 1;
    private String msg;

    public RmqException(String message) {
        super(message);
        this.msg = message;
    }

    /**
     * @param message
     * @param cause
     */
    public RmqException(String message, Throwable cause) {
        super(message, cause);
    }


}
