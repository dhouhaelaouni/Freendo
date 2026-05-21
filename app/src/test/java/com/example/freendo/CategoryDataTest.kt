package com.example.freendo

import org.junit.Assert.*
import org.junit.Test

class CategoryDataTest {

    @Test
    fun testCategoriesListNotEmpty() {
        assertTrue(CATEGORIES.isNotEmpty())
    }

    @Test
    fun testCategoriesCount() {
        assertEquals(6, CATEGORIES.size)
    }

    @Test
    fun testActivitiesMapHasAllCategories() {
        CATEGORIES.forEach { category ->
            assertTrue("Map should contain activities for ${category.name}", ACTIVITIES_MAP.containsKey(category.name))
            val activities = ACTIVITIES_MAP[category.name]
            assertNotNull(activities)
            assertTrue("Activities for ${category.name} should not be empty", activities!!.isNotEmpty())
        }
    }

    @Test
    fun testSpecificCategoryData() {
        val outdoor = CATEGORIES.find { it.name == "Outdoor" }
        assertNotNull(outdoor)
        assertEquals("🌲", outdoor?.emoji)
        
        val outdoorActivities = ACTIVITIES_MAP["Outdoor"]
        assertTrue(outdoorActivities?.contains("Hiking trail") ?: false)
    }
}
