package sort;

import java.util.Arrays;

public class Test implements Cloneable {
    public static void main(String[] args) {
        int[] sortArr = {1, 16, 5, 161, 1613, 33, 646, 42, 63};
        Sort sort = new BucketSort();
        sort.sort(sortArr);
        System.out.println(Arrays.toString(sortArr));
    }
}
