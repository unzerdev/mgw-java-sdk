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

package com.unzer.payment.communication.json.paylater;

import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.util.Date;

public class JsonInstallmentPlanRate extends JsonIdObject implements JsonObject {

  private Date date;
  private BigDecimal rate;

  public Date getDate() {
    return date;
  }

  public JsonInstallmentPlanRate setDate(Date date) {
    this.date = date;
    return this;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public JsonInstallmentPlanRate setRate(BigDecimal rate) {
    this.rate = rate;
    return this;
  }
}
