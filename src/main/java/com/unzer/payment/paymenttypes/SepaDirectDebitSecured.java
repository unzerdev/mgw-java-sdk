package com.unzer.payment.paymenttypes;

public class SepaDirectDebitSecured extends SepaDirectDebit implements PaymentType {

    public SepaDirectDebitSecured(String iban) {
        super(iban);
    }

    @Override
    public String getTypeUrl() {
        return "types/sepa-direct-debit-secured";
    }

}
