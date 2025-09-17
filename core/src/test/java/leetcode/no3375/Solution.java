package leetcode.no3375;

import java.util.Objects;
import java.util.TreeSet;

class Solution {
    /// 1 <= nums.length <= 100
    ///
    /// 1 <= nums\[i] <= 100
    ///
    /// 1 <= k <= 100
    public int minOperations(int[] nums, int k) {
        var ordered = new TreeSet<Integer>();

        for (var num : nums) {
            ordered.add(num);
        }

        var min = ordered.iterator().next();
        if (min < k) {
            return -1;
        }

        return min == k
            ? ordered.size() - 1
            : ordered.size();
    }

    public static void main(String[] args) {
        var sol = new Solution();
        
        eq(sol.minOperations(new int[]{9, 7, 5, 3}, 1), 4);
        eq(sol.minOperations(new int[]{2, 1, 2}, 2), -1);
        eq(sol.minOperations(new int[]{5, 2, 5, 4, 5}, 2), 2);
    }

    public static void eq(Object a, Object b) {
        if (!Objects.equals(a, b)) {
            throw new IllegalArgumentException(String.format("'%s' and '%s' not equal", a, b));
        }
    }
}