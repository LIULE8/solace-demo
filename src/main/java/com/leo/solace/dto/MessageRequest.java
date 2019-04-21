package com.leo.solace.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {
    @NotNull(message = "name can not be null")
    private String name;
    @NotNull(message = "code can not be null")
    private String code;
}
