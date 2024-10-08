package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidateCategoryTest {

    @Test
    fun categoriesReturnsFullCategoriesSet() {
        assertEquals(2, categories.size)
        assertTrue(categories.contains("Home"))
        assertTrue(categories.contains("College"))
        assertFalse(categories.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists() {
        assertTrue(isValidCategory("Home"))
        assertTrue(isValidCategory("home"))
        assertTrue(isValidCategory("COLLEGE"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist() {
        assertFalse(isValidCategory("Hom"))
        assertFalse(isValidCategory("colllege"))
        assertFalse(isValidCategory(""))
    }
}
