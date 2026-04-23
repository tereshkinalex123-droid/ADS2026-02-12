package by.it.group510902.tereshkin.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        int[] indicates = new int[m];
        for (int i = 0; i < m; i++){
            indicates[i] = i;
        }

        quickSortSegments(segments, 0, n - 1);
        quickSortIndices(indicates, points, 0, m - 1);

        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int segmentIndex = 0;

        for (int indicate : indicates){
            int point = points[indicate];

            int boundary = upperBound(segments, point);

            while (segmentIndex < boundary){
                heap.add(segments[segmentIndex].stop);
                segmentIndex++;
            }

            while (!heap.isEmpty() && heap.peek() < point){
                heap.poll();
            }
            result[indicate] = heap.size();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop){
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

    private int[] partitionSegments(Segment[] segments, int left, int right) {
        Segment pivot = segments[right];
        int lt = left;
        int eq = left;
        int gt = right;

        while (eq <= gt){
            int cmp = segments[eq].compareTo(pivot);
            if (cmp < 0){
                swap(segments, lt, eq);
                lt++;
                eq++;
            }
            else if (cmp > 0){
                swap(segments, gt, eq);
                gt--;
            }
            else{
                eq++;
            }
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    private void quickSortSegments(Segment[] segments, int left, int right){
        while (right > left){
            int[] bounds = partitionSegments(segments, left, right);
            int lt = bounds[0];
            int gt = bounds[1];

            if (lt - left < right - gt){
                quickSortSegments(segments, left, lt - 1);
                left = gt + 1;
            }
            else{
                quickSortSegments(segments, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private int partitionIndices(int[] indicates, int[] points, int left, int right){
        int pivot = points[indicates[right]];
        int i = left - 1;

        for (int j = left; j < right; j++){
            if (points[indicates[j]] <= pivot){
                i++;
                int temp = indicates[i];
                indicates[i] = indicates[j];
                indicates[j] = temp;
            }
        }

        int temp = indicates[i + 1];
        indicates[i + 1] = indicates[right];
        indicates[right] = temp;

        return i + 1;
    }

    private void quickSortIndices(int[] indicates, int[] points, int left, int right){
        if (left >= right) return;
        int mid = partitionIndices(indicates, points, left, right);
        quickSortIndices(indicates, points, left, mid - 1);
        quickSortIndices(indicates, points, mid + 1, right);
    }

    private int upperBound(Segment [] segments, int point){
        int left = 0;
        int right = segments.length;

        while (left < right) {
            int mid = (left + right) / 2;
            if (segments[mid].start > point){
                right = mid;
            }
            else{
                left = mid + 1;
            }
        }

        return left;
    }
}