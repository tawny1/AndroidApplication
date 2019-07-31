package com.tawny.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Author: tawny
 * Data：2018/11/8
 */
public class Offer {

    public static void main(String[] arg) {
//        Solution8 solution8 = new Solution8();
//        int[] ints = new int[]{2, 4, 8, 9, 11, 45};
//        int[] ints1 = new int[]{5, 4, 7, 12, 24, 35};
//
//        System.out.println(solution8.findMedianSortedArrays(ints, ints1));

        Solution9 solution9 = new Solution9();
//        int[] ints = new int[]{1, 3, 4, 5};
//        int[] ints1 = new int[]{7, 5, 3, 2, 1};
//        int[] ints1 = new int[]{1, 2, 3};

        int[] ints = new int[]{1};
        int[] ints1 = new int[]{2, 3, 4};

        System.out.println(solution9.test(ints, ints1));
    }


    /**
     * 用两个栈来实现一个队列，完成队列的Push和Pop操作。
     * 队列中的元素为int类型。
     */
    static class Solution1 {

        Stack<Integer> stack1 = new Stack<Integer>();
        Stack<Integer> stack2 = new Stack<Integer>();

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


    /**
     * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
     * 输入一个非减排序的数组的一个旋转，输出旋转数组的最小元素。
     * 例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。
     * NOTE：给出的所有元素都大于0，若数组大小为0，请返回0。
     */
    static class Solution2 {

        public int minNumberInRotateArray(int[] array) {
            if (array == null || array.length == 0) {
                return 0;
            }
            int len = array.length;
            return findMin(array, 0, len - 1);
        }

        int findMin(int[] array, int left, int right) {
            if (left >= right || array[left] < array[right]) {
                return array[left];
            }
            int mid;
            mid = (left + right) / 2;
            if (array[mid] > array[left]) {
                return findMin(array, mid + 1, right);
            } else if (array[mid] == array[left]) {
                return findMin(array, mid + 1, right) < findMin(array, left, mid - 1) ? findMin(array, mid + 1, right) : findMin(array, left, mid - 1);
            } else {
                return findMin(array, left, mid);
            }
        }
    }


    /**
     * 输入一个链表，按链表值从尾到头的顺序返回一个ArrayList。
     */
    static class Solution3 {

        class ListNode {
            int val;
            ListNode next = null;

            ListNode(int val) {
                this.val = val;
            }
        }

        public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
            ArrayList<Integer> list = new ArrayList<>();
            ListNode pre = null;
            ListNode next = null;
            while (listNode != null) {
                next = listNode.next;
                listNode.next = pre;
                pre = listNode;
                listNode = next;
            }
            while (pre != null) {
                list.add(pre.val);
                pre = pre.next;
            }
            return list;
        }
    }


    /**
     * 大家都知道斐波那契数列，现在要求输入一个整数n，
     * 请你输出斐波那契数列的第n项（从0开始，第0项为0）。n<=39
     */
    static class Solution4 {

        int Fibonacci(int n) {
            if (n <= 0)
                return 0;
            if (n == 1 || n == 2)
                return 1;
            return Fibonacci(n - 1) + Fibonacci(n - 2);
        }
    }


    /**
     * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。
     * 求该青蛙跳上一个n级的台阶总共有多少种跳法（先后次序不同算不同的结果）。
     */
    static class Solution5 {
        int JumpFloor(int target) {
            if (target <= 0)
                return 0;
            if (target == 1)
                return 1;
            if (target == 2)
                return 2;
            return JumpFloor(target - 1) + JumpFloor(target - 2);
        }
    }

    /**
     * 一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。
     * 求该青蛙跳上一个n级的台阶总共有多少种跳法。
     * <p>
     * 思路：
     * Fib(n) = Fib(n-1)+Fib(n-2)+Fib(n-3)+...+Fib(1)+1
     * Fib(n-1)=Fib(1)+Fib(2)+.......+Fib(n-2)+1
     * Fib(n)-Fib(n-1)=Fib(n-1)
     * 即：Fib(n) = 2*Fib(n-1)     n >= 2
     */
    static class Solution6 {

