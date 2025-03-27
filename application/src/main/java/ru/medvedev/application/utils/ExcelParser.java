package ru.medvedev.application.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@UtilityClass
public class ExcelParser {
    public static <T> Object parseExcelFile(MultipartFile file, Class<T> clazz) throws IOException {
        var inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        var sheet = workbook.getSheetAt(0);
        T result = clazz.cast(new Object());
        for (var i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row currentRow = sheet.getRow(i);
            Arrays.stream(clazz.getFields()).forEach(field -> {
                try {
                    result.getClass().getField(field.getName());
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        workbook.close();
        return null;
    }
}
