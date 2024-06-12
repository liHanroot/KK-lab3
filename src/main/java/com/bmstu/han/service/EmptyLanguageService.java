package com.bmstu.han.service;

import com.bmstu.han.pojo.GrammarInformation;
import com.bmstu.han.pojo.Production;
import com.bmstu.han.pojo.Symbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class EmptyLanguageService {
    public HashSet<String> isEmpty(GrammarInformation grammarInformation) {
        HashSet<String> cPrev = new HashSet<>();
        HashSet<String> cCurr = new HashSet<>();

        while (true) {
            for (Production production : grammarInformation.productions) {
                if (isFitCondition(production.right, cPrev, grammarInformation))
                    cCurr.add(production.left);
            }

            cCurr.addAll(cPrev);

            if (cPrev.equals(cCurr))
                break;

            cPrev = new HashSet<>(cCurr);
            cCurr.clear();
        }

        return (cCurr.contains(grammarInformation.startSymbol)) ? cCurr : null;
    }

    private boolean isFitCondition(ArrayList<Symbol> ruleRight, HashSet<String> cPrev, GrammarInformation grammarInformation) {
        for (Symbol elem : ruleRight) {
            if (grammarInformation.isTerminalSymbol(elem) || cPrev.contains(elem.name) || Objects.equals(elem.name, "ε"))
                continue;
            return false;
        }

        return true;
    }
}





