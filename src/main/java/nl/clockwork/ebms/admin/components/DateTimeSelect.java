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
package nl.clockwork.ebms.admin.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

import lombok.val;

public class DateTimeSelect extends AbstractCompositeField<HorizontalLayout,DateTimeSelect,LocalDateTime> implements WithElement
{
	private static final LocalTime DEFAULT_TIME = LocalTime.MIDNIGHT;
	private DatePicker datePicker;
	private TimePicker timePicker;

	public DateTimeSelect(String dateLabel, String timeLabel, int colspan)
	{
		super(null);
		setColSpan(this,colspan);
		datePicker = createDatePicker(dateLabel);
		timePicker = createTimePicker(timeLabel);
		datePicker.addValueChangeListener(datePickerChangeListener());
		timePicker.addValueChangeListener(timePickerChangeListener());
		getContent().add(datePicker,timePicker);
	}

	private DatePicker createDatePicker(String label)
	{
		val result = new DatePicker();
		result.setSizeFull();
		result.setLabel(label);
		result.setClearButtonVisible(true);
		return result;
	}

	private TimePicker createTimePicker(String label)
	{
		val result = new TimePicker();
		result.setSizeFull();
		result.setLabel(label);
		result.setClearButtonVisible(true);
		return result;
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<DatePicker,LocalDate>> datePickerChangeListener()
	{
		return event ->
		{
			val date = datePicker.getValue();
			if (date != null)
			{
				setModelValue(LocalDateTime.of(date,DEFAULT_TIME),event.isFromClient());
				timePicker.setValue(DEFAULT_TIME);
			}
			else
				clear();
		};
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<TimePicker,LocalTime>> timePickerChangeListener()
	{
		return event ->
		{
			val date = datePicker.getValue();
			val time = timePicker.getValue();
			if (date != null)
				setModelValue(LocalDateTime.of(date,time == null ? DEFAULT_TIME : time),event.isFromClient());
		};
	}

	@Override
	protected void setPresentationValue(LocalDateTime value)
	{
		if (value == null)
		{
			datePicker.clear();
			timePicker.clear();
		}
		else
		{
			datePicker.setValue(value.toLocalDate());
			timePicker.setValue(value.toLocalTime());
		}
	}
}
