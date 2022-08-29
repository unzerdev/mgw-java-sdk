package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonBancontact;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class Bancontact extends AbstractPaymentType implements PaymentType {

    private String holder;

    public Bancontact(String holder) {
        this.holder = holder;
    }

    public Bancontact() {
        super();
    }

    public Bancontact(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getTypeUrl() {
        return "types/bancontact";
    }

    @Override
    public PaymentType map(PaymentType bancontact, JsonObject jsonId) {
        if (bancontact instanceof Bancontact && jsonId instanceof JsonBancontact) {
            ((Bancontact) bancontact).setId(jsonId.getId());
            ((Bancontact) bancontact).setRecurring(((JsonBancontact) jsonId).getRecurring());
            ((Bancontact) bancontact).setHolder(((JsonBancontact) jsonId).getHolder());
        }
        return bancontact;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}
