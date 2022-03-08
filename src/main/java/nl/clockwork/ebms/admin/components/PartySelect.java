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

import java.util.Collections;
import java.util.List;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import org.oasis_open.committees.ebxml_cppa.schema.cpp_cpa_2_0.CollaborationProtocolAgreement;

import lombok.Builder;
import lombok.val;
import nl.clockwork.ebms.admin.CPAUtils;
import nl.clockwork.ebms.service.model.Party;

public class PartySelect extends AbstractCompositeField<HorizontalLayout,PartySelect,Party> implements WithElement
{
	private CollaborationProtocolAgreement cpa;
	private ComboBox<String> partyIdSelect;
	private ComboBox<String> roleSelect;

	@Builder
	public PartySelect(String partyIdLabel, String roleLabel, int colspan)
	{
		super(null);
		setColSpan(this,colspan);
		partyIdSelect = createComboBox(partyIdLabel,Collections.emptyList());
		roleSelect = createComboBox(roleLabel,Collections.emptyList());
		partyIdSelect.addValueChangeListener(partyIdSelectChangeListener());
		roleSelect.addValueChangeListener(roleSelectChangeListener());
		getContent().add(partyIdSelect,roleSelect);
	}

	private ComboBox<String> createComboBox(String label, List<String> items)
	{
		val result = new ComboBox<String>();
		result.setSizeFull();
		result.setLabel(label);
		result.setItems(items);
		result.setEnabled(false);
		result.setClearButtonVisible(true);
		return result;
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<String>,String>> partyIdSelectChangeListener()
	{
		return event ->
		{
			val partyId = partyIdSelect.getValue();
			if (partyId != null)
				setModelValue(new Party(partyId,null),event.isFromClient());
			else
				clear();
			val isEnabled = cpa != null && partyIdSelect.getValue() != null;
			roleSelect.setItems(isEnabled ? CPAUtils.getRoleNames(cpa,partyIdSelect.getValue()) : Collections.emptyList());
			roleSelect.setEnabled(isEnabled);
		};
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<String>,String>> roleSelectChangeListener()
	{
		return event ->
		{
			val partyId = partyIdSelect.getValue();
			val role = roleSelect.getValue();
			if (partyId != null && role != null)
				setModelValue(new Party(partyId,role),event.isFromClient());
			// else
			// 	// clear();
			// 	setModelValue(null,event.isFromClient());
		};
	}

	public void updateState(CollaborationProtocolAgreement cpa)
	{
		if (this.cpa != cpa)
		{
			this.cpa = cpa;
			reset(cpa);
		}
	}

	private void reset(CollaborationProtocolAgreement cpa)
	{
		setPartIdSelect(cpa);
		disable(roleSelect);
	}

	private void setPartIdSelect(CollaborationProtocolAgreement cpa)
	{
		val isEnabled = cpa != null;
		partyIdSelect.setItems(isEnabled ? CPAUtils.getPartyIds(cpa) : Collections.emptyList());
		partyIdSelect.setEnabled(isEnabled);
	}

	private void disable(ComboBox<String> comboBox)
	{
		comboBox.setItems(Collections.emptyList());
		comboBox.setEnabled(false);
	}

	@Override
	protected void setPresentationValue(Party value)
	{
		if (value == null)
		{
			partyIdSelect.clear();
			roleSelect.clear();
		}
		else
		{
			partyIdSelect.setValue(value.getPartyId());
			roleSelect.setValue(value.getRole());
		}
	}
}
