package com.example.freendo

data class Category(val name: String, val emoji: String)

val CATEGORIES = listOf(
    Category("Outdoor", "🌲"),
    Category("Food",    "🍕"),
    Category("Arts",    "🎨"),
    Category("Sports",  "⚽"),
    Category("Games",   "🎮"),
    Category("Music",   "🎵")
)

val ACTIVITIES_MAP = mapOf(
    "Outdoor" to listOf("Hiking trail", "Beach picnic", "Camping night", "Kayaking", "Cycling tour"),
    "Food"    to listOf("Pizza making class", "Street food tour", "BBQ cookout", "Sushi night", "Food festival"),
    "Arts"    to listOf("Painting workshop", "Open mic night", "Pottery class", "Photography walk", "Gallery visit"),
    "Sports"  to listOf("Basketball pickup game", "Tennis match", "Yoga in the park", "Swimming race", "Volleyball"),
    "Games"   to listOf("Board game café", "Escape room", "Bowling night", "Trivia night", "Card tournament"),
    "Music"   to listOf("Live concert", "Karaoke", "Jam session", "Music festival", "Drum circle")
)