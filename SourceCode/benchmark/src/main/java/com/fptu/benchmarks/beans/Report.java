/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author vansa
 */
@Data
public class Report {
    @JsonProperty("id")
    private String id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("permission")
    private String permission;
    @JsonProperty("pattern")
    private String pattern;
    @JsonProperty("path")
    private String path;
    @JsonProperty("type")
    private String type;
    @JsonProperty("command")
    private String command;
    @JsonProperty("expectationPattern")
    private String expectationPattern;
    @JsonProperty("required")
    private boolean required; 
    @JsonProperty("status")
    private boolean status;
}
