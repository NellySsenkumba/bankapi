package org.nelly.bankapi.generators;

public class AccountHelper {
    public static Long generateAccountNumber() {
        return (long) (Math.random() * 1000000000);
    }
}
