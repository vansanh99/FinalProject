/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author vansa
 */
@Data
public class Chapter {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("categories")
    private ArrayList<Category> categories;
}
