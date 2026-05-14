package com.example.calculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView display;

    String currentInput = "";
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
    }

    private void setupNumberButton(int id, String number) {

        Button button = findViewById(id);

        button.setOnClickListener(v -> {

            if (errorState) {
                return;
            }

            // Start new calculation after =
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

        previousResult = performOperation(
                previousResult,
                currentNumber,
                currentOperator
        );

        if (errorState) {
            return;
        }

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

    private void clearAll() {

        currentInput = "";
        previousResult = 0;
        currentOperator = "";
        afterEquals = false;
        errorState = false;

        display.setText("0");
    }
}