class Solution {
    public String smallestPalindrome(String s, long k) {
        int n = s.length();
        int halfLen = n / 2;

        // Step 1: Count total character frequencies in s
        int[] fullCount = new int[26];
        for (int i = 0; i < n; i++) {
            fullCount[s.charAt(i) - 'a']++;
        }

        // Step 2: Extract counts for the first half & identify midChar if length is odd
        int[] halfCount = new int[26];
        char midChar = '\0';

        for (int i = 0; i < 26; i++) {
            halfCount[i] = fullCount[i] / 2;
            if (fullCount[i] % 2 != 0) {
                midChar = (char) ('a' + i);
            }
        }

        // Helper check: Total distinct permutations possible with halfCount
        if (countPermutations(halfCount, k) < k) {
            return "";
        }

        // Step 3: Construct the first half character by character (Greedy Lexicographical Choice)
        StringBuilder firstHalf = new StringBuilder();

        for (int i = 0; i < halfLen; i++) {
            for (int c = 0; c < 26; c++) {
                if (halfCount[c] == 0) continue;

                // Try placing character ('a' + c) at current position
                halfCount[c]--;
                long ways = countPermutations(halfCount, k);

                if (ways >= k) {
                    // Valid character found, keep it and move to next position
                    firstHalf.append((char) ('a' + c));
                    break;
                } else {
                    // Skip these permutations and backtrack count
                    k -= ways;
                    halfCount[c]++;
                }
            }
        }

        // Step 4: Construct complete palindrome
        StringBuilder fullPalindrome = new StringBuilder(firstHalf);
        if (n % 2 != 0) {
            fullPalindrome.append(midChar);
        }
        fullPalindrome.append(new StringBuilder(firstHalf).reverse());

        return fullPalindrome.toString();
    }

    // Helper method to calculate combinations C(n, r) capped at k + 1 in O(1) memory
    private long nCr(int n, int r, long cap) {
        if (r < 0 || r > n) return 0;
        if (r == 0 || r == n) return 1;
        if (r > n - r) r = n - r;

        double res = 1.0;
        for (int i = 1; i <= r; i++) {
            res = res * (n - i + 1) / i;
            if (res >= cap) return cap;
        }
        return (long) Math.min((long) res, cap);
    }

    // Helper method to compute permutations: (c0 + c1 + ...)! / (c0! * c1! * ...)
    private long countPermutations(int[] count, long k) {
        long ways = 1;
        int rem = 0;
        long cap = k + 1;

        for (int c = 0; c < 26; c++) {
            if (count[c] == 0) continue;
            rem += count[c];
            long comb = nCr(rem, count[c], cap);
            ways = multiplyCap(ways, comb, cap);
            if (ways >= cap) return cap;
        }
        return ways;
    }

    // Multiplication with capping to prevent 64-bit long overflow
    private long multiplyCap(long a, long b, long cap) {
        if (a == 0 || b == 0) return 0;
        if (a > cap / b) return cap;
        return Math.min(cap, a * b);
    }
}