package ru.medvedev.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.medvedev.application.domain.record.ExcelField;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum CancellationServiceType {
    VSK_INSURANCE_CARD(List.of(
            new ExcelField(5, "Номер полиса"),
            new ExcelField(23, "Код продукта"))),
    VSK_INSURANCE_OTHER(List.of(
            new ExcelField(5, "Номер полиса"),
            new ExcelField(23, "Код продукта")
    )),
    SOVCOM_INSURANCE(List.of(
            new ExcelField(0, "Номер полиса")
    ));
    private final List<ExcelField> fields;
}
