package com.tawny.android;

public abstract class Person {
    String name;
    int age;
    static String teacher;

    public Person(){
        System.out.println("构造方法" + getClass() + ".." + hashCode());
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
