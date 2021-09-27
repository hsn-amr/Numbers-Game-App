package com.example.numbersgameapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: ConstraintLayout
    private lateinit var rvMain: RecyclerView
    private lateinit var userInput: EditText
    private lateinit var guessButton: Button
    private lateinit var guesses: ArrayList<String>
    private val secretNumber = Random.nextInt(11)
    private var doesWin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guesses= ArrayList()
        mainLayout = findViewById(R.id.mainLayout)
        rvMain = findViewById(R.id.rvMain)
        userInput = findViewById(R.id.etUserInput)
        guessButton = findViewById(R.id.btnGuess)

        guessButton.setOnClickListener{
            when{
                userInput.text.isNotEmpty() ->{
                    checkRange()
                    when{
                        userInput.text.toString().toInt() == secretNumber ->{
                            doesWin = true
                            gameOver(doesWin)
                        }
                        guesses.size >= 3 -> gameOver(doesWin)
                        else -> {
                            rvMain.adapter = RecyclerViewAdapter(guesses)
                            rvMain.layoutManager = LinearLayoutManager(this)
                        }
                    }
                }
                else -> {
                    Snackbar.make(mainLayout, "Please, enter a number", Snackbar.LENGTH_SHORT).show()
                }
            }
            userInput.text.clear()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("guesses", guesses)
        outState.putBoolean("doesWin", doesWin)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        guesses = savedInstanceState.getStringArrayList("guesses")!!
        doesWin = savedInstanceState.getBoolean("doesWin")
        rvMain.adapter = RecyclerViewAdapter(guesses)
        rvMain.layoutManager = LinearLayoutManager(this)
    }

    private fun gameOver(doesWin: Boolean){
        var message = if(doesWin) "You Win, it is $secretNumber" else "You Lost, it is $secretNumber"
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(message)
            .setPositiveButton("Restart Game", DialogInterface.OnClickListener{ _, _ -> recreate()})
            .setNegativeButton("Exit", DialogInterface.OnClickListener{ _, _ -> finish()})

        val alart = dialogBuilder.create()
        alart.setTitle("Game Over")
        alart.show()
        guesses.clear()
        this.doesWin = false
    }

    private fun checkRange(){
        when{
            userInput.text.toString().toInt() > 10 || userInput.text.toString().toInt() < 0 ->
                Snackbar.make(mainLayout, "Please, enter a number between 0 and 10", Snackbar.LENGTH_SHORT).show()
            else -> {
                guesses.add(userInput.text.toString())
            }
        }
    }
}