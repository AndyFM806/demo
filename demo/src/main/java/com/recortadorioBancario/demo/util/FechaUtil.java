package com.recortadorioBancario.demo.util;


import java.time.LocalDate;

public class FechaUtil {
    public static LocalDate sumarMeses(LocalDate fecha, int meses) {
        return fecha.plusMonths(meses);
    }
}
