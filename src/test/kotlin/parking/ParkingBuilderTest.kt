package parking

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParkingBuilderTest {

    @Test
    fun testBuildBasicParking() {
        val parking = ParkingBuilder().withSquareSize(4).build()
        assertEquals(16, parking.getAvailableBays())
    }
}