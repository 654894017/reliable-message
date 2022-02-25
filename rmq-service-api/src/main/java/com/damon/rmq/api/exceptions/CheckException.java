package com.damon.rmq.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 6005969376941549930L;
    private int code = 1;
    private String msg;

    public CheckException(String message) {
        super(message);
        this.msg = message;
    }
}
