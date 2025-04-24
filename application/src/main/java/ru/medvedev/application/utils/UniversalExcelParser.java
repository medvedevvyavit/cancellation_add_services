package ru.medvedev.application.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.medvedev.application.enums.CancellationServiceType;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static ru.medvedev.application.utils.ExcelUtils.getValue;

@UtilityClass
public class UniversalExcelParser {
    public static <T> List<T> parse(InputStream inputStream, Class<T> clazz, CancellationServiceType serviceType) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        var sheet = workbook.getSheetAt(0);
        List<T> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        if (serviceType.getFields().size() != fields.length) {
            throw new RuntimeException("Excel fields count does not match number of fields");
        }
        for (var currentRow : sheet) {
            if (currentRow.getRowNum() == 0) {
                continue;
            }
            T object = clazz.getDeclaredConstructor().newInstance();
            for (int i = 0; i < serviceType.getFields().size(); i++) {
                var excelField = serviceType.getFields().get(i);
                Field field = fields[i];
                field.setAccessible(true);
                field.set(object, getValue(currentRow.getCell(excelField.index())));
            }
            result.add(object);
        }
        return result;
    }
}
