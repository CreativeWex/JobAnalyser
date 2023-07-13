package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.InputValueFormatter;
import com.bereznev.exceptions.logic.ResourceNotFoundException;
import com.bereznev.mapper.AreaMapper;
import com.bereznev.service.LocationAnalyserService;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class LocationAnalyserServiceImpl implements LocationAnalyserService {
    private long locationId = Integer.MIN_VALUE;
    private void recursiveAreaTraversal(List<AreaMapper> areaList, String locationName) {
        for (AreaMapper area : areaList) {
            if (area.getName().equals(locationName) || area.getName().contains(locationName)) {
                locationId = area.getId();
            }
            if (!area.getAreas().isEmpty()) {
                recursiveAreaTraversal(area.getAreas(), locationName);
            }
        }
    }

    @Override
    public long findHhLocationIdByName(String locationName) {
        Gson gson = new Gson();
        locationName = InputValueFormatter.formatInputValue(locationName);
        String response = HttpUtils.sendHttpRequest("https://api.hh.ru/areas", "findHHLocationIdByName");

        JsonArray areaArray = JsonParser.parseString(response).getAsJsonArray();
        Type listType = new TypeToken<List<AreaMapper>>() {}.getType();
        List<AreaMapper> areas = gson.fromJson(areaArray, listType);

        recursiveAreaTraversal(areas, locationName);
        if (locationId == Integer.MIN_VALUE) {
            throw new ResourceNotFoundException("Area", "location name", locationName);
        }
        return locationId;
    }
}
