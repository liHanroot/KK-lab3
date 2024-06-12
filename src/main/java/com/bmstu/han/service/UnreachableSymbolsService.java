package com.bmstu.han.service;

import com.bmstu.han.pojo.GrammarInformation;
import com.bmstu.han.pojo.Production;
import com.bmstu.han.pojo.Symbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class UnreachableSymbolsService {
    public void delete(GrammarInformation grammarInformation) {
        HashSet<String> vPrev = new HashSet<>();
        HashSet<String> vCurr = new HashSet<>();

        vPrev.add(grammarInformation.startSymbol);

        while (true) {
            for (Production production : grammarInformation.productions) {
                if (!vPrev.contains(production.left))
                    continue;

                for (Symbol elem : production.right)
                    vCurr.add(elem.name);
            }

            vCurr.addAll(vPrev);

            if (vPrev.equals(vCurr))
                break;

            vPrev = new HashSet<>(vCurr);
            vCurr.clear();
        }

        update(grammarInformation, vCurr);
    }

    private void update(GrammarInformation grammarInformation, HashSet<String> v) {
        updateNonTerminalSymbols(grammarInformation, v);
        updateTerminalSymbols(grammarInformation, v);
        updateRules(grammarInformation, v);
    }

    private void updateNonTerminalSymbols(GrammarInformation grammarInformation, HashSet<String> v) {
        grammarInformation.nonTerminalSymbols.retainAll(v);
    }

    private void updateTerminalSymbols(GrammarInformation grammarInformation, HashSet<String> v) {
        ArrayList<Symbol> termList = new ArrayList<>();
        for (Symbol term : grammarInformation.terminalSymbols)
            if (v.contains(term.spell))
                termList.add(term);

        grammarInformation.terminalSymbols = termList;
    }

    private void updateRules(GrammarInformation grammarInformation, HashSet<String> v) {
        ArrayList<Production> productionList = new ArrayList<>();
        for (Production production : grammarInformation.productions) {
            if (!grammarInformation.isTerminalElement(production.left) && !v.contains(production.left))
                continue;

            int cnt = 0;
            for (Symbol elemRight : production.right)
                if (v.contains(elemRight.name) || Objects.equals(elemRight.name, "ε"))
                    cnt++;

            if (cnt == production.right.size())
                productionList.add(production);
        }

        grammarInformation.productions = productionList;
    }
}
