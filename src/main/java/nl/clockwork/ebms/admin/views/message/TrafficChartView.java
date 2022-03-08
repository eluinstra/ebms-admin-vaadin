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

import static nl.clockwork.ebms.admin.views.message.TrafficChartConfig.createChartTitle;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.Labels;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.NonNull;
import lombok.val;
import nl.clockwork.ebms.admin.components.WithBinder;
import nl.clockwork.ebms.admin.components.WithElement;
import nl.clockwork.ebms.admin.views.MainLayout;
import nl.clockwork.ebms.admin.views.WithBean;

@Route(value = "trafficChart", layout = MainLayout.class)
@PageTitle("Traffic Chart")
public class TrafficChartView extends VerticalLayout implements WithBean, WithBinder, WithElement
{
	private final TrafficChartConfig config = TrafficChartConfig.of(TimeUnit.DAY,EbMSMessageTrafficChartOption.ALL);
	private Component chart = updateApexCharts(createDefaultApexCharts(),config);

	public TrafficChartView()
	{
		setSizeFull();
		add(new H1(getTranslation("trafficChart")));
		val binder = createBinder(TrafficChartConfig.class,config);
		add(
				createDateBar(config),
				chart,
				createChartBar(config));
	}

	private Component createDateBar(TrafficChartConfig config)
	{
		val result = new HorizontalLayout();
		result.setWidthFull();
		result.setAlignItems(Alignment.STRETCH);
		result.add(createButton(new Icon(VaadinIcon.STEP_BACKWARD),event ->
		{
			config.previousPeriod();
			updateChart(config);
		}));
		result.add(/* bind(binder, */createComboBox(Arrays.asList(TimeUnit.values()),config.getTimeUnit(),event ->
		{
			config.setTimeUnit(event.getValue()); // FIXME
			config.resetFrom();
			updateChart(config);
		})/* ,"timeUnit") */);
		result.add(createButton(new Icon(VaadinIcon.STEP_FORWARD),event ->
		{
			config.nextPeriod();
			updateChart(config);
		}));
		return result;
	}

	private Component createButton(Icon icon, ComponentEventListener<ClickEvent<Icon>> clickListener)
	{
		icon.addClickListener(clickListener);
		return icon;
	}

	// private <T> ComboBox<T> createComboBox(List<T> items, ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<T>,T>> changeListener)
	// {
	// 	val result = new ComboBox<T>();
	// 	result.setItems(items);
	// 	result.addValueChangeListener(changeListener);
	// 	return result;
	// }

	private <T> ComboBox<T> createComboBox(List<T> items, T value, ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<T>,T>> changeListener)
	{
		val result = new ComboBox<T>();
		result.setItems(items);
		result.setValue(value);
		result.addValueChangeListener(changeListener);
		return result;
	}

	private void updateChart(TrafficChartConfig config)
	{
		val newChart = updateApexCharts(createDefaultApexCharts(),config);
		TrafficChartView.this.replace(chart,newChart);
		chart = newChart;
	}

	private ApexCharts createDefaultApexCharts()
	{
		return new ApexChartsBuilder()
				.withChart(ChartBuilder.get()
						.withType(Type.line)
						.withZoom(ZoomBuilder.get()
								.withEnabled(false)
								.build())
						.build())
				.withLegend(LegendBuilder.get()
						.withLabels(createLabels())
						.withPosition(Position.top)
						.build())
				.withYaxis(YAxisBuilder.get()
						.withTitle(com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder.get()
								.withText("Messages")
								.build())
						.build())
				.build();
	}

	private Labels createLabels()
	{
		val result = new Labels();
		result.setUseSeriesColors(true);
		return result;
	}

	private ApexCharts updateApexCharts(ApexCharts charts, TrafficChartConfig config)
	{
		val dateStrings = getDateStrings(config);
		charts.setTitle(TitleSubtitleBuilder.get()
				.withText(createChartTitle(config))
				.withAlign(Align.center)
				.build());
		charts.setStroke(StrokeBuilder.get()
				.withCurve(Curve.straight)
				.withColors(getColors(config.getEbMSMessageTrafficChartOption()))
				.build());
		charts.setXaxis(XAxisBuilder.get()
				.withCategories(dateStrings)
				.withTitle(TitleBuilder.get()
						.withText(config.getTimeUnit().getUnits())
						.build())
				.build());
		charts.updateSeries(createSeries(config.getEbMSMessageTrafficChartOption().getEbMSMessageTrafficChartSeries(),serie -> getMessages(config,dateStrings,serie)));
		return charts;
	}

	private List<String> getDateStrings(TrafficChartConfig config)
	{
		val dates = calculateDates(config.getTimeUnit().getTimeUnit(),config.getFrom(),config.getTo());
		return dates.stream().map(date -> config.getTimeUnit().getTimeUnitDateFormat().format(date)).collect(Collectors.toList());
	}

	private String[] getColors(@NonNull EbMSMessageTrafficChartOption trafficChartOption)
	{
		return Arrays.stream(trafficChartOption.getEbMSMessageTrafficChartSeries()).map(EbMSMessageTrafficChartSerie::getColor).toArray(String[]::new);
	}

	@SuppressWarnings("unchecked")
	private Series<Integer>[] createSeries(EbMSMessageTrafficChartSerie[] series, Function<EbMSMessageTrafficChartSerie,Integer[]> dataBySerie)
	{
		return Arrays.stream(series).map(serie -> createSerie(serie.getName(),() -> dataBySerie.apply(serie))).toArray(Series[]::new);
	}

	private Series<Integer> createSerie(String name, Supplier<Integer[]> dataSupplier)
	{
		val result = new Series<Integer>();
		result.setName(name);
		result.setData(dataSupplier.get());
		return result;
	}

	private Integer[] getMessages(TrafficChartConfig config, List<String> dates, EbMSMessageTrafficChartSerie serie)
	{
		val messageTraffic = getEbMSAdminDAO().selectMessageTraffic(config.from,config.getTo(),config.timeUnit,serie.getEbMSMessageStatuses());
		return dates.stream().map(date -> messageTraffic.getOrDefault(Integer.parseInt(date),0)).toArray(Integer[]::new);
	}

	private List<LocalDateTime> calculateDates(TemporalAmount period, LocalDateTime from, LocalDateTime to)
	{
		val dates = new ArrayList<LocalDateTime>();
		while (from.isBefore(to))
		{
			dates.add(from);
			from = from.plus(period);
		}
		return dates;
	}

	private Component createChartBar(TrafficChartConfig config)
	{
		val result = new HorizontalLayout();
		result.setWidthFull();
		result.setAlignItems(Alignment.END);
		result.add(/* bind(binder, */createComboBox(Arrays.asList(EbMSMessageTrafficChartOption.values()),config.getEbMSMessageTrafficChartOption(),event ->
		{
			config.setEbMSMessageTrafficChartOption(event.getValue()); // FIXME
			updateChart(config);
		})/* ,"ebMSMessageTrafficChartOption") */);
		return result;
	}

}
