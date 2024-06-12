package com.bmstu.han.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Production {
    @JsonProperty("lhs")
    public String left;

    @JsonProperty("rhs")
    public ArrayList<Symbol> right;

    public static Production create(String left, ArrayList<Symbol> right) {
        Production production = new Production();
        production.left = left;
        production.right = right;
        return production;
    }


}
