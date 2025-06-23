package ru.shaxowskiy.cloudfilestorage;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
       int[] num = {3, 4, 5, 6};
        int[] num1 = twoSum(num, 7);
        System.out.println(Arrays.toString(num1));
    }

    public static int[] twoSum(int[] nums, int target) {
        int[] twoNumbers = new int[2];
        for (int i = 0; i < nums.length; i++){
            for (int j = 0; j < nums.length; j++){
                if(i == j){
                    continue;
                }
                if(nums[i] + nums[j] == target){
                    twoNumbers[0] = i;
                    twoNumbers[1] = j;
                    break;
                }
            }
        }
        return twoNumbers;
    }
}
