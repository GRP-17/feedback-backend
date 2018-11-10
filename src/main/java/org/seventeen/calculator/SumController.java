package org.seventeen.calculator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class SumController {

    private static final String template = "The sum of %f and %f is %f.";

    @RequestMapping("/sum")
    @ResponseBody
    public Sum sum(@RequestParam(value="num1", defaultValue="0w") double num1,
                   @RequestParam(value="num2", defaultValue="0") double num2) {
//        double sum = new Sum(num1, num2).getSum();

//        return String.format(template, num1, num2, sum);
        return new Sum(num1, num2);
    }
}