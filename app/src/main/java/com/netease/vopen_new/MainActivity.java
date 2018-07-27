package com.netease.vopen_new;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(Solution.isMatch("aa", "a*"));
    }

    static class Solution {
        public static boolean isMatch(String s, String p) {
            if (s == null || s.equals("")) {
                if (p == null || p.equals("") || p.length() <= 2 && p.endsWith("*")) {
                    return true;
                }
                return false;
            }
            if (p == null || p.equals("")){
                return false;
            }
            int lenp = p.length(), lens = s.length();
            char tmp = '*';
            int i=0, j=0;
            while (i<lenp && j<lens) {
                switch (p.charAt(i)) {
                    case '.':
                        System.out.println("case '.'");
                        tmp = s.charAt(j);
                        i++;
                        j++;
                        break;
                    case '*':
                        System.out.println("case '*'");
                        if (s.charAt(j) == tmp) {
                            j++;
                        } else {
                            i++;
                        }
                        break;
                    default:
                        System.out.println("default");
                        if (p.charAt(i) == s.charAt(j)) {
                            tmp = s.charAt(j);
                            i++;
                            j++;
                        } else {
                            return false;
                        }
                        break;
                }
                System.out.println("i="+i);
                System.out.println("j="+j);
            }
            if (j == lens && (i == lenp || i == lenp - 1 && p.charAt(i) == '*' || i == lenp - 2 && p.charAt(lenp - 1) == '*')) {
                return true;
            }
            return false;
        }
    }
}
