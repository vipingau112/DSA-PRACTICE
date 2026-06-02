class Solution {
    public int jump(int[] nums) {
       //if array has 1 or fewer elements , 0 jumps are needed.
       if(nums== null || nums.length<=1){
        return 0;
       } 
       int jumps = 0;
       int currentEnd = 0;
       int farthest = 0;
       //Iterate through the array except for the last element 
       for(int i = 0; i < nums.length-1;i++){
        //update the farthest index we can reach from the current position 
        farthest = Math.max(farthest,i+nums[i]);
        // if we've reached the end of the current jump's range 
        if(i == currentEnd){
            jumps++;
            //commit to a jump
            currentEnd = farthest;// Set the new boundary to the farthest reachable point 
            // Optimization : if the new boundary already reaches  or exceeds the last index, stop 
            if(currentEnd >= nums.length-1){
                break;
            }
        }
       }
       return jumps;
    }
}