package com.unzer.payment.marketplace;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - 2021 Unzer E-Com GmbH
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

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.util.List;

public class MarketplaceAuthorization extends AbstractTransaction<MarketplacePayment> {
	
	private List<MarketplaceCancel> cancelList;
	
	public MarketplaceAuthorization() {
		super();
	}

	public MarketplaceAuthorization(Unzer unzer) {
		super(unzer);
	}

	@Override
	public String getTypeUrl() {
		return "marketplace/payments/<paymentId>/authorize";
	}

	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
		return null;
	}
	
	public List<MarketplaceCancel> getCancelList() {
		return cancelList;
	}

	public void setCancelList(List<MarketplaceCancel> cancelList) {
		this.cancelList = cancelList;
	}

	public MarketplaceCancel getCancel(String cancelId) {
		if (cancelList == null) return null;
		for (MarketplaceCancel cancel : cancelList) {
			if (cancelId.equalsIgnoreCase(cancel.getId())) {
				return cancel;
			}
		}
		return null;
	}
	
	public MarketplaceCharge charge(MarketplaceCharge charge) throws HttpCommunicationException {
		return getUnzer().marketplaceChargeAuthorization(this.getPaymentId(), this.getId(), charge);
	}
	
	/**
	 * Cancel for this authorization.
	 * 
	 * @param cancel refers to MarketplaceCancel.
	 * @return MarketplaceCancel
	 * @throws HttpCommunicationException
	 */
	public MarketplaceCancel cancel(MarketplaceCancel cancel) throws HttpCommunicationException {
		return getUnzer().marketplaceAuthorizationCancel(this.getPaymentId(), this.getId(), cancel);
	}
}
