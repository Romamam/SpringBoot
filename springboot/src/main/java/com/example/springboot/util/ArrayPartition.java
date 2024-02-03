package com.example.springboot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayPartition {

    public static int find_diff(int num1, int num2) {
        return Math.abs(num1 - num2);
    }

    public static void balance(int sum2, int sum1, int a2[], int a1[], int n, List<List<Integer>> result) {
        int diff = (sum2 - sum1) / 2;
        int min, bal = 999999;
        int idx1 = 0, idx2 = 0, temp1, temp2;
        int i = 0, j = 0;
        boolean FLAG = false;
        while (i < n && j < n) {
            min = a2[j] - a1[i];
            if (min > 0) {
                if (min < diff * 2) {
                    min = find_diff(min, diff);
                    if (bal > min) {
                        bal = min;
                        idx1 = i;
                        idx2 = j;
                        i++;
                        FLAG = true;
                    } else
                        j++;
                } else
                    j++;
            } else
                i++;
        }
        if (FLAG) {
            temp1 = a1[idx1];
            temp2 = a2[idx2];
            for (i = idx1; i >= 0; i--) {
                if (temp2 > a1[i - 1])
                    a1[i] = a1[i - 1];
                else {
                    a1[i] = temp2;
                    break;
                }
            }
            for (i = idx2; i < n; i++) {
                if (temp1 < a2[i + 1])
                    a2[i] = a2[i + 1];
                else {
                    a2[i] = temp1;
                    break;
                }
            }

            sum1 = sum1 - temp1 + temp2;
            sum2 = sum2 - temp2 + temp1;

            if (sum2 > sum1)
                balance(sum2, sum1, a2, a1, n, result);
            else
                balance(sum1, sum2, a1, a2, n, result);
        } else {
            List<Integer> partition1 = new ArrayList<>();
            List<Integer> partition2 = new ArrayList<>();
            for (int num : a1) {
                partition1.add(num);
            }
            for (int num : a2) {
                partition2.add(num);
            }
            result.add(partition1);
            result.add(partition2);
        }
    }

    public static List<List<Integer>> divided_array(int a[]) {
        int n = a.length;
        int a1[] = new int[n / 2];
        int a2[] = new int[n / 2];
        int sum1 = 0, sum2 = 0;
        int k = 0;

        Arrays.sort(a);

        a1[0] = a[n - 1];
        a2[0] = a[n - 2];

        for (int i = n - 2; i >= 2; i -= 2) {
            sum1 += a1[k];
            sum2 += a2[k];
            k += 1;
            if (sum1 > sum2) {
                a1[k] = a[i - 2];
                a2[k] = a[i - 1];
            } else {
                a1[k] = a[i - 1];
                a2[k] = a[i - 2];
            }
        }

        sum1 += a1[k];
        sum2 += a2[k];

        List<List<Integer>> result = new ArrayList<>();
        if (sum2 > sum1)
            balance(sum2, sum1, a2, a1, k + 1, result);
        else
            balance(sum1, sum2, a1, a2, k + 1, result);

        return result;
    }
}
