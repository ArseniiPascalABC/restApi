package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.repositories.car.CarRepository;
import com.SSS.restApi.responses.soap.CarResponse;
import jakarta.jws.WebService;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@WebService(
        serviceName = "CarService",
        endpointInterface = "com.SSS.restApi.controllers.soap.CarService")
public class CarServiceImpl implements CarService{

    private final CarRepository carRepository;
    private final CarDAO carDAO;

//    @Override
//    public Car getVehicleById(Long id) {
//        return carRepository.findById(id).orElse(null);
//    }

    @Override
    public CarResponse getVehicleById(Long id) {
//        Car car = carRepository.findById(id).orElse(null);
//        CarResponse response = new CarResponse();
//        response.setData(car);
//        if (car != null) {
//            response.setMessage("Машина найдена");
//            response.setSuccess(true);
//        } else {
//            response.setMessage("Машина с таким ID не найдена");
//            response.setSuccess(false);
//        }
//        return response;
    return null;
    }

//    @Override
//    public SoapCarListResponse getVehiclesByBrand(String brand) {
//        List<Car> cars = carRepository.findAllByBrandIgnoreCase(brand);
//        if (cars.isEmpty()) {
//            log.warn("CarController getCarsByBrand, the cars the user was looking for were not found in database cars");
//            return new SoapCarListResponse(new ArrayList<>());
//        }
//        SoapCarListResponse soapCarListResponse = new SoapCarListResponse(cars);
//        log.info("CarController getCarsByBrand, the cars the user was looking for were found");
//        return soapCarListResponse;

//    }

//    @Override
    public CarResponse getVehiclesByBrand(String brand) throws JAXBException {
//        List<Car> cars = carRepository.findAllByBrandIgnoreCase(brand);
//        CarResponse response = new CarResponse();
//        if (!cars.isEmpty()) {
//            response.setMessage("Машины найдены");
//            SoapCarListResponse soapCarListResponse = new SoapCarListResponse(cars);
//            response.setData(soapCarListResponse);
//            response.setSuccess(true);
//        } else {
//            response.setMessage("Машины с таким брендом не найдены");
//            response.setSuccess(false);
//        }
//        return response;
    return null;
    }
//    @Override
    public CarResponse addVehicle(Car car) {
//        CarResponse response = new CarResponse();
//        try {
//            carDAO.save(car);
//            log.info("CarController addCar, car was added");
//            response.setMessage("Запись добавлена");
//            response.setSuccess(true);
//        } catch (Exception e) {
//            log.info("CarController addCar, car was not added");
//            response.setMessage("Запись не была добавлена");
//            response.setSuccess(false);
//        }
//        return response;
    return null;
    }
}
