package com.bereznev.mapper;
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
public class AreaMapper {
    private Long id;
    private String name;
    private List<AreaMapper> areas;
}
