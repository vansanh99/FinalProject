package com.fptu.benchmarks.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OSCheck {
    @JsonProperty("command_list")
    private List<Command> commandList;
    private String operator;
    private boolean status;
}
