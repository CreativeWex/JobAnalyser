package com.bereznev.dto;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SkillsDto extends Dto {
    private List<String> skills;
}
