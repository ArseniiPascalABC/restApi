package com.sss.restapi.controllers.ftp;

import com.sss.restapi.models.car.Car;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.repositories.car.CarRepository;
import com.sss.restapi.repositories.moto.MotoRepository;
import com.sss.restapi.services.ftp.FtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ftp/files")
public class FtpController {
    private final CarRepository carRepository;
    private final MotoRepository motoRepository;
    private final DefaultFtpSessionFactory ftpSessionFactory;
    private final FtpService ftpService;
    private static final String[] HEADERS = {"vehicle", "id", "brand", "model"};
    private static final String FILENAME = "vehicles.csv";

    @GetMapping("/")
    public String fetchFromFTP() {
        try {
            InputStream inputStream = ftpSessionFactory.getSession().readRaw(FILENAME);
            String data = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            return data;
        } catch (IOException e) {
            log.error("Ftp Controller" + e);
            return "Error fetching data from FTP server";
        }
    }
    @PostMapping(value = "/")
    public String putAllVehiclesToFtpServer() {
        List<Car> cars = carRepository.findAll();
        List<Moto> motos = motoRepository.findAll();
        List<String[]> vehicleData = new ArrayList<>();
        for (Car car : cars) {
            vehicleData.add(new String[]{"Car", car.getId().toString(), car.getBrand(), car.getModel()});
        }
        for (Moto moto : motos) {
            vehicleData.add(new String[]{"Moto", moto.getId().toString(), moto.getBrand(), moto.getModel()});
        }

        try {
            ftpService.printToCsv(ftpSessionFactory, HEADERS, vehicleData, FILENAME);
            return "Data sent to FTP server successfully";
        } catch (Exception e) {
            log.error("FtpController PostController putAllVehiclesToFtpServer error " + e);
            return "Errors come to us";
        }
    }

}
