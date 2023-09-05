package parking

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.Ignore
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ParkingTest {

    private fun getParking() =
        ParkingBuilder()
            .withSquareSize(5)
            .withPedestrianExit(8)
            .withPedestrianExit(12)
            .withDisabledBay(5)
            .withDisabledBay(10)
            .build()

    @Test
    fun testGetAvailableBays() {
        val parking = getParking()
        assertEquals(23, parking.getAvailableBays())
    }

    @Test
    fun testParkCarVehicleTypeC() {
        val parking = getParking()
        assertEquals(7, parking.parkCar('C'))
        assertEquals(22, parking.getAvailableBays())
    }

    @Test
    fun testParkCarVehicleTypeM() {
        val parking = getParking()
        assertEquals(7, parking.parkCar('M'))
        assertEquals(22, parking.getAvailableBays())
    }

    @Test
    fun testParkCarTwoVehicles() {
        val parking = getParking()
        assertEquals(7, parking.parkCar('C'))
        assertEquals(22, parking.getAvailableBays())
        assertEquals(9, parking.parkCar('M'))
        assertEquals(21, parking.getAvailableBays())
    }

    @Test
    fun testParkCarDisabled() {
        val parking = getParking()
        assertEquals(10, parking.parkCar('D'))
        assertEquals(22, parking.getAvailableBays())
        assertEquals(5, parking.parkCar('D'))
        assertEquals(21, parking.getAvailableBays())
        assertEquals(-1, parking.parkCar('D'))
        assertEquals(21, parking.getAvailableBays())
    }

    @Test
    fun testUnParkCar() {
        val parking = getParking()
        val firstCarBayIndex = parking.parkCar('C')
        assertTrue(parking.unParkCar(firstCarBayIndex))
        assertFalse(parking.unParkCar(-1))
        assertEquals(23, parking.getAvailableBays())

        val secondCarBayIndex = parking.parkCar('D')
        assertTrue(parking.unParkCar(secondCarBayIndex))
        assertEquals(23, parking.getAvailableBays())
    }

    @Test
    fun testCarParkDisplay() {
        val parking = getParking()
        val parkingHumanReadable =
            """UUUUU
              |@UU=U
              |@U=UU
              |UUUUU
              |UUUUU""".trimMargin()
        println(parkingHumanReadable)
        println(parking.toString())
        assertEquals(parkingHumanReadable, parking.toString())
    }
}