        int jumpFloorII(int number) {
            if (number < 0)
                return -1;
            else if (number == 0 || number == 1)
                return 1;
            else
                return 2 * jumpFloorII(number - 1);
        }
    }


    /**
     * 输入一个整数，输出该数二进制表示中1的个数。
     * 其中负数用补码表示。
     */
    static class Solution7 {

        int NumberOf1(int n) {
            int count = 0;
            while (n != 0) {
                count++;
                n = n & (n - 1);
            }
            return count;
        }
    }


    /**
     * 有两个大小为 m 和 n 的排序数组 nums1 和 nums2 。
     * 请找出两个排序数组的中位数，并且总的运行时间复杂度为 O(m+n) 。
     * <p>
     * 思路：（归并排序）
     * 将数组合并，创建一个大小为m+n的数组arr。由后向前遍历，比较两个数组末尾元素的大小，
     * 将大的放入arr数组末尾，下标前移，依次比较。直到某一数组全部放入arr数组，
     * 将剩下的数组拷贝进去。最后做奇偶判断。
     */
    static class Solution8 {
        double findMedianSortedArrays(int[] nums1, int[] nums2) {
            int len1 = nums1.length;
            int len2 = nums2.length;
            int[] arr = new int[len1 + len2];
            int end1 = len1 - 1;     //数组nums1的末尾下标
            int end2 = len2 - 1;     //数组nums2的末尾下标
            int index = arr.length - 1;     //数组arr的末尾下标
            while (end1 >= 0 && end2 >= 0) {
                if (nums1[end1] > nums2[end2]) {
                    arr[index] = nums1[end1];
                    end1--;
                    index--;
                } else {
                    arr[index] = nums2[end2];
                    end2--;
                    index--;
                }
            }

            //复制剩下部分 arr前面的数组（可能是nums1，也可能是nums2）
            while (end1 > 0) {
                arr[index] = nums1[end1];
                end1--;
                index--;
            }

            while (end2 > 0) {
                arr[index] = nums2[end2];
                end2--;
                index--;
            }

            if (arr.length % 2 == 0) {
                return (double) (arr[arr.length / 2] + arr[arr.length / 2 - 1]) / 2;
            } else {
                return arr[arr.length / 2];
            }
        }
    }


    /**
     * 有两个大小为 m 和 n 的排序数组 nums1 和 nums2 。
     * 请找出两个排序数组的中位数，并且总的运行时间复杂度为 O(log(m+n)) 。
     * <p>
     * 思路：（二分查找法） --> 时间复杂度O(log(m+n)) :
     * 1、找两个数组的中位数，也就是找在两个数组中第k小的数(k=(m+n)/2)；
     * 2、对于两个递增的数组，存在两个数i,j，如果nums1[i] > nums2[j],
     * 那么num2[0-j]的数都小于nums[i]。
     * 假设我们要找第k小的数，令i=k/2，j=k/2；则每一次都能找到比第k个数字小k/2的数字。
     * 将k/2个的数字从数组中去掉，则第k个数字就变成了k-k/2小的数字
     * 3、重复比较删除操作，当k=1时，就是我们要找的中位数。
     */
    static class Solution9 {
        double test(int[] nums1, int[] nums2) {
            int len1 = nums1.length;
            int len2 = nums2.length;
            int size = len1 + len2;

            if (size % 2 == 0) {
                int k = size / 2;

                if (len1 < 3){
                    System.out.println();
                }

                if (nums1.length == 0) {
                    return (double) (nums2[nums2.length / 2 - 1] + nums2[nums2.length / 2])/2;
                }
                if (nums2.length == 0) {
                    return (double)(nums1[nums1.length / 2 - 1] + nums1[nums1.length / 2])/2;
                }
                if (len1 < k / 2) {
                    if (nums2[nums2.length / 2] > nums1[nums1.length - 1]) {
                        return (double)(nums2[nums2.length / 2 - nums1.length / 2] + nums2[nums2.length / 2 - nums1.length / 2 + 1]) / 2;
                    }
                    return (double)(nums2[nums2.length / 2 + nums1.length / 2] + nums2[nums2.length / 2 + nums1.length / 2 + 1]) / 2;
                }
                if (nums2.length < k / 2) {
                    if (nums1[nums1.length / 2] > nums2[nums2.length - 1]) {
                        return nums1.length / 2 - nums2.length / 2;
                    }
                    return (nums1[nums1.length / 2 - nums2.length / 2] + nums1[nums1.length / 2 - nums2.length / 2 + 1]) / 2;
                }
                return (findNumber(nums1, nums2, k, len1, len2) + findNumber(nums1, nums2, k + 1, len1, len2)) / 2;
            } else {
                int k = size / 2;
                if (nums1.length == 0) {
                    return nums2[nums2.length / 2];
                }
                if (nums2.length == 0) {
                    return nums1[nums1.length / 2];
                }
                if (nums1.length < k/ 2) {
                    if (nums2[nums2.length / 2] > nums1[nums1.length - 1]) {
                        return nums2.length / 2 - nums1.length / 2;
                    }
                    return nums2[(nums2.length / 2 - nums1.length / 2)];
                }
                if (nums2.length < k/2 ) {
                    if (nums1[nums1.length / 2] > nums2[nums2.length - 1]) {
                        return nums1.length / 2 - nums2.length / 2;
                    }
                    return nums1[(nums1.length / 2 - nums2.length / 2)];
                }
                return findNumber(nums1, nums2, k + 1, len1, len2);
            }
        }

