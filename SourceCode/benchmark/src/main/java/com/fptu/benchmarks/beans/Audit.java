/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author vansa
 */
@Data
public class Audit {
    @JsonProperty("level_list")
    private List<Level> levelList;
    @JsonProperty("chapters")
    private ArrayList<Chapter> chapters;
    
    
}
