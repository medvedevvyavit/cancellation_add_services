package ru.medvedev.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.medvedev.application.domain.VskCardSourceDto;
import ru.medvedev.application.domain.record.ExcelField;
import ru.medvedev.application.enums.CancellationServiceType;
import ru.medvedev.application.utils.UniversalExcelParser;

import java.util.List;

import static ru.medvedev.application.enums.CancellationServiceType.VSK_INSURANCE_CARD;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/excel")
public class ExcelController {

    @PostMapping("/vsk-card")
    public ResponseEntity<List<VskCardSourceDto>> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        var result = UniversalExcelParser.parse(file.getInputStream(), VskCardSourceDto.class, VSK_INSURANCE_CARD);
        return ResponseEntity.ok(result);
    }
}
