package com.dev.studylog.httpRequest;

import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class ArrayToListConverter implements Converter<String[] , List<Long>> {
    @Override
    public List<Long> convert(String[] source) {
        if(source == null || source.length ==0 ) return null;
        List<Long> list = new ArrayList<>();
        for(int i=0; i<source.length; i++){
            if(source[i].isEmpty() || source[i]==null) continue;
            list.add(Long.valueOf(source[i]));
        }
        return list.isEmpty() ? null : list;
    }
}
