package com.tawny.android;


public class MyClass {
    public static void main(String[] args) {
        Student 张三 = new Student();
        Student 王二 = new Student();
        张三.name = "张三";
        张三.age = 18;
        Student.teacher = "老王";
        System.out.println(张三.toString());
        System.out.printf(王二.toString());
    }
}
