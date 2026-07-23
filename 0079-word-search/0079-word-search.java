class Solution {
    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        // Try starting the search from every single cell in the grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // If the first character matches, launch DFS
                if (board[r][c] == word.charAt(0)) {
                    if (dfs(board, word, r, c, 0)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    private boolean dfs(char[][] board, String word, int r, int c, int index) {
        // Base Case 1: If we matched all characters in the word, we win!
        if (index == word.length()) {
            return true;
        }

        // Base Case 2: Out of bounds check or current cell doesn't match the required character
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length || board[r][c] != word.charAt(index)) {
            return false;
        }

        // 1. Mark the cell as visited so we don't reuse it in the current path
        char temp = board[r][c];
        board[r][c] = '#';

        // 2. Explore all 4 possible directions (Up, Down, Left, Right)
        boolean found = dfs(board, word, r + 1, c, index + 1) || // Down
                        dfs(board, word, r - 1, c, index + 1) || // Up
                        dfs(board, word, r, c + 1, index + 1) || // Right
                        dfs(board, word, r, c - 1, index + 1);   // Left

        // 3. Backtrack: Restore the original character before returning
        board[r][c] = temp;

        return found;
    }
}