package parking

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ParkingBuilderTest {

  @Test
  fun testBuildBasicParking() {
    val parking = ParkingBuilder().withSquareSize(4).build()
    assertEquals(16, parking.getAvailableBays())
  }

  @Test
  fun testBuildParkingWithPedestrianExit() {
    val parking = ParkingBuilder().withSquareSize(3).withPedestrianExit(5).build()
    assertEquals(8, parking.getAvailableBays())
  }

  @Test
  fun testBuildParkingWithDisabledSlot() {
    val parking = ParkingBuilder().withSquareSize(2).withDisabledBay(2).build()
    assertEquals(4, parking.getAvailableBays())
  }

  @Test
  fun testBuildParkingWithPedestrianExitsAndDisabledSlots() {
    val parking =
      ParkingBuilder()
        .withSquareSize(10)
        .withPedestrianExit(8)
        .withPedestrianExit(42)
        .withPedestrianExit(85)
        .withDisabledBay(2)
        .build()
    assertEquals(97, parking.getAvailableBays())
  }
}
