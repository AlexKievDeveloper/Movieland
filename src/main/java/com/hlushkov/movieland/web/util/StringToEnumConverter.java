package com.hlushkov.movieland.web.util;

import com.hlushkov.movieland.entity.SortDirection;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, SortDirection> {
    @Override
    public SortDirection convert(String directionParameterValue) {
        return SortDirection.valueOf(directionParameterValue.toUpperCase());
    }
}
