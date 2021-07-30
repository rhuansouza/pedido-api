package com.costanzo.pedidoapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {

    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
    @NotEmpty
    private String district;
    @NotEmpty
    private String city;
    @NotEmpty
    private String phone;
}
