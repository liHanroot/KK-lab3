package com.bmstu.han.service;

import java.util.ArrayList;
import java.util.Scanner;


public class RecursiveDescentService {
    static int n = 0;
    static ArrayList<String> data = new ArrayList<String>();

    public static void validationExpression(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите любое выражение для анализа(请输入任意表达式进行分析) i# i*i# i)i# i+(i-i*i)+i/i# i+*i#:");
        String str = input.nextLine();
        str += " #";
        str = str.replace("(", " ( ");
        str = str.replace(")", " ) ");
        str = str.replace("+", " + ");
        str = str.replace("-", " - ");
        str = str.replace("*", " * ");
        str = str.replace("/", " / ");
        str = str.replace("#", " # ");
        String s = "";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) != ' '){
                s += str.charAt(i);
            }else{
                data.add(s);
                s="";
                continue;
            }
        }
        deleteBlack(data);
        for(int i=0;i<data.size();i++)
            if(num(data.get(i)))
                data.set(i, "i");
        int z = 0;
        int y = 0;
        for(int i=0;i<data.size();i++){
            if(data.get(i).equals("("))
                z++;
            else if(data.get(i).equals(")"))
                y++;
        }
        if(!(z==y)){
            System.out.println("Левая и правая скобки выражения не совпадают, проверьте выражение.(表达式左右括号不匹配,请检查表达式)");
            System.exit(0);
        }
        E();
        System.out.println("Грамматически правильно(语法正确)");
    }


    private static boolean num(String string) {
        for(int i=0;i<string.length();i++){
            if(string.charAt(i)>='0'&&string.charAt(i)<='9'||string.charAt(i)=='。'||string.charAt(i)=='.')
                continue;
            else{
                return false;
            }
        }
        return true;
    }

    //递归分析函数实现(Реализация функции рекурсивного анализа)
    private static void E() {
        if(data.get(n).equals("(")||data.get(n).equals("i")){
            T();
            Ep();
        }else{
            System.out.println("第"+(n+1)+"个元素处有误");
            System.out.println("error:E→TE'");
            System.exit(0);
        }
    }


    private static void Ep() {
        if(data.get(n).equals("+")){
            match("+");
            T();
            Ep();
        }else if(data.get(n).equals("-")){
            match("-");
            T();
            Ep();
        }else if(data.get(n).equals(")")||data.get(n).equals("#")){
        }else{
            System.out.println((n+1)+" ошибки в элементах");
            System.out.println("error:E'→+TE'|-TE'|ε");
            System.exit(0);
        }
    }


    private static void T() {
        if(data.get(n).equals("(")||data.get(n).equals("i")){
            F();
            Tp();
        }else{
            System.out.println((n+1)+" ошибки в элементах");
            System.out.println("error:T→FT'");
            System.exit(0);
        }
    }


    private static void Tp() {
        if(data.get(n).equals("*")){
            match("*");
            F();
            Tp();
        }else if(data.get(n).equals("/")){
            match("/");
            F();
            Tp();
        }else if(data.get(n).equals("+")||data.get(n).equals("-")||data.get(n).equals(")")||data.get(n).equals("#")){
        }else{
            System.out.println((n+1)+" ошибки в элементах");
            System.out.println("error:T'→*FT'|/FT'|ε");
            System.exit(0);
        }
    }


    private static void F() {
        if(data.get(n).equals("(")){
            match("(");
            E();
            match(")");
        }else if(data.get(n).equals("i")){
            match("i");
        }else{
            System.out.println((n+1)+" ошибки в элементах");
            System.out.println("error:F→i|(E)");
            System.exit(0);
        }
    }


    private static void match(String string) {
        if(data.get(n).equals(string)){
            n++;
        }else{
            System.out.println((n+1)+" ошибки в элементах");
            System.out.println("miss:\""+string+"\"");
            System.exit(0);
        }
    }


    private static void deleteBlack(ArrayList<String> data) {
        while(data.contains("")){
            int n = data.indexOf("");
            data.remove(n);
        }
    }
}

