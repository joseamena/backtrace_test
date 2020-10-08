package com.joseantoniomena.simplecalculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import backtraceio.library.BacktraceClient
import backtraceio.library.BacktraceCredentials
import backtraceio.library.BacktraceDatabase
import backtraceio.library.enums.database.RetryBehavior
import backtraceio.library.enums.database.RetryOrder
import backtraceio.library.logger.BacktraceLogger
import backtraceio.library.logger.LogLevel
import backtraceio.library.models.BacktraceExceptionHandler
import backtraceio.library.models.database.BacktraceDatabaseSettings
import backtraceio.library.models.json.BacktraceReport
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*

enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

class MainActivity : AppCompatActivity() {

    private val stack = ArrayDeque<Int>()
    private val TAG = "Main"

    private lateinit var backtraceClient: BacktraceClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val credentials = BacktraceCredentials(
            "https://haciendalasnubes.sp.backtrace.io/",
            "9956259f9866412932971165481633b26cf6a5c93ea1cc731d21f93897fb44f3")

        val context = applicationContext
        val dbPath = context.filesDir.absolutePath

        val settings = BacktraceDatabaseSettings(dbPath)
        settings.maxRecordCount = 100
        settings.maxDatabaseSize = 1000
        settings.retryBehavior = RetryBehavior.ByInterval
        settings.isAutoSendMode = true
        settings.retryOrder = RetryOrder.Queue

        val database = BacktraceDatabase(context, settings)
        backtraceClient = BacktraceClient(context, credentials, database)

        BacktraceExceptionHandler.enable(backtraceClient)
        backtraceClient.send("test")

        BacktraceLogger.setLevel(LogLevel.DEBUG);
        database.setupNativeIntegration(backtraceClient, credentials);

        button1.setOnClickListener {
            addNumberToDisplay("1")
        }

        button2.setOnClickListener {
            addNumberToDisplay("2")
        }

        button3.setOnClickListener {
            addNumberToDisplay("3")
        }

        button4.setOnClickListener {
            addNumberToDisplay("4")
        }

        button5.setOnClickListener {
            addNumberToDisplay("5")
        }

        button6.setOnClickListener {
            addNumberToDisplay("6")
        }

        button7.setOnClickListener {
            addNumberToDisplay("7")
        }

        button8.setOnClickListener {
            addNumberToDisplay("8")
        }

        button9.setOnClickListener {
            addNumberToDisplay("9")
        }

        button0.setOnClickListener {
            addNumberToDisplay("0")
        }

        buttonEnter.setOnClickListener {
            processNumber(textView.text.toString().toInt())
            textView.text = ""
        }

        buttonPlus.setOnClickListener {
            performOperation(Operation.ADD)
        }

        buttonMinus.setOnClickListener {
            performOperation(Operation.SUBTRACT)
        }

        buttonMultiply.setOnClickListener {
            performOperation(Operation.MULTIPLY)
        }

        buttonDivide.setOnClickListener {
            performOperation(Operation.DIVIDE)
        }
    }

    private fun addNumberToDisplay(number: String) {
        textView.text = textView.text.toString() + number
    }

    private fun processNumber(number: Int) {
        stack.push(number)
    }

    private fun performOperation(operation: Operation) {
        val num2 = stack.peek()

//        try {
            stack.pop()
//        } catch (e: Exception) {
//            backtraceClient.send(BacktraceReport(e));
//            textView.text = "Error"
//            return
//        }

        val num1 = stack.peek()

//        try {
            stack.pop()
//        } catch (e: Exception) {
//            backtraceClient.send(BacktraceReport(e));
//            textView.text = "Error"
//            return
//        }

        try {
            val result = when (operation) {
                Operation.ADD -> add(num1, num2)
                Operation.SUBTRACT -> subtract(num1, num2)
                Operation.MULTIPLY -> multiply(num1, num2)
                Operation.DIVIDE -> divide(num1, num2)
            }
            textView.text = result.toString()
        } catch (e: ArithmeticException) {
            Log.v(TAG, "sending report: $e")
            backtraceClient.send(BacktraceReport(e));
            textView.text = "Error"
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun add(x: Int, y: Int): Int
    external fun subtract(x: Int, y: Int): Int
    external fun multiply(x: Int, y: Int): Int
    external fun divide(x: Int, y: Int): Int

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
