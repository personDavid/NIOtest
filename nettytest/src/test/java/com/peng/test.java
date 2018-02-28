package com.peng;

import java.util.Properties;

public class test {
    public static void main(String[] args) {
        String ssl = System.getProperty("port");
        System.out.println(ssl);
        String port = System.getProperty("port", "8009");
        String ssl1 = System.getProperty("port");
        System.out.println(ssl1);
        System.out.println(port);
        Properties properties = System.getProperties();
        String s = properties.toString();
        String[] split = s.split(",");
        for (String ss:split
             ) {
            System.out.println(ss);
        }
    }}
