package sort;

public class SelectSort implements Sort {

    @Override
    public void sort(int[] sort) {
        for (int i = 0; i < sort.length; i++) {
            int temp = sort[i];
            int index = i;
            for (int j = i; j < sort.length; j++) {
                if (sort[j] < temp) {
                    temp = sort[j];
                    index = j;
                }
            }
            sort[index] = sort[i];
            sort[i] = temp;
        }
    }
}
