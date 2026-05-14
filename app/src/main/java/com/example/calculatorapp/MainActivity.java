package com.example.calculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView display;

    String currentInput = "";
    String history = "";
    double previousResult = 0;
    String currentOperator = "";
    boolean afterEquals = false;
    boolean errorState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        // Number buttons
        setupNumberButton(R.id.btn0, "0");
        setupNumberButton(R.id.btn1, "1");
        setupNumberButton(R.id.btn2, "2");
        setupNumberButton(R.id.btn3, "3");
        setupNumberButton(R.id.btn4, "4");
        setupNumberButton(R.id.btn5, "5");
        setupNumberButton(R.id.btn6, "6");
        setupNumberButton(R.id.btn7, "7");
        setupNumberButton(R.id.btn8, "8");
        setupNumberButton(R.id.btn9, "9");

        // Operators
        setupOperatorButton(R.id.btnPlus, "+");
        setupOperatorButton(R.id.btnMinus, "-");
        setupOperatorButton(R.id.btnMultiply, "*");
        setupOperatorButton(R.id.btnDivide, "/");
        setupOperatorButton(R.id.btnPower, "^");

        // Equals
        Button btnEquals = findViewById(R.id.btnEquals);
        btnEquals.setOnClickListener(v -> calculateResult());

        // Clear
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> clearAll());

        // Base conversion
        Button btnBinary = findViewById(R.id.btnBinary);
        Button btnOctal = findViewById(R.id.btnOctal);
        Button btnHex = findViewById(R.id.btnHex);

        btnBinary.setOnClickListener(v -> convertBase(2));
        btnOctal.setOnClickListener(v -> convertBase(8));
        btnHex.setOnClickListener(v -> convertBase(16));
        Button btnHistory = findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

            intent.putExtra("history", history);

            startActivity(intent);
        });
    }

    private void setupNumberButton(int id, String number) {

        Button button = findViewById(id);

        button.setOnClickListener(v -> {

            if (errorState) {
                return;
            }

            if (afterEquals) {
                currentInput = "";
                previousResult = 0;
                currentOperator = "";
                afterEquals = false;
            }

            currentInput += number;
            display.setText(currentInput);
        });
    }

    private void setupOperatorButton(int id, String operator) {

        Button button = findViewById(id);

        button.setOnClickListener(v -> {

            if (errorState) {
                return;
            }

            if (currentInput.isEmpty()) {
                return;
            }

            double currentNumber = Double.parseDouble(currentInput);

            if (currentOperator.isEmpty()) {
                previousResult = currentNumber;
            } else {

                previousResult = performOperation(
                        previousResult,
                        currentNumber,
                        currentOperator
                );

                if (errorState) {
                    return;
                }

                display.setText(String.valueOf(previousResult));
            }

            currentOperator = operator;
            currentInput = "";
            afterEquals = false;
        });
    }

    private void calculateResult() {

        if (errorState) {
            return;
        }

        if (currentInput.isEmpty() || currentOperator.isEmpty()) {
            return;
        }

        double currentNumber = Double.parseDouble(currentInput);

        double firstNumber = previousResult;

        previousResult = performOperation(
                previousResult,
                currentNumber,
                currentOperator
        );

        if (errorState) {
            return;
        }

        history += firstNumber + " "
                + currentOperator + " "
                + currentNumber + " = "
                + previousResult + "\n";

        display.setText(String.valueOf(previousResult));

        currentInput = "";
        currentOperator = "";
        afterEquals = true;
    }

    private double performOperation(double a, double b, String operator) {

        switch (operator) {

            case "+":
                return a + b;

            case "-":
                return a - b;

            case "*":
                return a * b;

            case "/":

                if (b == 0) {
                    display.setText("Error");
                    errorState = true;
                    return 0;
                }

                return a / b;

            case "^":
                return Math.pow(a, b);
        }

        return b;
    }

    private void convertBase(int base) {

        if (errorState) {
            return;
        }

        try {

            int number;

            if (!currentInput.isEmpty()) {
                number = Integer.parseInt(currentInput);
            } else {
                number = (int) previousResult;
            }

            String result = "";

            switch (base) {

                case 2:
                    result = Integer.toBinaryString(number);
                    break;

                case 8:
                    result = Integer.toOctalString(number);
                    break;

                case 16:
                    result = Integer.toHexString(number).toUpperCase();
                    break;
            }

            display.setText(result);

        } catch (Exception e) {

            display.setText("Error");
            errorState = true;
        }
    }

    private void clearAll() {

        currentInput = "";
        previousResult = 0;
        currentOperator = "";
        afterEquals = false;
        errorState = false;

        display.setText("0");
    }
}