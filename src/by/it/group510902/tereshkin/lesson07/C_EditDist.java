package by.it.group510902.tereshkin.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        StringBuilder res = new StringBuilder();

        int[][] dp = new int[n+1][m+1];

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i-1) == two.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    int del = dp[i-1][j] + 1;
                    int add = dp[i][j-1] + 1;
                    int change = dp[i-1][j-1] + 1;
                    dp[i][j] = Math.min(del, Math.min(add, change));
                }
            }
        }

        int i = n;
        int j = m;

        while(i > 0 || j > 0){
            if (i > 0 && j > 0 && one.charAt(i-1) == two.charAt(j-1)) {
                res.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && (j == 0 || dp[i][j] == dp[i-1][j] + 1)) {
                res.insert(0, String.format("-%c,", one.charAt(i-1)));
                i--;
            } else if (j > 0 && (i == 0 || dp[i][j] == dp[i][j-1] + 1)) {
                res.insert(0, String.format("+%c,", two.charAt(j-1)));
                j--;
            } else if (i > 0 && j > 0) {
                res.insert(0, String.format("~%c,", two.charAt(j-1)));
                i--;
                j--;
            }
        }

        return res.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}