        private double findNumber(int[] nums1, int[] nums2, int k, int len1, int len2) {


            if (k != 1) {

                if (nums1[k / 2 - 1] >= nums2[k / 2 - 1]) {
                    int newArray[] = new int[len2 - (k / 2)];
                    len2 = len2 - k / 2;
//                    System.arraycopy(nums2, k / 2, newArray, k / 2, newArray.length - k / 2);
                    int i = 0;
                    for (int j = k / 2; j < newArray.length + (k / 2); j++) {
                        newArray[i++] = nums2[j];
                    }
                    System.out.println(Arrays.toString(newArray));
                    return findNumber(nums1, newArray, k - (k / 2), len1, len2);
                } else {

                    if (nums1.length == 0) {
                        return nums2[k];
                    }
                    if (nums2.length == 0) {
                        return nums1[k];
                    }

                    int newArray[] = new int[len1 - (k / 2)];
                    len1 = len1 - k / 2;
                    int i = 0;
                    for (int j = k / 2; j < newArray.length + (k / 2); j++) {
                        newArray[i++] = nums1[j];
                    }
                    System.out.println(Arrays.toString(newArray));
                    return findNumber(newArray, nums2, k - (k / 2), len1, len2);
                }
            } else {
                if (nums1.length == 0) {
                    return nums2[0];
                }
                if (nums2.length == 0) {
                    return nums1[0];
                }
                if (nums1[0] >= nums2[0]) {
                    return nums2[0];
                } else {
                    return nums1[0];
                }
            }
        }


        double findMedianSortedArrays(int[] nums1, int[] nums2) {
            int len1 = nums1.length;
            int len2 = nums2.length;
            int size = len1 + len2;
            if (size % 2 == 0) {
                return (findKth(nums1, 0, len1, nums2, 0, len2, size / 2) +
                        findKth(nums1, 0, len1, nums2, 0, len2, size / 2 + 1)) / 2;
            } else {
                return findKth(nums1, 0, len1, nums2, 0, len2, size / 2 + 1);
            }
        }

        private double findKth(int[] nums1, int start1, int len1, int[] nums2, int start2, int len2, int k) {
            if (len1 - start1 > len2 - start2) {
                return findKth(nums2, start2, len2, nums1, start1, len1, k);
            }
            if (len1 - start1 == 0) {
                return nums2[k - 1];
            }
            if (k == 1) {
                return Math.min(nums1[start1], nums2[start2]);
            }

            int p1 = start1 + Math.min(len1 - start1, k / 2);
            int p2 = start2 + k - p1 + start1;
            if (nums1[p1 - 1] < nums2[p2 - 1]) {
                return findKth(nums1, p1, len1, nums2, start2, len2, k - p1 + start1);
            } else if (nums1[p1 - 1] > nums2[p2 - 1]) {
                return findKth(nums1, start1, len1, nums2, p2, len2, k - p2 + start2);
            } else {
                return nums1[p1 - 1];
            }
        }
    }

}

