/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author sanh
 */
@Data
public class Command {
    
    @JsonProperty("command")
    private String command;
    @JsonProperty("expectationPattern")
    private String expectationPattern;
}
