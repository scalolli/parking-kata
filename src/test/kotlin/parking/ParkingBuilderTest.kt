package parking

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParkingBuilderTest {

    @Test
    fun testBuildBasicParking() {
        val parking = ParkingBuilder()
            .withSquareSize(4)
            .build()
        assertEquals(16, parking.getAvailableBays())
    }

    @Test
    fun testBuildParkingWithPedestrianExit() {
        val parking = ParkingBuilder()
            .withSquareSize(3)
            .withPedestrianExit(5)
            .build()
        assertEquals(8, parking.getAvailableBays())
    }

    @Test
    fun testBuildParkingWithDisabledSlot() {
        val parking = ParkingBuilder()
            .withSquareSize(2)
            .withDisabledBay(2)
            .build()
        assertEquals(4, parking.getAvailableBays())
    }
}