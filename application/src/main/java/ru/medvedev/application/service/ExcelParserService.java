package ru.medvedev.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelParserService {
    void parseExcelFile(MultipartFile file) throws IOException;
}
