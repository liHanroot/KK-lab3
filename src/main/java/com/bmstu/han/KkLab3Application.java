package com.bmstu.han;

import com.bmstu.han.controller.GrammarController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KkLab3Application {

    public static void main(String[] args) {

        GrammarController.handlingLeftRecursion("src/main/resources/han1.json");

    }

}

