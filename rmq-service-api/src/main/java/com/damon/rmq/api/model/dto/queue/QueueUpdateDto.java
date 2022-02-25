package com.damon.rmq.api.model.dto.queue;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class QueueUpdateDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7357756131942314109L;

    @NotBlank
    private String id;

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

    private String updateUser;
}
