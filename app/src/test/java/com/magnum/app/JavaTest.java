package com.magnum.app;

import androidx.arch.core.util.Function;

import org.junit.Test;

import java.util.function.BiFunction;

public class JavaTest {

    @Test
    public void dosome() {
        System.out.println("Doodle's WOrld");
    }

    @Test
    public void func() {
        Function f = input -> null;

        Function<String, String> fdd = (String fd) -> "";

        BiFunction<String, String, String> bifunc = (String first, String second) -> "s";
    }

}
