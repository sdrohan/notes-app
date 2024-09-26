package utils

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class NumberUtilitiesTest {

    @Test
    fun validRangeWorksWithPositiveTestData() {
        assertTrue(validRange(1, 1, 1))
        assertTrue(validRange(1, 1, 2))
        assertTrue(validRange(1, 0, 1))
        assertTrue(validRange(1, 0, 2))
        assertTrue(validRange(-1, -2, -1))
    }

    @Test
    fun validRangeWorksWithNegativeTestData() {
        assertFalse(validRange(1, 0, 0))
        assertFalse(validRange(1, 1, 0))
        assertFalse(validRange(1, 2, 1))
        assertFalse(validRange(-1, -1, -2))
    }
}