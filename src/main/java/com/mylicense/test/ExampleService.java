package com.mylicense.test;

public class ExampleService {

    private ExampleServiceProperties properties;

    public ExampleService(ExampleServiceProperties properties) {
        this.properties = properties;
    }

    public String wrap(String word) {
        return properties.getPrefix() + word + properties.getSuffix();
    }
}
