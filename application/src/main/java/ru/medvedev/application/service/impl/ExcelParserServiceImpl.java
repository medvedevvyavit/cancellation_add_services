package ru.medvedev.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.medvedev.application.service.ExcelParserService;

import java.io.IOException;

@Slf4j
@Service
public class ExcelParserServiceImpl implements ExcelParserService {

    @Override
    public void parseExcelFile(MultipartFile file) throws IOException {
        var inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        var sheet = workbook.getSheetAt(0);

        for (var i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row currentRow = sheet.getRow(i);
            for (Cell currentCell : currentRow) {
                log.info("cell = {}", currentRow.getCell(0).getStringCellValue());
            }
        }

        workbook.close();
    }
}
