package com.bmstu.han.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Accessors(chain = true)
@Data
public class Grammar {
    @JsonProperty("grammar")
    public GrammarInformation grammarInformation;

    public void print() {
        printName();
        printStartSymbol();
        printNonTerminal();
        printTerminal();
        printProduction();
        log.info("**************************\n");
    }

    private void printName() {
        log.info("Name: {}", grammarInformation.name);
    }

    private void printStartSymbol() {
        log.info("Start symbol: {}", grammarInformation.startSymbol);
    }

    private void printNonTerminal() {
        log.info("Non-terminal symbols: {}", String.join(", ", grammarInformation.nonTerminalSymbols));
    }

    private void printTerminal() {
        ArrayList<String> termList = new ArrayList<>();
        for (Symbol elem : grammarInformation.terminalSymbols)
            termList.add(elem.spell);
        log.info("Terminal symbols: {}", String.join(", ", termList));
    }

    private void printProduction() {
        log.info("Productions:");
        HashMap<String, ArrayList<String>> resRules = new HashMap<>();
        for (Production production : grammarInformation.productions) {
            if (!resRules.containsKey(production.left))
                resRules.put(production.left, new ArrayList<>());

            StringBuilder ruleStr = new StringBuilder();
            for (Symbol elem : production.right)
                ruleStr.append(elem.name);

            resRules.get(production.left).add(ruleStr.toString());
        }

        for (Map.Entry<String, ArrayList<String>> set : resRules.entrySet())
            log.info("{} -> {}", set.getKey(), String.join(" | ", resRules.get(set.getKey())));
    }

    public boolean isEqual(Grammar o) {
        GrammarInformation cmpGrammar = o.getGrammarInformation();

        return grammarInformation.isEqualName(cmpGrammar.getName()) &&
                grammarInformation.isEqualStartSymbol(cmpGrammar.getStartSymbol()) &&
                grammarInformation.isEqualTerminalSymbols(cmpGrammar.getTerminalSymbols()) &&
                grammarInformation.isEqualNonTerminalSymbols(cmpGrammar.getNonTerminalSymbols()) &&
                grammarInformation.isEqualProductions(cmpGrammar.getProductions());
    }
}
