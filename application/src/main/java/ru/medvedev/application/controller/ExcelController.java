package ru.medvedev.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.medvedev.application.domain.SmsAdditionalServiceDto;
import ru.medvedev.application.service.ExcelParserService;
import ru.medvedev.application.utils.ExcelParser;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/excel")
public class ExcelController {

    private final ExcelParserService excelParserService;

    @PostMapping("/sms")
    public ResponseEntity<Void> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
//        excelParserService.parseExcelFile(file);

        var smsAdditionalServiceDto = (SmsAdditionalServiceDto) ExcelParser.parseExcelFile(file, SmsAdditionalServiceDto.class);

        return ResponseEntity.ok().build();
    }
}
