package ru.medvedev.application.domain;

import lombok.Data;

@Data
public class VskCardSourceDto {
    private String insuranceContractNumber;
    private String serviceCode;
}
