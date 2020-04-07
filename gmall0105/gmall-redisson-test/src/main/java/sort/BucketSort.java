package sort;

public class BucketSort implements Sort {

    @Override
    public void sort(int[] sort) {
        int[][] bucket = new int[10][sort.length];
        int[] bucketCounts = new int[10];

        int max = sort[0];
        for (int i = 1; i < sort.length; i++) {
            if (sort[i] > max) {
                max = sort[i];
            }
        }
        int maxLength = (max + "").length();

        for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
            for (int j = 0; j < sort.length; j++) {
                int dig = (sort[j] / n) % 10;
                bucket[dig][bucketCounts[dig]] = sort[j];
                bucketCounts[dig]++;
            }
            int index = 0;
            for (int k = 0; k < bucketCounts.length; k++) {
                if (bucketCounts[k] != 0) {
                    for (int l = 0; l < bucketCounts[k]; l++) {
                        sort[index] = bucket[k][l];
                        index++;
                    }
                }
                bucketCounts[k] = 0;
            }
        }


    }
}
