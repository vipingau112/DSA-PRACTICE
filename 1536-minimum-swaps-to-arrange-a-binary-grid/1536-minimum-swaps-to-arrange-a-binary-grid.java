class Solution {
    public int minSwaps(int[][] grid) {
        int n = grid.length;
        int[] trailingZeros = new int[n];

        // Step 1: Har row ke rightmost 0s count karo
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (grid[i][j] == 0) {
                    count++;
                } else {
                    break;
                }
            }
            trailingZeros[i] = count;
        }

        int swaps = 0;

        // Step 2: Har row position i ke liye suitable row dhoondo aur swap karo
        for (int i = 0; i < n; i++) {
            int neededZeros = n - 1 - i;
            int foundIdx = -1;

            // Niche ki rows me dhoondo jiske pas required zeros ho
            for (int j = i; j < n; j++) {
                if (trailingZeros[j] >= neededZeros) {
                    foundIdx = j;
                    break;
                }
            }

            // Agar koi valid row nahi mili
            if (foundIdx == -1) {
                return -1;
            }

            // Row ko shift karke position i tak lao aur swaps count karo
            for (int j = foundIdx; j > i; j--) {
                int temp = trailingZeros[j];
                trailingZeros[j] = trailingZeros[j - 1];
                trailingZeros[j - 1] = temp;
                swaps++;
            }
        }

        return swaps;
    }
}