/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.converter;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
/**
 *
 * @author polzovatel
 */
public class StringToDate implements Converter<String, LocalDate> {

    public LocalDate convert(String s) {
        return ISODateTimeFormat.localDateParser().parseLocalDate(s);
    }
} 