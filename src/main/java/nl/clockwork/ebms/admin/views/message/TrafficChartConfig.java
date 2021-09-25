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