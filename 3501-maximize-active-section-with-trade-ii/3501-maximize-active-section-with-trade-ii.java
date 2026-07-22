import java.util.*;

class Solution {

    // Segment Tree to answer Range Maximum Queries on the array of adjacent zero-block sums
    static class SegmentTree {
        int n;
        int[] tree;

        public SegmentTree(int[] arr) {
            this.n = arr.length;
            this.tree = new int[4 * Math.max(1, n)];
            if (n > 0) {
                build(arr, 0, 0, n - 1);
            }
        }

        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = start + (end - start) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }

        public int query(int l, int r) {
            if (l > r || n == 0) return 0;
            return query(0, 0, n - 1, l, r);
        }

        private int query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0; // Out of bounds
            if (l <= start && end <= r) return tree[node]; // Fully in range

            int mid = start + (end - start) / 2;
            int leftMax = query(2 * node + 1, start, mid, l, r);
            int rightMax = query(2 * node + 2, mid + 1, end, l, r);
            return Math.max(leftMax, rightMax);
        }
    }

    public List<Integer> maxActiveSectionsAfterTrade(String s, int[][] queries) {
        int n = s.length();
        int totalOnes = 0;
        for (int k = 0; k < n; k++) {
            if (s.charAt(k) == '1') totalOnes++;
        }

        // 1. Identify all zero-blocks
        List<Integer> zeroLens = new ArrayList<>();
        List<Integer> bLeft = new ArrayList<>();
        List<Integer> bRight = new ArrayList<>();

        int idx = 0;
        while (idx < n) {
            if (s.charAt(idx) == '0') {
                int start = idx;
                while (idx < n && s.charAt(idx) == '0') {
                    idx++;
                }
                int end = idx - 1;
                bLeft.add(start);
                bRight.add(end);
                zeroLens.add(end - start + 1);
            } else {
                idx++;
            }
        }

        int m = zeroLens.size();
        List<Integer> ans = new ArrayList<>(queries.length);

        // Edge case: less than 2 zero blocks -> no trade can merge two blocks
        if (m < 2) {
            for (int i = 0; i < queries.length; i++) {
                ans.add(totalOnes);
            }
            return ans;
        }

        // 2. Precompute sums of adjacent zero-block lengths for Case 3
        int[] tmpSum = new int[m - 1];
        for (int k = 0; k < m - 1; k++) {
            tmpSum[k] = zeroLens.get(k) + zeroLens.get(k + 1);
        }

        // 3. Build Segment Tree on tmpSum
        SegmentTree segTree = new SegmentTree(tmpSum);

        // 4. Process each query
        for (int[] q : queries) {
            int ql = q[0];
            int qr = q[1];

            // Find first block ending >= ql
            int i = binarySearchRight(bRight, ql);
            // Find last block starting <= qr
            int j = binarySearchLeft(bLeft, qr);

            // Out of range or no valid zero-blocks inside [ql, qr]
            if (i >= m || j < 0 || i >= j) {
                ans.add(totalOnes);
                continue;
            }

            // Exactly 2 zero blocks overlap with query
            if (j - i == 1) {
                int ziPrime = bRight.get(i) - Math.max(bLeft.get(i), ql) + 1;
                int zjPrime = Math.min(bRight.get(j), qr) - bLeft.get(j) + 1;
                ans.add(totalOnes + ziPrime + zjPrime);
                continue;
            }

            // 3 or more zero blocks overlap with query
            int ziPrime = bRight.get(i) - Math.max(bLeft.get(i), ql) + 1;
            int zjPrime = Math.min(bRight.get(j), qr) - bLeft.get(j) + 1;

            // Case 1: First trimmed block + second full block
            int val1 = ziPrime + zeroLens.get(i + 1);

            // Case 2: Second-to-last full block + last trimmed block
            int val2 = zeroLens.get(j - 1) + zjPrime;

            // Case 3: Range max on fully interior adjacent pairs (if any exist)
            int val3 = segTree.query(i + 1, j - 2);

            int bestGain = Math.max(val1, Math.max(val2, val3));
            ans.add(totalOnes + bestGain);
        }

        return ans;
    }

    // First index where list.get(idx) >= target
    private int binarySearchRight(List<Integer> list, int target) {
        int low = 0, high = list.size() - 1;
        int res = list.size();
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (list.get(mid) >= target) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    // Last index where list.get(idx) <= target
    private int binarySearchLeft(List<Integer> list, int target) {
        int low = 0, high = list.size() - 1;
        int res = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (list.get(mid) <= target) {
                res = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return res;
    }
}