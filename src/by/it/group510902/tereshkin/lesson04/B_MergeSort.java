package by.it.group510902.tereshkin.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        int size = 1;
        int left = 0;
        while (size < n){
            left = 0;
            while (left < n - size){
                int mid = left + size - 1;
                int right = Math.min(left + 2*size - 1, n-1);
                int[] cur = merge(a, left, mid, right);
                for(int ii = 0; ii < right - left + 1; ii++){
                    a[left+ii] = cur[ii];
                }
                left += size*2;
            }
            size *= 2;
        }
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    private int[] merge(int[] arr, int left, int mid, int right){
        int[] res = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        for (int o = 0; o < right - left + 1; o++){
            if (i > mid) {
                res[o] = arr[j];
                j++;
            } else if (j > right){
                res[o] = arr[i];
                i++;
            } else if (arr[i] <= arr[j]){
                res[o] = arr[i];
                i++;
            } else {
                res[o] = arr[j];
                j++;
            }
        }
        return res;
    }
}