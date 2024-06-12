package com.bmstu.han.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Symbol {
    @JsonProperty("-type")
    public String type;

    @JsonProperty("-spell")
    public String spell;

    @JsonProperty("-name")
    public String name;
}
