/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import com.unzer.payment.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;

import static com.unzer.payment.business.BasketV1TestData.getMinTestBasketV1;
import static com.unzer.payment.business.BasketV2TestData.getMinTestBasketV2;
import static com.unzer.payment.business.Keys.LEGACY_PRIVATE_KEY;
import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;


public class SepaDirectDebitSecuredTest extends AbstractPaymentTest {

    @Test
    public void testCreateSepaDirectDebitSecuredManatoryType() throws HttpCommunicationException {
        SepaDirectDebitSecured sdd = new SepaDirectDebitSecured("DE89370400440532013000");
        sdd = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(sdd);
        assertNotNull(sdd.getId());
    }

    @Test
    public void testCreateSepaDirectDebitSecuredFullType() throws HttpCommunicationException {
        SepaDirectDebitSecured sddOriginal = getSepaDirectDebitSecured();
        SepaDirectDebitSecured sddCreated = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(sddOriginal);
        assertSddEquals(sddOriginal, sddCreated);
    }

    @Test
    public void testFetchSepaDirectDebitSecuredType() throws HttpCommunicationException {
        Unzer unzer = getUnzer(LEGACY_PRIVATE_KEY);
        SepaDirectDebitSecured sdd = unzer.createPaymentType(getSepaDirectDebitSecured());
        assertNotNull(sdd.getId());
        SepaDirectDebitSecured fetchedSdd = (SepaDirectDebitSecured) unzer.fetchPaymentType(sdd.getId());
        assertNotNull(fetchedSdd.getId());
        assertSddEquals(sdd, fetchedSdd);
    }

    @Test
    @Deprecated
    public void testChargeSepaDirectDebitSecuredTypeBasketV1() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebitSecured sdd = unzer.createPaymentType(getSepaDirectDebitSecured());
        assertNotNull(sdd.getId());
        Basket basket = unzer.createBasket(getMinTestBasketV1());
        sdd.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"), getMaximumCustomerSameAddress(generateUuid()), basket);
    }

    @Test
    public void testChargeSepaDirectDebitSecuredTypeBasketV2() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebitSecured sdd = unzer.createPaymentType(getSepaDirectDebitSecured());
        assertNotNull(sdd.getId());
        Basket basket = unzer.createBasket(getMinTestBasketV2());
        sdd.charge(
                basket.getTotalValueGross(),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                getMaximumCustomerSameAddress(generateUuid()),
                basket
        );
    }

    private void assertSddEquals(SepaDirectDebitSecured sddOriginal, SepaDirectDebitSecured sddCreated) {
        assertNotNull(sddCreated.getId());
        assertEquals(sddOriginal.getBic(), sddCreated.getBic());
        assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
        assertEquals(sddOriginal.getIban(), sddCreated.getIban());
    }

    @Test
    public void testChargeSepaDirectDebitGuaranteed() throws HttpCommunicationException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/sepa-direct-debit-guaranteed", unzer.getPrivateKey(), new SepaDirectDebitSecured("DE89370400440532013000"));
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        SepaDirectDebitSecured sepaDirectDebitSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = sepaDirectDebitSecured.getId().matches("s-ddg-\\w*");
        assertTrue(matches);

        Charge charge = sepaDirectDebitSecured.charge(BigDecimal.TEN, Currency.getInstance("EUR"), unsafeUrl("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()));
        assertNotNull(charge.getPaymentId());
    }

    private SepaDirectDebitSecured getSepaDirectDebitSecured() {
        SepaDirectDebitSecured sdd = new SepaDirectDebitSecured("DE89370400440532013000");
        sdd.setBic("COBADEFFXXX");
        sdd.setHolder("Max Mustermann");
        return sdd;
    }

}
