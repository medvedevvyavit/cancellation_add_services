package ru.medvedev.application.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class UniversalExcelParser {

    private static final DataFormatter FORMATTER = new DataFormatter();

    static {
        FORMATTER.setUse4DigitYearsInAllDateFormats(true);
    }

    public static <T> List<T> parse(InputStream inputStream, Class<T> clazz) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        var sheet = workbook.getSheetAt(0);
        List<T> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (var currentRow : sheet) {
            if (currentRow.getRowNum() == 0) {
                continue;
            }
            T object = clazz.getDeclaredConstructor().newInstance();
            for (Cell currentCell : currentRow) {
                Field field = fields[currentCell.getColumnIndex()];
                field.setAccessible(true);
                field.set(object, getValue(currentRow.getCell(currentCell.getColumnIndex())));
            }
            result.add(object);
        }
        return result;
    }

    private static String getValue(Cell cell) {
        if (Objects.isNull(cell)) {
            return null;
        }
        return FORMATTER.formatCellValue(cell);
    }
}
