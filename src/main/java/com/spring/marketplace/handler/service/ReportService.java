package com.spring.marketplace.handler.service;

import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportService  {

    @Value("${app.file.path}")
    private String fileBasePath;

    @SneakyThrows
    public void exportAllProductsInXlsxFile(List<Product> products){
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Products");

            Iterator<Product> iterator = products.iterator();
            int rowIndex = 0;
            while(iterator.hasNext()){
                Row row = sheet.createRow(rowIndex++);
                Product product = iterator.next();

                row.createCell(0).setCellValue(product.getId().toString());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getCategory().name());
                row.createCell(4).setCellValue(product.getPrice().doubleValue());
                row.createCell(5).setCellValue(product.getQuantity().intValue());
                row.createCell(6).setCellValue(product.getSku());
                row.createCell(7).setCellValue(product.getCreatedAt().toString());
                row.createCell(8).setCellValue(product.getOwner().getUsername());
            }

            Path path = Path.of(fileBasePath);
            if(!Files.exists(path)){
                Files.createDirectory(path);
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String fullPathToFile =fileBasePath + "report_" + now.format(formatter) + ".xlsx";

            FileOutputStream fileOutputStream = new FileOutputStream(fullPathToFile);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            log.info("Export data successful");
        }
    }

    public List<String> getReportFilesName() {
        File baseFile = new File(fileBasePath);
        File[] allFiles = Optional.ofNullable(baseFile.listFiles())
                .filter((array) -> array.length > 0)
                .orElseThrow(() -> new ApplicationException(ErrorType.FAILED_TO_GET_LIST_OF_FILES));

        return Arrays.stream(allFiles).map(File::getName).toList();
    }
}
