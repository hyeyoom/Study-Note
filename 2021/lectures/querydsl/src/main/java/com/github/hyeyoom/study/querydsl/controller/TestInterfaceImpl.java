package com.github.hyeyoom.study.querydsl.controller;

import java.util.List;

public class TestInterfaceImpl implements TestInterface {

    @Override
    public List<String> test() {
        return List.of("abcdafdadf");
    }
}
