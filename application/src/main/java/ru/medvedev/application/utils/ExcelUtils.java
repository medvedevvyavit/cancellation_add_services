package ru.medvedev.application.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.util.Objects;

@UtilityClass
public class ExcelUtils {
    private static final DataFormatter FORMATTER = new DataFormatter();

    static {
        FORMATTER.setUse4DigitYearsInAllDateFormats(true);
    }

    public static String getValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return null;
        }
        return FORMATTER.formatCellValue(cell);
    }
}
