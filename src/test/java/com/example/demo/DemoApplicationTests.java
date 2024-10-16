package com.example.demo;

import com.example.demo.Car;
import com.example.demo.CarController;
import com.example.demo.CarRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class DemoApplicationTests {
	@Test
	public void testGetCarById() {
		Car car = new Car();
		car.setId(1L);
		car.setModel("Toyota Camry");
		car.setPrice(24000.0f);
		car.setColor("Black");

		CarRepository carRepository = Mockito.mock(CarRepository.class);
		when(carRepository.findById(1L)).thenReturn(Mono.just(car));

		CarController carController = new CarController(carRepository);

		ResponseEntity<Car> response = carController.getCarById(1L).block();
        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(car, response.getBody());
	}
	@Test
	public void testGetAllCars() {
		Car car1 = new Car();
		car1.setModel("Toyota Camry");
		car1.setPrice(24000.0f);
		car1.setColor("Black");
		Car car2 = new Car();
		car2.setModel("Ford Mustang");
		car2.setPrice(30000.0f);
		car2.setColor("Red");

		CarRepository carRepository = Mockito.mock(CarRepository.class);
		when(carRepository.findAll()).thenReturn(Flux.just(car1, car2));

		CarController carController = new CarController(carRepository);

		Flux<Car> response = carController.getAllCars(null);
		assertEquals(2, response.collectList().block().size());
	}

	@Test
	public void testCreateCar() {
		Car car = new Car();
		car.setId(1L);
		car.setModel("Toyota Camry");
		car.setPrice(24000.0f);
		car.setColor("Black");

		CarRepository carRepository = Mockito.mock(CarRepository.class);
		when(carRepository.save(car)).thenReturn(Mono.just(car));

		CarController carController = new CarController(carRepository);

		Mono<Car> response = carController.createCar(car);
		assertEquals(car, response.block());
	}

	@Test
	public void testUpdateCar() {
		Car existingCar = new Car();
		existingCar.setModel("Toyota Camry");
		existingCar.setPrice(24000.0f);
		existingCar.setColor("Black");
		Car updatedCar = new Car();
		updatedCar.setModel("Ford Mustang");
		updatedCar.setPrice(30000.0f);
		updatedCar.setColor("Red");

		CarRepository carRepository = Mockito.mock(CarRepository.class);
		when(carRepository.findById(1L)).thenReturn(Mono.just(existingCar));
		when(carRepository.save(existingCar)).thenReturn(Mono.just(updatedCar));

		CarController carController = new CarController(carRepository);

		ResponseEntity<Car> response = carController.updateCar(1L, updatedCar).block();
        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedCar, response.getBody());
	}

	@Test
	public void testDeleteCat() {
		Car car = new Car();
		car.setId(1L);
		car.setModel("Toyota Camry");
		car.setPrice(24000.0f);
		car.setColor("Black");

		CarRepository carRepository = Mockito.mock(CarRepository.class);
		when(carRepository.findById(1L)).thenReturn(Mono.just(car));
		when(carRepository.delete(car)).thenReturn(Mono.empty());

		CarController carController = new CarController(carRepository);

		ResponseEntity<Void> response = carController.deleteCar(1L).block();
        assert response != null;
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}