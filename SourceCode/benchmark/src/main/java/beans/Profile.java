/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author vansa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Age")
    private String age;
}
