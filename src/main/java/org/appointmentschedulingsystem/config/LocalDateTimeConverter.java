package org.appointmentschedulingsystem.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.ValueConversionContext;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mapping.PersistentProperty;

import java.time.LocalTime;

@ReadingConverter
@WritingConverter
public class LocalDateTimeConverter implements PropertyValueConverter
        <LocalTime, String, ValueConversionContext<? extends PersistentProperty<?>>> {
    @Override
    public LocalTime read(@NotNull String value, @NotNull ValueConversionContext context) {
        return LocalTime.parse(value);
    }

    @Override
    public String write(LocalTime value, @NotNull ValueConversionContext context) {
        return value.toString();
    }
}