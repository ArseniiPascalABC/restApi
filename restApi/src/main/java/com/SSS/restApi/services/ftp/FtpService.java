package com.sss.restapi.services.ftp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class FtpService {
    public String createCsvString(String[] headers, List<String[]> data) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();
        try (StringWriter stringWriter = new StringWriter(); final CSVPrinter printer = new CSVPrinter(stringWriter, csvFormat)) {
            printer.printRecords(data);
            return stringWriter.toString();
        } catch (IOException e) {
            log.error("Failed to create CSV string: " + e);
            return "";
        }
    }
    public void addFileToZip(ZipOutputStream zipOutputStream, String fileName, String fileContent) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(fileContent.getBytes());
        zipOutputStream.closeEntry();
    }

    public void unzipArchive(File zipFile, File destinationDir) throws IOException {
        destinationDir.mkdirs();
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destinationDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream input = zip.getInputStream(entry);
                         OutputStream output = new FileOutputStream(entryDestination)) {
                        IOUtils.copy(input, output);
                    }
                }
            }
        }
    }
}
