package sort;

public class InsertSort implements Sort {
    @Override
    public void sort(int[] sort) {
        int i = 1;
        while (i < sort.length) {
            int temp = sort[i];
            int j = i - 1;
            while (j >= 0 && temp < sort[j]) {
                sort[j + 1] = sort[j];
                j--;
            }
            sort[j + 1] = temp;
            i++;
        }
    }
}
