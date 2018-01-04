package solutions;

import java.util.*;

public class Solutions {
    
    //Used for binary tree problems.
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    //Used in the merge interval problem.
    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }
    
    //297. Serialize and Deserialize Binary Tree
    public String serialize(TreeNode root) {
        if (root == null) {
            return "N";
        }
        StringBuilder temp = new StringBuilder();
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.removeFirst();
            if (node == null) {
                temp.append('N');
                temp.append(',');
            } else {
                temp.append(node.val);
                temp.append(',');
                queue.add(node.left);
                queue.add(node.right);
            }
        }
        return temp.toString();
    }
	
    public TreeNode deserialize(String data) {
        TreeNode root = null;
        if (data.equals("N")) {
            return root;
        }
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        String [] tokenized = data.split(",");
        root = new TreeNode(Integer.valueOf(tokenized[0]));
        queue.add(root);
        for (int i = 1; i < tokenized.length; i+=2) {
            TreeNode node = queue.removeFirst();
            if (!tokenized[i].equals("N")) {
                node.left = new TreeNode(Integer.valueOf(tokenized[i]));
                queue.add(node.left);
            } else {
                node.left = null;
            }
            
            if (!tokenized[i+1].equals("N")) {
                node.right = new TreeNode(Integer.valueOf(tokenized[i+1]));
                queue.add(node.right);
            } else {
                node.right = null;
            }
        }
        return root;
    }
	
    //179. Largest Number
    public String largestNumber(int[] nums) {
        if (nums.length == 0) {
            return "";
        }
        Integer [] objVersion = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            objVersion[i] = new Integer(nums[i]);
        }
        Arrays.sort(objVersion, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                String one = a+""+b;
                String two = b+""+a;
                return (one.compareTo(two));
            }    
        });
        StringBuilder temp = new StringBuilder();
        int numZero = 0;
        for (int i = objVersion.length-1; i >= 0; i--) {
            temp.append(objVersion[i]);
            if (objVersion[i] == 0) {
                numZero+=1;
            }
        }
        if (numZero == nums.length) {
            return "0";
        }
        return temp.toString();
    }
    
    //150. Evaluate Reverse Polish Notation
    public int evalRPN(String[] tokens) {
        if (tokens.length == 0) {
            return 0;
        }
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < tokens.length; i++) {
            String s = tokens[i];
            if (s.equals("/") || s.equals("+") || s.equals("-") || s.equals("*")) {
                int numOne = 0, numTwo = 0;
                if (!stack.isEmpty()) {
                    numOne = stack.pop();
                } else {
                    return 0;
                }
                if (!stack.isEmpty()) {
                    numTwo = stack.pop();
                } else {
                    return 0;
                }
                int val = 0;
                if (s.equals("/")) {
                    val = numTwo/numOne;
                } else if (s.equals("*")) {
                    val = numTwo*numOne;
                } else if (s.equals("+")) {
                    val = numTwo+numOne;
                } else {
                    val = numTwo-numOne;
                }
                stack.push(val);
            } else {
                stack.push(Integer.parseInt(s));
            }
        }
        return (stack.pop());
    }
    
    //94. Binary Tree Inorder Traversal
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<Integer>();
        if (root != null) {
            Stack<TreeNode> stack = new Stack<TreeNode>();
            stack.push(root);
            TreeNode nextUp = root.left;
            while (!stack.isEmpty() || nextUp != null) {
                if (nextUp == null) {
                    TreeNode popped = stack.pop();
                    list.add(popped.val);
                    nextUp = popped.right;
                } else {
                    TreeNode temp = nextUp;
                    nextUp = temp.left;
                    stack.push(temp);
                }
            }
        }
        return list;
    }
    
    //3. Longest Substring Without Repeating Characters
    public int lengthOfLongestSubstring(String input) {
        if (input.equals("") || input == null) {
            return 0;
        }
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        int longestLength = 0;
        int start = 0;
        
        for (int i = 0; i<input.length(); i++) {
            if (map.containsKey(input.charAt(i))) {
                int newStart = map.remove(input.charAt(i))+1;
                if (newStart < input.length()) {
                    start = Math.max(newStart, start);
                }
            }
            map.put(input.charAt(i), i);
            int temp = (i-start)+1;
            longestLength = Math.max(temp, longestLength);
        }
        return longestLength;
    }
    
    //134. Gas Station
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int deficit = 0;
        int gasLeft = 0;
        int start = -1;
        for (int i = 0; i < gas.length; i++) {
            if (gas[i]+gasLeft >= cost[i]) {
                if (start == -1) {
                    start = i;
                }
                gasLeft = (gas[i]+gasLeft)-cost[i];
            } else {
                int temp = (gas[i]+gasLeft)-cost[i];
                deficit+=temp;
                gasLeft = 0;
                start = -1;
            }
        }
        return ((deficit+gasLeft) >= 0? start : -1);
    }
    
    //56. Merge Intervals
    public List<Interval> merge(List<Interval> intervals) {
        List<Interval> list = new ArrayList<Interval>();
        if (intervals == null || intervals.size() == 0) {
            return list;
        }
        Collections.sort(intervals, new Comparator<Interval>() {
            public int compare(Interval i, Interval j) {
                if (i.start == j.start) {
                    return (i.end-j.end);
                }
                return (i.start-j.start);
            }    
        });
        Interval ele = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            Interval curr = intervals.get(i);
            if (curr.start == ele.start || curr.start <= ele.end) {
                ele.end = Math.max(ele.end, curr.end);
            } else {
                list.add(ele);
                ele = curr;
            }
        }
        list.add(ele);
        return list;
    }
}
