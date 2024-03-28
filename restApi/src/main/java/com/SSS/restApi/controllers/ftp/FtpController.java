package com.sss.restapi.controllers.ftp;

import com.sss.restapi.enums.VehicleHeaders;
import com.sss.restapi.models.car.Car;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.repositories.car.CarRepository;
import com.sss.restapi.repositories.moto.MotoRepository;
import com.sss.restapi.services.ftp.FilesSentEvent;
import com.sss.restapi.services.ftp.FtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ftp/files")
public class FtpController {
    private final CarRepository carRepository;
    private final MotoRepository motoRepository;
    private final DefaultFtpSessionFactory ftpSessionFactory;
    private final FtpService ftpService;

    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleFilesSentEvent(FilesSentEvent event) {
        String filename = event.getFilename();
        String fileNameWithoutZip = filename.substring(0, filename.length()-4);
        try (InputStream inputStream = ftpSessionFactory.getSession().readRaw(filename)) {
            File tempFile = File.createTempFile(fileNameWithoutZip, ".zip");
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                IOUtils.copy(inputStream, outputStream);
            }
            File targetDir = new File("restApi/src/main/resources/static/" + fileNameWithoutZip);
            ftpService.unzipArchive(tempFile, targetDir);
            log.info("File {} has been downloaded and extracted to resources folder", filename);
            ftpSessionFactory.getSession().remove(filename);
        } catch (IOException e) {
            log.error("Error handling FilesSentEvent: {}", e.getMessage());
        }
    }

    @PostMapping(value = "/")
    @Scheduled(cron = "${ftp.schedule.everyHourMF}")
    public void putAllVehiclesToFtpServer() {
        List<Car> cars = carRepository.findAll();
        List<Moto> motos = motoRepository.findAll();
        List<String[]> carData = new ArrayList<>();
        List<String[]> motoData = new ArrayList<>();
        for (Car car : cars) {
            carData.add(new String[]{"Car", car.getId().toString(), car.getBrand(), car.getModel()});
        }
        for (Moto moto : motos) {
            motoData.add(new String[]{"Moto", moto.getId().toString(), moto.getBrand(), moto.getModel()});
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm");
            String currentTime = dateFormat.format(new Date());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                ftpService.addFileToZip(zipOutputStream, "cars.csv", ftpService.createCsvString(VehicleHeaders.getAllHeaders(), carData));
                ftpService.addFileToZip(zipOutputStream, "motos.csv", ftpService.createCsvString(VehicleHeaders.getAllHeaders(), motoData));
            }

            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            String filename = "vehicles-" + currentTime + ".zip";
            ftpSessionFactory.getSession().write(inputStream, filename);
            log.info("FtpController PostController putAllVehiclesToFtpServer done at " + currentTime);
            eventPublisher.publishEvent(new FilesSentEvent(this, filename));
        } catch (Exception e) {
            log.error("FtpController PostController putAllVehiclesToFtpServer error " + e);
        }
    }

}
