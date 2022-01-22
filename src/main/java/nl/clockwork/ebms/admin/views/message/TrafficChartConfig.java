/*
 * Copyright 2021 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.clockwork.ebms.admin.views.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.val;

@Data
public class TrafficChartConfig implements Serializable
{
	@NonNull
	TimeUnit timeUnit;
	@NonNull
	LocalDateTime from;
	@NonNull
	EbMSMessageTrafficChartOption ebMSMessageTrafficChartOption;
	
	public static String createChartTitle(TrafficChartConfig config)
	{
		return config.getEbMSMessageTrafficChartOption().getTitle() + " " + config.timeUnit.getDateFormatter().format(config.getFrom());
	}

	public List<TimeUnit> getTimeUnits()
	{
		return Arrays.asList(TimeUnit.values());
	}
	public LocalDateTime getTo()
	{
		return from.plus(timeUnit.getPeriod());
	}
	public List<EbMSMessageTrafficChartOption> getEbMSMessageTrafficChartOptions()
	{
		return Arrays.asList(EbMSMessageTrafficChartOption.values());
	}

  public void previousPeriod()
  {
		from = from.minus(timeUnit.getPeriod());
  }

	public void nextPeriod()
	{
		from = from.plus(timeUnit.getPeriod());
	}

	public void resetFrom()
	{
		from = timeUnit.getResetTime().apply(from);
	}

	public static TrafficChartConfig of(TimeUnit timeUnit, EbMSMessageTrafficChartOption ebMSMessageTrafficChartOption)
	{
		val from = timeUnit.getFrom();
		return new TrafficChartConfig(timeUnit,from,ebMSMessageTrafficChartOption);
	}

}