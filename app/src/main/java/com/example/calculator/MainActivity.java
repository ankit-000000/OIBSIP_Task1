package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvCalculation;
    private TextView tvResult;

    private StringBuilder currentInput;
    private double result = 0;
    private char lastOperator = ' ';
    private boolean hasResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCalculation = findViewById(R.id.tv_calculation);
        tvResult = findViewById(R.id.tv_result);
        currentInput = new StringBuilder();

        setupNumberButtons();
        setupOperatorButtons();
    }

    private void setupNumberButtons() {
        int[] numberButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_point
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    private void setupOperatorButtons() {
        int[] operatorButtonIds = {
                R.id.btn_Addition, R.id.btn_Subtraction,
                R.id.btn_Multiplication, R.id.btn_division,
                R.id.btn_Remainder
        };

        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(this);
        }

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_AllClear).setOnClickListener(this);
        findViewById(R.id.btn_equals).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        if (id == R.id.btn_equals) {
            calculateResult();
        } else if (id == R.id.btn_back) {
            handleBackspace();
        } else if (id == R.id.btn_clear) {
            clearCurrentInput();
        } else if (id == R.id.btn_AllClear) {
            clearAll();
        } else if (isNumeric(buttonText) || buttonText.equals(".")) {
            handleNumericInput(buttonText);
        } else if (isOperator(buttonText)) {
            handleOperatorInput(buttonText);
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean isOperator(String str) {
        return str.matches("[+\\-x/%]");
    }

    private void handleNumericInput(String input) {
        if (hasResult) {
            currentInput.setLength(0);
            hasResult = false;
        }
        currentInput.append(input);
        tvCalculation.setText(currentInput.toString());
    }

    private void handleOperatorInput(String operator) {
        if (currentInput.length() > 0) {
            lastOperator = operator.charAt(0);
            currentInput.append(" ").append(lastOperator).append(" ");
        } else if (result != 0) {
            lastOperator = operator.charAt(0);
            currentInput.append(result).append(" ").append(lastOperator).append(" ");
        }
        tvCalculation.setText(currentInput.toString());
    }

    private void handleBackspace() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            tvCalculation.setText(currentInput.toString());
        }
    }

    private void clearCurrentInput() {
        currentInput.setLength(0);
        tvCalculation.setText("");
    }

    private void clearAll() {
        currentInput.setLength(0);
        result = 0;
        lastOperator = ' ';
        hasResult = false;
        tvCalculation.setText("");
        tvResult.setVisibility(View.GONE);
    }

    private void calculateResult() {
        if (currentInput.length() == 0) {
            return;
        }

        // Append the last number if the input does not end with an operator
        if (!isOperator(String.valueOf(currentInput.charAt(currentInput.length() - 1)))) {
            currentInput.append(" ");
        }

        String[] inputParts = currentInput.toString().split(" ");
        if (inputParts.length < 3) {
            return;
        }

        double leftOperand = Double.parseDouble(inputParts[0]);
        char operator = inputParts[1].charAt(0);
        double rightOperand = Double.parseDouble(inputParts[2]);

        switch (operator) {
            case '+':
                result = leftOperand + rightOperand;
                break;
            case '-':
                result = leftOperand - rightOperand;
                break;
            case 'x':
                result = leftOperand * rightOperand;
                break;
            case '/':
                if (rightOperand != 0) {
                    result = leftOperand / rightOperand;
                } else {
                    tvResult.setText("Error");
                    tvResult.setVisibility(View.VISIBLE);
                    hasResult = true;
                    return;
                }
                break;
            case '%':
                result = leftOperand % rightOperand;
                break;
        }

        DecimalFormat df = new DecimalFormat("#.##########");
        tvResult.setText(df.format(result));
        tvResult.setVisibility(View.VISIBLE);
        hasResult = true;
    }
}
