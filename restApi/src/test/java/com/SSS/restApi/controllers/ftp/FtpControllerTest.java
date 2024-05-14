package com.sss.restapi.controllers.ftp;

import com.sss.restapi.models.car.Car;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.repositories.car.CarRepository;
import com.sss.restapi.repositories.moto.MotoRepository;
import com.sss.restapi.services.ftp.FilesSentEvent;
import com.sss.restapi.services.ftp.FtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {FtpController.class, DefaultFtpSessionFactory.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class FtpControllerTest {
    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;
    @MockBean
    private CarRepository carRepository;
    @Autowired
    private FtpController ftpController;
    @MockBean
    private FtpService ftpService;
    @MockBean
    private MotoRepository motoRepository;
    @Autowired
    private DefaultFtpSessionFactory ftpSessionFactory;

    @BeforeEach
    void setUp() {
        ftpSessionFactory.setUsername("ftp");
        ftpSessionFactory.setPassword("ftp");
        ftpSessionFactory.setHost("localhost");
        ftpSessionFactory.setPort(21);
    }
    @EventListener
    void testHandleFilesSentEvent(ApplicationEventPublisher eventPublisher) throws Exception {
        FilesSentEvent event = new FilesSentEvent(this, "test.zip");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(ftpSessionFactory.getSession().readRaw("test.zip")).thenReturn(inputStream);

        ftpController.handleFilesSentEvent(event);

        verify(ftpService, times(1)).unzipArchive(any(), any());
        verify(ftpSessionFactory.getSession(), times(1)).remove("test.zip");
    }

    @Test
    void testPutAllVehiclesToFtpServer() throws Exception {
        when(carRepository.findAll()).thenReturn(new ArrayList<>());
        when(motoRepository.findAll()).thenReturn(new ArrayList<>());
        when(ftpService.createCsvString(Mockito.any(), Mockito.any()))
                .thenReturn("Create Csv String");
        doNothing().when(ftpService)
                .addFileToZip(Mockito.any(), Mockito.any(), Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/ftp/files/");

        MockMvcBuilders.standaloneSetup(ftpController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm");
        String currentTime = dateFormat.format(new Date());
        String filename = "vehicles-" + currentTime + ".zip";
        applicationEventPublisher.publishEvent(new FilesSentEvent(this, filename));
    }

    @Test
    void testPutAllVehiclesToFtpServer2() throws Exception {
        when(carRepository.findAll()).thenReturn(new ArrayList<>());
        when(motoRepository.findAll()).thenReturn(new ArrayList<>());
        when(ftpService.createCsvString(Mockito.any(), Mockito.any()))
                .thenReturn("Create Csv String");
        doThrow(new IOException("dd.MM.yyyy-HH.mm")).when(ftpService)
                .addFileToZip(Mockito.any(), Mockito.any(), Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/ftp/files/");

        MockMvcBuilders.standaloneSetup(ftpController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testPutAllVehiclesToFtpServer3() throws Exception {
        Car car = new Car();
        car.setBrand("dd.MM.yyyy-HH.mm");
        car.setId(1L);
        car.setModel("dd.MM.yyyy-HH.mm");

        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car);
        when(carRepository.findAll()).thenReturn(carList);
        when(motoRepository.findAll()).thenReturn(new ArrayList<>());
        when(ftpService.createCsvString(Mockito.any(), Mockito.any()))
                .thenReturn("Create Csv String");
        doNothing().when(ftpService)
                .addFileToZip(Mockito.any(), Mockito.any(), Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/ftp/files/");

        MockMvcBuilders.standaloneSetup(ftpController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testPutAllVehiclesToFtpServer4() throws Exception {
        when(carRepository.findAll()).thenReturn(new ArrayList<>());

        Moto moto = new Moto();
        moto.setBrand("dd.MM.yyyy-HH.mm");
        moto.setId(1L);
        moto.setModel("dd.MM.yyyy-HH.mm");

        ArrayList<Moto> motoList = new ArrayList<>();
        motoList.add(moto);
        when(motoRepository.findAll()).thenReturn(motoList);
        when(ftpService.createCsvString(Mockito.any(), Mockito.any()))
                .thenReturn("Create Csv String");
        doNothing().when(ftpService)
                .addFileToZip(Mockito.any(), Mockito.any(), Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/ftp/files/");

        MockMvcBuilders.standaloneSetup(ftpController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
