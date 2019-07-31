package com.tawny.java;

import java.util.ArrayList;


public class Student {

    public Student() {
    }

    public static void main(String[] arg) {
//        String str = "    sfjlje werjl weoi";
//        System.out.print(str.replaceAll(" ", "%20"));
//
//        Stacks stack = new Stacks();
//        stack.push("obj");
//        stack.pop();
//
//
        ListNode listNode = new ListNode(23);

        listNode.next = new ListNode(56);
        listNode.next.next = new ListNode(18);
        listNode.next.next.next = new ListNode(90);
        listNode.next.next.next.next = new ListNode(35);
        listNode.next.next.next.next.next = new ListNode(29);

//        System.out.println(listNode);
//        System.out.println(listNode.next);
//        System.out.println(listNode.next.next);
//        System.out.println(listNode.next.next.next);
//        System.out.println(listNode.next.next.next.next);
//        System.out.println(listNode.next.next.next.next.next);
//
//        ListNode l = listNode;
//        ListNode p = new ListNode();
        printListFromTailToHead(listNode);
    }

    public static ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> list = new ArrayList<>();
        ListNode current = null;
        ListNode next = null;
        while (listNode != null) {
            next = listNode.next;
            listNode.next = current;
            current = listNode;
            listNode = next;
        }
        System.out.println(current);
        System.out.println(current.next);
        System.out.println(current.next.next);
        System.out.println(current.next.next.next);

        while (current != null) {
            list.add(current.val);
            current = current.next;
        }
        return list;
    }

    public static class ListNode {
        int val;
        ListNode next = null;

        public ListNode(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return this.val + "";
        }

    }
}
