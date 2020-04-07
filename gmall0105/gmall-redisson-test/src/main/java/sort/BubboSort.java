package sort;

public class BubboSort  implements Sort{
    public void sort(int[] sort) {
        int len = sort.length;
        int temp;
        while (len >1) {
            for (int i = 0, j = 1; j < len; i++, j++) {
                if (sort[i] > sort[j]) {
                    temp = sort[i];
                    sort[i] = sort[j];
                    sort[j] = temp;
                }
            }
            len--;
        }
    }
}
