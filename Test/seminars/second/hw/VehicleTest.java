package seminars.second.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    Car car;
    Motorcycle motorcycle;

    @BeforeEach
    void setUp() {
        car = new Car("Dodge", "Ram", 2010);
        motorcycle = new Motorcycle("BMW", "X1000", 2011);
    }

    @Test
    public void testCarIsInstanceOfVehicle() {
        assertTrue(car instanceof Vehicle);
    }

    @Test
    public void testQualityOfWheels() {
        assertThat(car.getNumWheels()).isEqualTo(4);
        assertThat(motorcycle.getNumWheels()).isEqualTo(2);
    }

    @Test
    public void testSpeed() {
        car.testDrive();
        motorcycle.testDrive();

        assertThat(car.getSpeed()).isEqualTo(60);
        assertThat(motorcycle.getSpeed()).isEqualTo(75);
    }

    @Test
    public void testParking() {
        car.testDrive();
        car.park();
        motorcycle.testDrive();
        motorcycle.park();

        assertThat(car.getSpeed()).isEqualTo(0);
        assertThat(motorcycle.getSpeed()).isEqualTo(0);
    }

}