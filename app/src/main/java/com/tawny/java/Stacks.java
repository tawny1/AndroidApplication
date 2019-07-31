package com.tawny.java;

import java.util.Stack;

/**
 * Author: tawny
 * Data：2018/11/8
 */
public class Stacks {

    Stack<Integer> stack1 = new Stack<>();
    Stack<Integer> stack2 = new Stack<>();


    public static void main(String[] arg0) {

    }

    public void push(int node) {

        stack1.push(node);
    }

    public int pop() {

        if (stack1.empty() && stack2.empty()) {
            throw new RuntimeException("Queue is empty!");
        }
        if (stack2.empty()) {
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

}
