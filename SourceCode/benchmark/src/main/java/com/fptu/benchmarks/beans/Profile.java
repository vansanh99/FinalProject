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
public class Profile {
    @JsonProperty("family")
    private String family;
    @JsonProperty("OSCheck")
    private OSCheck OSCheck;
    @JsonProperty("templateReport")
    private String templateReport;
    @JsonProperty("description")
    private String description;
    @JsonProperty("audit")
    private Audit audit;
}
