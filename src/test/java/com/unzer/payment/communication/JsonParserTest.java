package com.unzer.payment.communication;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.json.JsonCharge;
import com.unzer.payment.communication.json.JsonErrorObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest extends AbstractPaymentTest {

    @Test
    public void given_an_error_message_then_payment_exception_is_thrown() {
        try {
            JsonParser parser = new JsonParser();
            parser.fromJson(TestData.errorJson(), JsonCharge.class);

            fail("Expected PaymentException");
        } catch (PaymentException exception) {
            assertBasicErrorAttributes(exception.getId(), exception.getUrl(), exception.getTimestamp());
            assertPaymentError(exception.getPaymentErrorList().get(0));
        }
    }

    @Test
    public void given_an_error_json_then_fromJson_returnes_jsonerrorobject() {
        assertJsonError(new JsonParser().fromJson(TestData.errorJson(), JsonErrorObject.class));
    }

    @Test
    public void testValidJson() {
        assertTrue(new JsonParser().isJsonValid("{\"name\": \"value\"}"));
    }

    @Test
    public void testInvalidJson() {
        assertFalse(new JsonParser().isJsonValid("This is an error message!"));
    }

    @Test
    public void testNullJson() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonParser().fromJson(null, Payment.class);
        });
    }

    @Test
    public void testNullClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonParser().fromJson("{\"name\": \"value\"}", null);
        });
    }

    @Test
    public void testNullValidJson() {
        assertFalse(new JsonParser().isJsonValid(null));
    }

    @Test
    public void testFromInvalidJson() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonParser().fromJson("This is an error message!", Payment.class);
        });
    }

    private void assertJsonError(JsonErrorObject expectedError) {
        assertBasicErrorAttributes(expectedError.getId(), expectedError.getUrl(), expectedError.getTimestamp());
        assertEquals(1, expectedError.getErrors().size());
        assertPaymentError(expectedError.getErrors().get(0));
    }

    private void assertBasicErrorAttributes(String id, String url, String timestamp) {
        assertEquals("s-err-f2ea241e5e8e4eb3b1513fab12c", id);
        assertEquals("https://api.unzer.com/v1/payments/charges", url);
        assertEquals("2019-01-09 15:42:24", timestamp);

    }

    private void assertPaymentError(PaymentError error) {
        assertEquals("COR.400.100.101", error.getCode());
        assertEquals("Address untraceable", error.getMerchantMessage());
        assertEquals("The provided address is invalid. Please check your input and try agian.",
                error.getCustomerMessage());
    }
}
