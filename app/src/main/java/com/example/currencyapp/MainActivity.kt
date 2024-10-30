package com.example.currencyapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var editTextAmountFrom: EditText
    private lateinit var editTextAmountTo: EditText


    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "VND" to 24000.0,
        "EUR" to 0.85,
        "JPY" to 110.0
    )

    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        editTextAmountFrom = findViewById(R.id.editTextAmountFrom)
        editTextAmountTo = findViewById(R.id.editTextAmountTo)

        val currencies = exchangeRates.keys.toList()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter


        editTextAmountFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) {
                    isUpdating = true
                    convertCurrency(editTextAmountFrom, editTextAmountTo, true)
                    isUpdating = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editTextAmountTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) {
                    isUpdating = true
                    convertCurrency(editTextAmountTo, editTextAmountFrom, false)
                    isUpdating = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun convertCurrency(fromEditText: EditText, toEditText: EditText, isFromAmount: Boolean) {
        val amountText = fromEditText.text.toString()
        if (amountText.isEmpty()) {
            toEditText.text.clear()
            return
        }

        val amount = amountText.toDouble()
        val fromCurrency = if (isFromAmount) spinnerFrom.selectedItem.toString() else spinnerTo.selectedItem.toString()
        val toCurrency = if (isFromAmount) spinnerTo.selectedItem.toString() else spinnerFrom.selectedItem.toString()

        val fromRate = exchangeRates[fromCurrency] ?: return
        val toRate = exchangeRates[toCurrency] ?: return

        val result = amount * (toRate / fromRate)
        toEditText.setText(String.format("%.2f", result))
    }
}
