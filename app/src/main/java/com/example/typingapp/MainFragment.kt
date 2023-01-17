package com.example.typingapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import kotlin.math.ceil
import kotlin.random.Random

class MainFragment : Fragment(R.layout.fragment_main) {
    private val AVG_CHARACTERS_PER_ROW = 22
    private val WORD_FILE_NAME = "Words.txt"
    private val TOTAL_LINES = 3

    private lateinit var wordList: LinearLayout
    private lateinit var words: ArrayList<String>
    private lateinit var timerLabel: TextView
    private lateinit var mainInput: EditText
    private lateinit var fragmentView: View

    private var currentWords = arrayListOf<WordData>()
    private var currentIndex = 0
    private var isStarted = false
    private var totalCharacters = 0

    var timeInSeconds: Long = 15

    private var sessionID = Random.nextDouble().toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = requireActivity().getSharedPreferences("Settings", 0)
        timeInSeconds = settings.getLong("Time", 15)

        currentIndex = 0
        isStarted = false
        words = loadWords()
        fragmentView = view

        timerLabel = view.findViewById(R.id.timerLabel)
        timerLabel.text = timeInSeconds.toString()

        view.findViewById<Button>(R.id.restartBtn).setOnClickListener {
            restart()
        }

        mainInput = view.findViewById(R.id.etMain)
        wordList = view.findViewById(R.id.wordList)
        wordList.setOnClickListener {
            mainInput.requestFocus()
            val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(mainInput, InputMethodManager.SHOW_IMPLICIT)
        }
        mainInput.addTextChangedListener {
            val txt = mainInput.text.toString()

            if (!isStarted && txt.isNotEmpty()) {
                isStarted = true
                start()
            }

            val currentWordData = currentWords[currentIndex]

            if (currentWordData.word.startsWith(txt)) {
                currentWordData.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.transparent))
            } else {
                currentWordData.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
            }

            if (txt.endsWith(' ')) {
                val typedWord = txt.substring(0, txt.length - 1)
                if (typedWord == currentWordData.word) {
                    totalCharacters += txt.length
                    val newWordData = currentWords[++currentIndex]
                    newWordData.view.setTextColor(ContextCompat.getColor(view.context, R.color.highlighted_text))
                    currentWordData.view.setTextColor(ContextCompat.getColor(view.context, R.color.purple_700))
                    currentWordData.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.transparent))
                    mainInput.text.clear()

                    if (newWordData.index != currentWordData.index && newWordData.index == 2) {
                        shiftWords()
                    }
                }
            }
        }

        initText()
    }

    private fun getWPM() = ceil((totalCharacters / 5f) / (timeInSeconds / 60f)).toInt()

    private fun restart() {
        currentIndex = 0
        mainInput.text.clear()
        currentWords.clear()
        timerLabel.text = timeInSeconds.toString()
        mainInput.isEnabled = true
        isStarted = false

        initText()
    }

    private fun initText() {
        wordList.removeAllViews()
        for (i in 0 until TOTAL_LINES) {
            addContainer(i)
        }
        currentWords[0].view.setTextColor(ContextCompat.getColor(fragmentView.context, R.color.highlighted_text))
    }

    private fun start() {
        mainInput.isEnabled = true
        totalCharacters = 0
        val newId = Random.nextDouble().toString()
        sessionID = newId

        object : CountDownTimer(timeInSeconds * 1_000, 1_000) {
            override fun onTick(p0: Long) {
                if (newId == sessionID && isStarted) {
                    val secondsRemaining = (p0 + 1000) / 1000
                    timerLabel.text = secondsRemaining.toString()
                }
            }

            override fun onFinish() {
                if (newId == sessionID && isStarted) {
                    val wpm = getWPM()
                    timerLabel.text = "${wpm} WPM"
                    mainInput.text.clear()
                    mainInput.isEnabled = false

                    val prefs = requireActivity().getSharedPreferences("Settings", 0)
                    val scores = prefs.getString("Scores", "50,75,50")
                    prefs.edit {
                        putString("Scores", "$scores,$wpm")
                        apply()
                    }

                }
            }
        }.start()
    }

    private fun addRow(container: LinearLayout, index: Int) {
        val row = getWordRow()
        row.forEach {
            val wordLabel = TextView(fragmentView.context)
            wordLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            wordLabel.text = it
            wordLabel.typeface = ResourcesCompat.getFont(fragmentView.context, R.font.roboto_mono)
            container.addView(wordLabel)
            val params = wordLabel.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(10, 0, 10, 0)
            wordLabel.layoutParams = params

            currentWords.add(WordData(it, wordLabel, index))
        }
    }

    private fun clearWords() {
        currentWords.forEach {
            (it.view.parent as ViewGroup).removeView(it.view)
        }
        currentWords.clear()
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

        addContainer(2)
    }

    private fun addContainer(index: Int) {
        try {
            val newRowContainer = LinearLayout(fragmentView.context)
            newRowContainer.orientation = LinearLayout.HORIZONTAL
            wordList.addView(newRowContainer)
            addRow(newRowContainer, index)
        } catch (e: Throwable) {
            println("Error: $e")
        }
    }

    private fun loadWords(): ArrayList<String> {
        val result = arrayListOf<String>()

        activity?.application?.assets?.open(WORD_FILE_NAME)?.bufferedReader()?.forEachLine {
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
