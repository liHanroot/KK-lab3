package com.bmstu.han.controller;

import com.bmstu.han.pojo.Grammar;
import com.bmstu.han.pojo.GrammarInformation;
import com.bmstu.han.pojo.Production;
import com.bmstu.han.pojo.Symbol;
import com.bmstu.han.service.EmptyLanguageService;
import com.bmstu.han.service.RecursionService;
import com.bmstu.han.service.RecursiveDescentService;
import com.bmstu.han.service.UnreachableSymbolsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

@Slf4j
public class GrammarController {

    //处理-删除左递归(目前只支持删除直接左递归)
    public static void handlingLeftRecursion(String filenameJson) {
        Grammar grammar = null;

        try {
            grammar = readGrammarFromFile(filenameJson);
        } catch (IOException e) {
            log.error("Ошибка чтения файла(文件读取错误): {}", e.getMessage());
        }

        log.info("Чтение синтаксиса файла JSON(读取 JSON 文件语法)");

        grammar.print();

        try {
            deleteLeftRecursion(grammar.grammarInformation);
        } catch (Exception e) {
            log.error("Удалить ошибку левой рекурсии(消除左递归错误): {}", e.getMessage());
        }

        log.info("Удалить левую рекурсию(消除左递归)");

        grammar.print();

        RecursiveDescentService.validationExpression();

    }

    //处理-删除无用符号
    public static Grammar handlingUselessSymbols(String filenameJson) {
        Grammar grammar;

        try {
            grammar = readGrammarFromFile(filenameJson);
        } catch (IOException e) {
            log.error("Ошибка чтения файла(文件读取错误): {}", e.getMessage());
            return null;
        }
        log.info("Чтение синтаксиса файла JSON(读取 JSON 文件语法)");

        grammar.print();

        return deleteUselessSymbols(grammar);
    }

    //删除无用符号
    private static Grammar deleteUselessSymbols(Grammar grammar) {
        HashSet<String> ne = isEmptyLanguage(grammar.grammarInformation);
        if (ne == null) {
            log.error("Empty language");
            return null;
        }
//        log.info("ne: {}", String.join(", ", ne));


        updateGrammar(ne, grammar.grammarInformation);
        log.info("1.Удалить негенерируемые символы(删除非产生符号)");

        grammar.print();

        deleteUnreachableSymbols(grammar.grammarInformation);

        log.info("2.Удалить недоступные символы(删除不可达符号)");

        grammar.print();

        //writeGrammarToFile(grammar);
        return grammar;
    }

    //从JSON文件中读取语法
    public static Grammar readGrammarFromFile(String filenameJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filenameJson), Grammar.class);
    }

    //将处理好的语法写成文件
    private static void writeGrammarToFile(Grammar grammar) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/resources/test_res2.json"), grammar);
        } catch (Exception e) {
            log.error("Ошибка записи файла(写入文件错误): {}", e.getMessage());
        }
    }

    //删除左递归
    private static void deleteLeftRecursion(GrammarInformation grammarInformation) throws Exception {
        new RecursionService().deleteLeftRecursion(grammarInformation);
    }


    private static HashSet<String> isEmptyLanguage(GrammarInformation grammarInformation) {
        return new EmptyLanguageService().isEmpty(grammarInformation);
    }


    //删除非产生符号
    private static void updateGrammar(HashSet<String> ne, GrammarInformation grammarInformation) {
        ArrayList<String> neList = new ArrayList<>(ne);
        grammarInformation.nonTerminalSymbols.retainAll(neList);

        ArrayList<Production> productionList = new ArrayList<>();
        for (Production production : grammarInformation.productions) {
            if (!grammarInformation.isTerminalElement(production.left) && !ne.contains(production.left))
                continue;

            int cnt = 0;
            for (Symbol elemRight : production.right) {
                if (grammarInformation.isTerminalSymbol(elemRight) ||
                        ne.contains(elemRight.name) ||
                        Objects.equals(elemRight.name, "ε"))
                    cnt++;
            }

            if (cnt == production.right.size())
                productionList.add(production);
        }

        grammarInformation.productions = productionList;
    }

    //删除无用符号
    private static void deleteUnreachableSymbols(GrammarInformation grammarInformation) {
        new UnreachableSymbolsService().delete(grammarInformation);
    }
}
