package com.bmstu.han;

import com.bmstu.han.controller.GrammarController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KkLab2Application {

    public static void main(String[] args) {

//        GrammarController.handlingLeftRecursion("src/main/resources/han1.json");


        GrammarController.handlingUselessSymbols("src/main/resources/han5.json");


//-----------------------------------------------------------------------------------------------
//        GrammarController.handlingUselessSymbols("src/main/resources/han6.json");


//        GrammarController.handlingUselessSymbols("src/main/resources/han4.json");

//        GrammarController.handlingUselessSymbols("src/main/resources/han3.json");

//        GrammarController.handlingLeftRecursion("src/main/resources/han2.json");


    }

}

