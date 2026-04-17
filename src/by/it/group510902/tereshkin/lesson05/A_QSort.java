package by.it.group510902.tereshkin.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.PriorityQueue;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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


        int[] indicates = new int[m];
        for (int i = 0; i < m; i++){
            indicates[i] = i;
        }

        quickSortSegments(segments, 0, n-1);
        quickSortIndices(indicates, points,0, m-1);


        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int segmentIndex = 0;

        for (int indicate : indicates){
            int point = points[indicate];

            while (segmentIndex < n && segments[segmentIndex].start <= point){
                heap.add(segments[segmentIndex].stop);
                segmentIndex++;
            }

            while (!heap.isEmpty() && heap.peek() < point){
                heap.poll();
            }
            result[indicate] = heap.size();
        }

        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start < stop){
                this.start = start;
                this.stop = stop;
            }
            else{
                this.stop = start;
                this.start = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            int s1 = start;
            int s2 = o.start;
            return Integer.compare(s1, s2);
        }
    }

    private int partitionSegments(Segment[] segments, int left, int right){
        Segment pivot = segments[right];
        int i = left - 1;

        for (int j = left; j < right; j++){
            if (segments[j].start <= pivot.start){
                i++;
                Segment temp = segments[j];
                segments[j] = segments[i];
                segments[i] = temp;
            }
        }

        Segment temp = segments[right];
        segments[right] = segments[i+1];
        segments[i+1] = temp;

        return i+1;
    }

    private void quickSortSegments(Segment[] segments, int left, int right){
        if (left >= right) return;
        int mid = partitionSegments(segments, left, right);
        quickSortSegments(segments, left, mid-1);
        quickSortSegments(segments,mid + 1, right);
    };

    private int partitionIndices(int[] indicates, int[] points, int left, int right){
        int pivot = points[indicates[right]];
        int i = left - 1;

        for (int j = left; j < right; j++){
            if (points[indicates[j]] <= pivot){
                i++;
                int temp = indicates[j];
                indicates[j] = indicates[i];
                indicates[i] = temp;
            }
        }

        int temp = indicates[right];
        indicates[right] = indicates[i+1];
        indicates[i+1] = temp;

        return i+1;
    }

    private void quickSortIndices(int[] indicates, int[] points, int left, int right){
        if (left >= right) return;
        int mid = partitionIndices(indicates, points, left, right);
        quickSortIndices(indicates, points, left, mid-1);
        quickSortIndices(indicates, points,mid + 1, right);
    }


}
