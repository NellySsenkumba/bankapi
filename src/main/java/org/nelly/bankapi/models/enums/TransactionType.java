package org.nelly.bankapi.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    DEPOSIT(TransactionMetric.CREDIT),
    WITHDRAWAL_AGENT(TransactionMetric.DEBIT),
    WITHDRAW_ATM(TransactionMetric.DEBIT),
    WITHDRAW_MOBILE(TransactionMetric.DEBIT),
    ONLINE_TRANSFER(TransactionMetric.DEBIT),
    INTERNAL_TRANSFER_SENDER(TransactionMetric.DEBIT),
    INTERNAL_TRANSFER_RECEIVER(TransactionMetric.CREDIT),
    POS_PURCHASE(TransactionMetric.DEBIT),
    BILL_PAYMENT(TransactionMetric.DEBIT),
    CHARGE(TransactionMetric.DEBIT),
    BALANCE_INQUIRY(TransactionMetric.DEBIT),
    ;
    private final TransactionMetric transactionMetric;
}
