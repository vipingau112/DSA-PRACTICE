

class Solution {
    public int uniqueXorTriplets(int[] nums) {
        // Step 1: Array se duplicate elements hata lo
        Set<Integer> uniqueNums = new HashSet<>();
        for (int num : nums) {
            uniqueNums.add(num);
        }
        
        // Convert set to array for faster iteration
        int[] arr = new int[uniqueNums.size()];
        int index = 0;
        for (int num : uniqueNums) {
            arr[index++] = num;
        }
        
        // Step 2: Pehle 2 elements ke saare possible XOR pairs nikaalo (Round 1 & 2)
        boolean[] possiblePairs = new boolean[2048]; // Max value <= 1500, so XOR < 2048
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                possiblePairs[arr[i] ^ arr[j]] = true;
            }
        }
        
        // Step 3: Teesre element ko mix karke final unique XOR values count karo (Round 3)
        boolean[] finalXors = new boolean[2048];
        int count = 0;
        
        for (int pairXor = 0; pairXor < 2048; pairXor++) {
            if (possiblePairs[pairXor]) {
                for (int num : arr) {
                    int tripletXor = pairXor ^ num;
                    if (!finalXors[tripletXor]) {
                        finalXors[tripletXor] = true;
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
}