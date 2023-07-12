package com.bereznev;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class Dto {
    @JsonProperty("time_spent")
    protected String timeSpent;

    @JsonProperty("filtered_by_vacancy_name")
    protected String nameFilter = null;

    @JsonProperty("filtered_by_location")
    protected String locationFilter = null;
}
