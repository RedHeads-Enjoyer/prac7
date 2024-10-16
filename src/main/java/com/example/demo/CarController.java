package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarRepository carRepository;

    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Car>> getCarById(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> ResponseEntity.ok(car))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Car> getAllCars(@RequestParam(name = "color", required = false)
                                String color) {
        Flux<Car> cars = carRepository.findAll();
        if (color != null) {
            cars = cars.filter(cat -> cat.getColor().equals(color));
        }
        return cars
                .map(this::transformCar).onErrorResume(e -> {
                    return Flux.error(new CustomException("Failed to fetch cars", e));
                })
                .onBackpressureBuffer();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Car> createCar(@RequestBody Car car) {
        return carRepository.save(car);
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Car>> updateCar(@PathVariable Long id,
                                               @RequestBody Car updatedCar) {
        return carRepository.findById(id)
                .flatMap(existingCar -> {
                    existingCar.setColor(updatedCar.getColor());
                    existingCar.setModel(updatedCar.getModel());
                    existingCar.setPrice(updatedCar.getPrice());
                    return carRepository.save(existingCar);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCar(@PathVariable Long id) {
        return carRepository.findById(id)
                .flatMap(existingCar ->
                        carRepository.delete(existingCar)
                                .then(Mono.just(new
                                        ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Car transformCar(Car car) {
        car.setModel(car.getModel().toUpperCase());
        return car;
    }
}