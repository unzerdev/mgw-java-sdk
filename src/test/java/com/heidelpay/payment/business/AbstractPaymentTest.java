package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.heidelpay.payment.Address;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Basket;
import com.heidelpay.payment.BasketItem;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Customer.Salutation;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.impl.HttpClientBasedRestCommunication;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;

public class AbstractPaymentTest {

	public Heidelpay getHeidelpay() {
		return new Heidelpay("s-priv-2a102ZMq3gV4I3zJ888J7RR6u75oqK3n");
//		return new Heidelpay("s-priv-6S59Dt6Q9mJYj8X5qpcxSpA3XLXUw4Zf");
	}
	public Heidelpay getHeidelpayDE() {
		return new Heidelpay("s-priv-2a102ZMq3gV4I3zJ888J7RR6u75oqK3n", Locale.GERMANY);
	}
	public Heidelpay getHeidelpay(String key) {
		return new Heidelpay(new HttpClientBasedRestCommunication(), key);
	}
	public Heidelpay getHeidelpayDE(String key) {
		return new Heidelpay(new HttpClientBasedRestCommunication(Locale.GERMANY), key);
	}

	protected Authorization getAuthorization(String typeId) throws MalformedURLException {
		return getAuthorization(typeId, (String)null);
	}
	protected Authorization getAuthorization(String typeId, Boolean card3ds) throws MalformedURLException {
		return getAuthorization(typeId, null, null, null, null, card3ds);
	}
	protected Authorization getAuthorization(String typeId, String customerId) throws MalformedURLException {
		return getAuthorization(typeId, customerId, null, null, null, null);
	}
	protected Authorization getAuthorization(String typeId, String customerId, String metadataId) throws MalformedURLException {
		return getAuthorization(typeId, customerId, null, metadataId, null, null);
	}
	protected Authorization getAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId) throws MalformedURLException {
		return getAuthorization(typeId, customerId, orderId, metadataId, basketId, null);
	}
	protected Authorization getAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) throws MalformedURLException {
		Authorization authorization = new Authorization();
		authorization
		.setAmount(new BigDecimal(10))
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(typeId)
		.setReturnUrl(new URL("https://www.heidelpay.com"))
		.setOrderId(orderId)
		.setCustomerId(customerId)
		.setMetadataId(metadataId)
		.setBasketId(basketId)
		.setCard3ds(card3ds);
		return authorization;
	}
	
	protected Charge getCharge() throws MalformedURLException, HttpCommunicationException {
		return getCharge(null);
	}
	protected Charge getCharge(String orderId) throws MalformedURLException, HttpCommunicationException {
		return getCharge(createPaymentTypeCard().getId(), null);
	}
	protected Charge getCharge(String orderId, Boolean card3ds) throws MalformedURLException, HttpCommunicationException {
		return getCharge(createPaymentTypeCard().getId(), null, orderId, null, null, card3ds);
	}
	protected Charge getCharge(String typeId, String customerId, String orderId, String metadataId, String basketId) throws MalformedURLException, HttpCommunicationException {
		return getCharge(typeId, customerId, orderId, metadataId, basketId, null);
	}
	protected Charge getCharge(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) throws MalformedURLException, HttpCommunicationException {
		Charge charge = new Charge();
		charge.setAmount(BigDecimal.ONE)
		.setCurrency(Currency.getInstance("EUR"))
		.setTypeId(typeId)
		.setReturnUrl(new URL("https://www.google.at"))
		.setOrderId(orderId)
		.setCustomerId(customerId)
		.setMetadataId(metadataId)
		.setBasketId(basketId)
		.setCard3ds(card3ds);
		return charge;
	}
	
	protected Card createPaymentTypeCard() throws HttpCommunicationException {
		Card card = getPaymentTypeCard();
		card = (Card)getHeidelpay().createPaymentType(card);
		return card;
	}
	protected InvoiceGuaranteed createPaymentTypeInvoiceGuaranteed() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = new InvoiceGuaranteed();
		invoice = (InvoiceGuaranteed)getHeidelpay().createPaymentType(invoice);
		return invoice;
	}

	protected Card getPaymentTypeCard() {
		Card card = new Card("4444333322221111", "03/20");
		card.setCvc("123");
		return card;
	}

	protected String getRandomId() {
		return UUID.randomUUID().toString();
	}

	protected Customer createMaximumCustomer() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
	}

	protected Customer createFactoringOKCustomer() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createCustomer(getFactoringOKCustomer(getRandomId()));
	}

	protected Customer createMaximumCustomerSameAddress() throws HttpCommunicationException, ParseException {
		return getHeidelpay().createCustomer(getMaximumCustomerSameAddress(getRandomId()));
	}

	protected Customer getMinimumCustomer() {
		return new Customer("Rene", "Felder"); 
	}

	protected Customer getMaximumCustomerSameAddress(String customerId) throws ParseException {
		Customer customer = new Customer("Rene", "Felder");
		customer
		.setCustomerId(customerId)
		.setSalutation(Salutation.mr)
		.setEmail("info@heidelpay.com")
		.setMobile("+43676123456")
		.setBirthDate(getDate("03.10.1974"))
		.setBillingAddress(getAddress())
		.setShippingAddress(getAddress());
		return customer;
	}

	protected Customer getMaximumCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("Rene", "Felder");
		customer
		.setCustomerId(customerId)
		.setSalutation(Salutation.mr)
		.setEmail("info@heidelpay.com")
		.setMobile("+43676123456")
		.setBirthDate(getDate("03.10.1974"))
		.setBillingAddress(getAddress())
		.setShippingAddress(getAddress("Schubert", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"));
		return customer;
	}

	protected Customer getFactoringOKCustomer(String customerId) throws ParseException {
		Customer customer = new Customer("Maximilian", "Mustermann");
		customer
						.setCustomerId(customerId)
						.setSalutation(Salutation.mr)
						.setBirthDate(getDate("22.11.1980"))
						.setBillingAddress(getFactoringOKAddress())
						.setShippingAddress(getFactoringOKAddress());
		return customer;
	}

	private Address getFactoringOKAddress() {
		return getAddress("Maximilian Mustermann", "Hugo-Junkers-Str. 3", "Frankfurt am Main", "Frankfurt am Main", "60386", "DE");
	}

	private Address getAddress() {
		return getAddress("Mozart", "Grüngasse 16", "Vienna", "Vienna", "1010", "AT");
	}
	private Address getAddress(String name, String street, String city, String state, String zip, String country) {
		Address address = new Address();
		address
		.setName(name)
		.setStreet(street)
		.setCity(city)
		.setState(state)
		.setZip(zip)
		.setCountry(country);
		return address;
	}

	protected Metadata createTestMetadata() throws PaymentException, HttpCommunicationException {
		Metadata metadata = getTestMetadata();
		metadata = getHeidelpay().createMetadata(metadata);
		return metadata;

	}


	protected Metadata getTestMetadata() {
		return getTestMetadata(false);
	}
	protected Metadata getTestMetadata(boolean sorted) {
		Metadata metadata = new Metadata(sorted)
				.addMetadata("invoice-nr", "Rg-2018-11-1")
				.addMetadata("shop-id", "4711")
				.addMetadata("delivery-date", "24.12.2018")
				.addMetadata("reason", "X-mas present");
		return metadata;
	}



	protected Date getDate(String date) throws ParseException {
		return new SimpleDateFormat("dd.MM.yy").parse(date);
	}

	protected void assertMapEquals(Map<String, String> testMetadataMap, Map<String, String> metadataMap) {
		for (Map.Entry<String, String> entry : metadataMap.entrySet()) {
			assertEquals(entry.getValue(), testMetadataMap.get(entry.getKey()));
		}
	}


	protected void assertChargeEquals(Charge initCharge, Charge charge) {
		assertEquals(initCharge.getAmount(), charge.getAmount());
		assertEquals(initCharge.getCurrency(), charge.getCurrency());
		assertEquals(initCharge.getCustomerId(), charge.getCustomerId());
		assertEquals(initCharge.getId(), charge.getId());
		assertEquals(initCharge.getPaymentId(), charge.getPaymentId());
		assertEquals(initCharge.getReturnUrl(), charge.getReturnUrl());
		assertEquals(initCharge.getRiskId(), charge.getRiskId());
		assertEquals(initCharge.getTypeId(), charge.getTypeId());
		assertEquals(initCharge.getTypeUrl(), charge.getTypeUrl());
		assertEquals(initCharge.getCancelList(), charge.getCancelList());
		assertProcessingEquals(initCharge.getProcessing(), charge.getProcessing());
	}

	private void assertProcessingEquals(Processing initProcessing, Processing processing) {
		assertEquals(initProcessing.getShortId(), processing.getShortId());
		assertEquals(initProcessing.getUniqueId(), processing.getUniqueId());
	}

	protected void assertCustomerEquals(Customer customerExpected, Customer customer) {
		assertEquals(customerExpected.getFirstname(), customer.getFirstname());
		assertEquals(customerExpected.getLastname(), customer.getLastname());
		assertEquals(customerExpected.getCustomerId(), customer.getCustomerId());
		assertEquals(customerExpected.getBirthDate(), customer.getBirthDate());
		assertEquals(customerExpected.getEmail(), customer.getEmail());
		assertEquals(customerExpected.getMobile(), customer.getMobile());
		assertEquals(customerExpected.getPhone(), customer.getPhone());
		assertAddressEquals(customerExpected.getBillingAddress(), customer.getBillingAddress());		
		assertAddressEquals(customerExpected.getShippingAddress(), customer.getShippingAddress());		
	}
	protected void assertAddressEquals(Address addressExpected, Address address) {
		if (addressExpected == null) return;
		assertEquals(addressExpected.getCity(), address.getCity());
		assertEquals(addressExpected.getCountry(), address.getCountry());
		assertEquals(addressExpected.getName(), address.getName());
		assertEquals(addressExpected.getState(), address.getState());
		assertEquals(addressExpected.getStreet(), address.getStreet());
		assertEquals(addressExpected.getZip(), address.getZip());
	}

	protected void assertCancelEquals(Cancel cancelInit, Cancel cancel) {
		assertEquals(cancelInit.getAmount(), cancel.getAmount());
		assertEquals(cancelInit.getId(), cancel.getId());
		assertEquals(cancelInit.getTypeUrl(), cancel.getTypeUrl());
	}


	protected BigDecimal getBigDecimal(String number) {
		BigDecimal bigDecimal = new BigDecimal(number);
		bigDecimal.setScale(4);
		return bigDecimal;
	}

	protected String maskIban(String text) {
		return maskString(text, 6, text.length()-4, '*');
	}

	protected static String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null) return null;
		if (strText.equals("")) return "";
		if (start < 0) start = 0;
		if (end > strText.length()) end = strText.length();

		int maskLength = end - start;

		if (maskLength == 0) return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
	}

	protected Basket getMaxTestBasket() {
		Basket basket = new Basket();
		basket.setAmountTotal(new BigDecimal(866.49));
		basket.setAmountTotalDiscount(BigDecimal.TEN);
		basket.setCurrencyCode(Currency.getInstance("EUR"));
		basket.setNote("Mistery shopping");
		basket.setOrderId(getRandomId());
		basket.addBasketItem(getMaxTestBasketItem1());
		basket.addBasketItem(getMaxTestBasketItem2());
		return basket;
	}

	protected Basket getMinTestBasket() {
		Basket basket = new Basket()
				.setAmountTotal(new BigDecimal(500.5))
				.setCurrencyCode(Currency.getInstance("EUR"))
				.setOrderId(getRandomId())
				.addBasketItem(getMinTestBasketItem());
		return basket;
	}
	private BasketItem getMaxTestBasketItem1() {
		BasketItem basketItem = new BasketItem();
		basketItem.setBasketItemReferenceId("Artikelnummer4711");
		basketItem.setAmountDiscount(BigDecimal.ONE);
		basketItem.setAmountGross(new BigDecimal(500.5));
		basketItem.setAmountNet(new BigDecimal(420.1));
		basketItem.setAmountPerUnit(new BigDecimal(100.1));
		basketItem.setAmountVat(new BigDecimal(80.4));
		basketItem.setQuantity(5);
		basketItem.setTitle("Apple iPhone");
		basketItem.setUnit("Pc.");
		basketItem.setVat(19);
		basketItem.setSubTitle("XS in Red");
		try {
			basketItem.setImageUrl(new URL("https://www.apple.com/v/iphone-xs/d/images/overview/hero_top_device_large_2x.jpg"));
		} catch (MalformedURLException e) {
		}
		return basketItem;
	}
	private BasketItem getMaxTestBasketItem2() {
		BasketItem basketItem = new BasketItem();
		basketItem.setBasketItemReferenceId("Artikelnummer4712");
		basketItem.setAmountDiscount(BigDecimal.ONE);
		basketItem.setAmountGross(new BigDecimal(365.99));
		basketItem.setAmountNet(new BigDecimal(101.66));
		basketItem.setAmountPerUnit(new BigDecimal(121.99));
		basketItem.setAmountVat(new BigDecimal(20.33));
		basketItem.setQuantity(3);
		basketItem.setTitle("Apple iPad Air");
		basketItem.setUnit("Pc.");
		basketItem.setVat(20);
		basketItem.setSubTitle("Nicht nur Pros brauchen Power.");
		try {
			basketItem.setImageUrl(new URL("https://www.apple.com/de/ipad-air/images/overview/hero__gmn7i7gbziqa_large_2x.jpg"));
		} catch (MalformedURLException e) {
		}
		return basketItem;
	}

	private BasketItem getMinTestBasketItem() {
		BasketItem basketItem = new BasketItem()
				.setBasketItemReferenceId("Artikelnummer4711")
				.setQuantity(5)
				.setAmountPerUnit(new BigDecimal(100.1))
				.setAmountNet(new BigDecimal(420.1))
				.setTitle("Apple iPhone");
		return basketItem;
	}

}
