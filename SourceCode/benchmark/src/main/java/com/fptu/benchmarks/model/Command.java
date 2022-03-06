/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fptu.benchmarks.model;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author sanh
 */
@Data
@Builder
public class Command {
    String cmd;
    String[] args;
}
