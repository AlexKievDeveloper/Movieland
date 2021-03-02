package com.hlushkov.movieland.web.util;

import com.hlushkov.movieland.common.SortDirection;
import org.springframework.core.convert.converter.Converter;

public class SortDirectionRequestParameterConverter implements Converter<String, SortDirection> {

    @Override
    public SortDirection convert(String directionParameterValue) {
        return SortDirection.getSortDirection(directionParameterValue);
    }

}
