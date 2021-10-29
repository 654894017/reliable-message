package com.damon.rmq.api.model.dto.queue;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class QueueAddDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1284433227488306392L;

    @NotBlank
    private String businessName;

    @NotBlank
    private String consumerQueue;

    @NotBlank
    private String checkUrl;

    @NotNull
    private Integer checkDuration;

    @NotNull
    private Short checkTimeout;

    private String createUser;

    private String updateUser;
}
