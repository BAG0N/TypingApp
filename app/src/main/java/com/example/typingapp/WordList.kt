package com.example.typingapp

import java.io.File

val WordList = arrayOf("hello", "test", "apple", "banana", "of", "and", "a")

fun main() {
    val words = arrayListOf<String>()
    File("C:\\Users\\alpen\\AndroidStudioProjects\\TypingApp\\app\\src\\main\\assets\\Words.txt").forEachLine {
        words.add(it.lowercase())
    }
    println(words.joinToString(", "))
}
