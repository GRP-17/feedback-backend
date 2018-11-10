package org.seventeen.calculator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sum {
    private double num1;
    private double num2;
    private double sum;

    public Sum(double num1, double num2) {
        this.num1 = num1;
        this.num2 = num2;
        this.sum = this.calculate(num1, num2);
    }

    public double calculate(double num1, double num2) {
        return num1 + num2;
    }
}
