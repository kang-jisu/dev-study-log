package com.dev.studylog.httpRequest;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StringToListConverter implements Converter<String, List<?>> {
    @Override
    public List<?> convert(String source) {

        List<?> list = Arrays.asList(source.split(",")).stream()
                .filter(sn -> sn != null && !sn.equals(""))
                .collect(Collectors.toList());

        return list.size()==0 ? null : list;

    }
}
