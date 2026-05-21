package com.example.freendo

import org.junit.Assert.assertEquals
import org.junit.Test

class EventAdapterTest {

    @Test
    fun testEventAdapterItemCount() {
        val events = listOf(
            Event(id = "1", title = "Beach Day"),
            Event(id = "2", title = "Hiking")
        )
        val adapter = EventAdapter(events)
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun testUpdateList() {
        val adapter = EventAdapter(emptyList())
        val newEvents = listOf(Event(id = "1", title = "New Event"))
        
        adapter.updateList(newEvents)
        
        assertEquals(1, adapter.itemCount)
    }
}
