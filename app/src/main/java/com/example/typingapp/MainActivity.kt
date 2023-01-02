package com.example.typingapp

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import androidx.core.widget.addTextChangedListener
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val AVG_CHARACTERS_PER_ROW = 22
    private val WORD_FILE_NAME = "Words.txt"

    private lateinit var wordList: LinearLayout
    private lateinit var words: ArrayList<String>
    private var currentWords = arrayListOf<WordData>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        words = loadWords()

        val mainInput = findViewById<EditText>(R.id.etMain)
        wordList = findViewById(R.id.wordList)
        wordList.setOnClickListener {
            mainInput.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(mainInput, InputMethodManager.SHOW_IMPLICIT)
        }
        mainInput.addTextChangedListener {
            val txt = mainInput.text.toString()
            val currentWordData = currentWords[currentIndex]

            if (currentWordData.word.contains(txt)) {
                println("not correct")
                currentWordData.view.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                println("correct")
                currentWordData.view.setTextColor(ContextCompat.getColor(this, R.color.red))
            }

            if (txt.endsWith(' ')) {
                val typedWord = txt.substring(0, txt.length - 1)
                if (typedWord == currentWordData.word) {
                    val newWordData = currentWords[++currentIndex]
                    newWordData.view.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
                    currentWordData.view.setTextColor(ContextCompat.getColor(this, R.color.purple_200))
                    mainInput.text.clear()

                    if (newWordData.index != currentWordData.index && newWordData.index == 2) {
                        shiftWords()
                    }
                }
            }
        }

        wordList.forEachIndexed { index, view ->
            if (view is LinearLayout) {
                addRow(view, index)
            }
        }
        currentWords[0].view.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
    }

    private fun addRow(container: LinearLayout, index: Int) {
        val row = getWordRow()
        row.forEach {
            val wordLabel = TextView(this)
            wordLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            wordLabel.text = it
            wordLabel.typeface = ResourcesCompat.getFont(this, R.font.roboto_mono)
            container.addView(wordLabel)
            val params = wordLabel.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            wordLabel.layoutParams = params

            currentWords.add(WordData(it, wordLabel, index))
        }
    }

    private fun shiftWords() {
        currentWords.forEach {
            if (it.index == 0) {
                (it.view.parent as ViewGroup).removeView(it.view)
                currentIndex--
            }
        }

        currentWords = currentWords.filter { it.index > 0 } as ArrayList<WordData>
        currentWords.forEach {
            it.index--
        }

        try {
            val newRowContainer = LinearLayout(this)
            newRowContainer.orientation = LinearLayout.HORIZONTAL
            wordList.addView(newRowContainer)
            addRow(newRowContainer, 2)
        } catch (e: Throwable) {
            println("Error: $e")
        }
    }

    private fun loadWords(): ArrayList<String> {
        val result = arrayListOf<String>()

        application.assets.open(WORD_FILE_NAME).bufferedReader().forEachLine {
            result.add(it.lowercase())
        }

        return result
    }

    private fun getWordRow(): ArrayList<String> {
        val result = arrayListOf<String>()
        var length = 0
        var lastWord = ""

        while (length < AVG_CHARACTERS_PER_ROW) {
            val index = Random.nextInt(words.size)
            val randomWord = words[index]
            if (randomWord != lastWord) {
                length += randomWord.length
                lastWord = randomWord
                result.add(randomWord)
            }
        }

        return result
    }
}
