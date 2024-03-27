package com.sss.restapi.services.ftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FtpService {
    public void printToCsv(DefaultFtpSessionFactory defaultFtpSessionFactory, String[] headers, List<String[]> data, String fileName) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();
        try (StringWriter stringWriter = new StringWriter(); final CSVPrinter printer = new CSVPrinter(stringWriter, csvFormat)) {
            printer.printRecords(data);
            InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes());
            defaultFtpSessionFactory.getSession().write(inputStream, fileName);
        } catch (IOException | RuntimeException e) {
            log.error("printToCsv broke " + e);
        }
    }

}
