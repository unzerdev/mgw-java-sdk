package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Prepayment;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class PrepaymentTest extends AbstractPaymentTest {

    @Test
    public void testCreatePrepaymentManatoryType() {
        Prepayment prepayment = new Prepayment();
        prepayment = getUnzer().createPaymentType(prepayment);
        assertNotNull(prepayment.getId());
    }

    @Test
    public void testChargePrepaymentType() {
        Prepayment prepayment = getUnzer().createPaymentType(getPrepayment());
        Charge charge = prepayment.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getProcessing());
        assertNotNull(charge.getProcessing().getIban());
        assertNotNull(charge.getProcessing().getBic());
        assertNotNull(charge.getProcessing().getDescriptor());
        assertNotNull(charge.getProcessing().getHolder());
    }

    @Test
    public void testFetchPrepaymentType() {
        Prepayment prepayment = getUnzer().createPaymentType(getPrepayment());
        assertNotNull(prepayment.getId());
        Prepayment fetchedPrepayment = (Prepayment) getUnzer().fetchPaymentType(prepayment.getId());
        assertNotNull(fetchedPrepayment.getId());
    }


    private Prepayment getPrepayment() {
        return new Prepayment();
    }


}
