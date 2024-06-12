package com.bmstu.han.service;

import com.bmstu.han.pojo.GrammarInformation;
import com.bmstu.han.pojo.Production;
import com.bmstu.han.pojo.Symbol;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class RecursionService {
    public void deleteLeftRecursion(GrammarInformation grammarInformation) throws Exception {
        for (int i = 0; i < grammarInformation.nonTerminalSymbols.size(); i++) {
            String nonTerminalI = grammarInformation.nonTerminalSymbols.get(i);

            for (int j = 0; j < i; j ++) {
                String nonTerminalJ = grammarInformation.nonTerminalSymbols.get(j);

                expandRules(nonTerminalI, nonTerminalJ, grammarInformation.productions);
            }

            deleteRecursion(grammarInformation.productions, nonTerminalI);
        }
    }

    private void expandRules(String leftNonTerminal, String rightNonTerminal, List<Production> productionList) {
        int i = 0;
        while (i < productionList.size()) {
            Production curProduction = productionList.get(i);
            if (curProduction.right.size() == 0) {
                i++;
                continue;
            }

            Symbol firstElemRight = curProduction.right.get(0);

            if (!Objects.equals(curProduction.left, leftNonTerminal) ||
                    !Objects.equals(firstElemRight.name, rightNonTerminal)) {
                i++;
                continue;
            }

            Production updProduction = productionList.remove(i);
            updProduction.right.remove(0);

            int j = 0;
            while (j < productionList.size()) {
                if (!Objects.equals(productionList.get(j).left, rightNonTerminal)) {
                    j++;
                    continue;
                }

                ArrayList<Symbol> addElementList = new ArrayList<>(productionList.get(j).right);
                addElementList.addAll(updProduction.right);

                productionList.add(Production.create(updProduction.left, addElementList));

                j++;
            }

            i++;
        }
    }

    private void deleteRecursion(List<Production> productionList, String nonTerminal) throws Exception {
        ArrayList<ArrayList<Symbol>> alphaArr = new ArrayList<>();
        ArrayList<ArrayList<Symbol>> betaArr = new ArrayList<>();

        for (Production production : productionList) {
            if (!Objects.equals(production.left, nonTerminal))
                continue;

            Symbol firstElemRight = production.right.get(0);
            ArrayList<Symbol> temp = new ArrayList<>(production.right);
            temp.add(new Symbol()
                    .setName(nonTerminal + "'"));
            if (Objects.equals(firstElemRight.name, nonTerminal)) {
                temp.remove(0);
                alphaArr.add(temp);
            }
            else {
                betaArr.add(temp);
            }
        }

        if (alphaArr.size() == 0)
            return;
        if (betaArr.size() == 0) {
            throw new Exception("Недопустимая грамматика");
        }

        updateRules(nonTerminal, productionList, alphaArr, betaArr);
    }

    private void updateRules(String nonTerminal, List<Production> productionList, ArrayList<ArrayList<Symbol>> alphaArr,
                             ArrayList<ArrayList<Symbol>> betaArr) {
        deleteRules(nonTerminal, productionList);
        updateAlphaRules(nonTerminal, productionList, alphaArr);
        updateBetaRules(nonTerminal, productionList, betaArr);
    }

    private void updateAlphaRules(String nonTerminal, List<Production> productionList, ArrayList<ArrayList<Symbol>> alphaArr) {
        String newName = nonTerminal + "'";
        for (ArrayList<Symbol> elem : alphaArr) {
            Production newProduction = Production.create(newName, elem);

            productionList.add(newProduction);
        }

        Production defProduction = Production.create(newName, new ArrayList<>() { {
            add(new Symbol().setName("ε"));
        }});

        productionList.add(defProduction);
    }

    private void updateBetaRules(String nonTerminal, List<Production> productionList, ArrayList<ArrayList<Symbol>> betaArr) {
        for (ArrayList<Symbol> elem : betaArr)
            productionList.add(Production.create(nonTerminal, elem));
    }

    private void deleteRules(String nonTerminal, List<Production> productionList) {
        int i = 0;
        while (i < productionList.size()) {
            if (!Objects.equals(productionList.get(i).left, nonTerminal)) {
                i++;
                continue;
            }

            productionList.remove(productionList.get(i));
        }
    }
}
