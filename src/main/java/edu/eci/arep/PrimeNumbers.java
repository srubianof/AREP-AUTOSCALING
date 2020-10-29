package edu.eci.arep;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PrimeNumbers {
    public static Long primeNumbersTill(int n) {
        boolean prime[] = new boolean[n + 1];
        Arrays.fill(prime, true);
        for (int p = 2; p * p <= n; p++) {
            if (prime[p]) {
                for (int i = p * 2; i <= n; i += p) {
                    prime[i] = false;
                }
            }
        }
        List<Long> primeNumbers = new LinkedList<>();
        for (long i = 2; i <= n; i++) {
            if (prime[(int) i]) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers.get(primeNumbers.size()-1);
    }
}
