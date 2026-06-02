class Solution {
    public int trap(int[] height) {
        // If there are no blocks, no water can be trapped
        if (height == null || height.length == 0) {
            return 0;
        }

        // 1. Put fingers at the start and end
        int left = 0;
        int right = height.length - 1;
        
        // 2. Keep track of the tallest walls seen so far
        int leftMax = 0;
        int rightMax = 0;
        
        int totalWater = 0;

        // 3. Move fingers inward until they meet
        while (left < right) {
            
            // Always work on the shorter side
            if (height[left] < height[right]) {
                
                if (height[left] >= leftMax) {
                    leftMax = height[left]; // Found a new tall wall on the left
                } else {
                    totalWater += leftMax - height[left]; // Trapped water!
                }
                left++; // Move left finger inward
                
            } else {
                
                if (height[right] >= rightMax) {
                    rightMax = height[right]; // Found a new tall wall on the right
                } else {
                    totalWater += rightMax - height[right]; // Trapped water!
                }
                right--; // Move right finger inward
                
            }
        }

        return totalWater;
    }
}