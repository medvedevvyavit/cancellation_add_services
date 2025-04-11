package ru.medvedev.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.medvedev.application.domain.SmsAdditionalServiceDto;
import ru.medvedev.application.utils.UniversalExcelParser;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/excel")
public class ExcelController {

    @PostMapping("/sms")
    public ResponseEntity<List<SmsAdditionalServiceDto>> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        var result = UniversalExcelParser.parse(file.getInputStream(), SmsAdditionalServiceDto.class);
        return ResponseEntity.ok(result);
    }
}
