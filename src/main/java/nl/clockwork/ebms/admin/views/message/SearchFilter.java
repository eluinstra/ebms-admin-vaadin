package nl.clockwork.ebms.admin.views.message;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import org.oasis_open.committees.ebxml_cppa.schema.cpp_cpa_2_0.CollaborationProtocolAgreement;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nl.clockwork.ebms.EbMSMessageStatus;
import nl.clockwork.ebms.admin.CPAUtils;
import nl.clockwork.ebms.admin.components.DateTimeSelect;
import nl.clockwork.ebms.admin.components.PartySelect;
import nl.clockwork.ebms.admin.components.WithBinder;
import nl.clockwork.ebms.admin.components.WithElement;
import nl.clockwork.ebms.admin.views.WithBean;
import nl.clockwork.ebms.jaxb.JAXBParser;
import nl.clockwork.ebms.service.model.Party;

@Slf4j
public class SearchFilter extends HorizontalLayout implements WithBean, WithBinder, WithElement
{
	public SearchFilter(EbMSMessageFilter messageFilter)
	{
		val binder = createBinder(EbMSMessageFilter.class,messageFilter);
		val cpa = createComboBox(getTranslation("lbl.cpaId"),getEbMSAdminDAO().selectCPAIds(),2);
		val fromParty = new PartySelect(getTranslation("lbl.fromPartyId"),getTranslation("lbl.fromRole"),2);
		val toParty = new PartySelect(getTranslation("lbl.toPartyId"),getTranslation("lbl.toRole"),2);
		val service = createComboBox(getTranslation("lbl.service"),Collections.emptyList(),1);
		val action = createComboBox(getTranslation("lbl.action"),Collections.emptyList(),1);
		val conversationId = createTextField(getTranslation("lbl.conversationId"),1);
		val messageId = createTextField(getTranslation("lbl.messageId"),1);
		val refToMessageId = createTextField(getTranslation("lbl.refToMessageId"),1);
		val statuses = createStatuses(1);
		// val from = createDateTimePicker(getTranslation("lbl.from"),1);
		val from = new DateTimeSelect(getTranslation("lbl.fromDate"),getTranslation("lbl.fromTime"),1);
		// val to = createDateTimePicker(getTranslation("lbl.to"),1);
		val to = new DateTimeSelect(getTranslation("lbl.toDate"),getTranslation("lbl.toTime"),1);
		cpa.addValueChangeListener(cpaSelectChangeListener(cpa,fromParty,toParty));
		fromParty.addValueChangeListener(partySelectChangeListener(cpa,fromParty,toParty,service));
		toParty.addValueChangeListener(partySelectChangeListener(cpa,toParty,fromParty,service));
		service.addValueChangeListener(serviceSelectChangeListener(messageFilter,action));
		val form = new FormLayout(
				bind(binder,cpa,"cpaId"),
				bind(binder,fromParty,"fromParty"),
				bind(binder,toParty,"toParty"),
				bind(binder,service,"service"),
				bind(binder,action,"action"),
				bind(binder,conversationId,"conversationId",builder -> builder.withNullRepresentation("")),
				bind(binder,messageId,"messageId",builder -> builder.withNullRepresentation("")),
				bind(binder,refToMessageId,"refToMessageId",builder -> builder.withNullRepresentation("")),
				bind(binder,statuses,"statuses"),
				bind(binder,from,"from"),
				bind(binder,from,to,"to",t -> t == null || from.getValue() == null || !t.isBefore(from.getValue()),"to must be after from"));
		form.setSizeFull();
		add(form);
	}

	private ComboBox<String> createComboBox(String label, List<String> items, int colspan)
	{
		val result = new ComboBox<String>();
		setColSpan(result,colspan);
		result.setLabel(label);
		result.setItems(items);
		result.setClearButtonVisible(true);
		result.setEnabled(!items.equals(Collections.emptyList()));
		return result;
	}

	private TextField createTextField(String label, int colspan)
	{
		val result = new TextField(label);
		setColSpan(result,colspan);
		result.setClearButtonVisible(true);
		return result;
	}

	private MultiSelectListBox<EbMSMessageStatus> createStatuses(int colspan)
	{
		val result = new MultiSelectListBox<EbMSMessageStatus>();
		setColSpan(result,colspan);
		result.setHeight("11em");
		result.setItems(EbMSMessageStatus.values());
		// result.getElement().setAttribute("size","5");
		return result;
	}

	private DateTimePicker createDateTimePicker(String label, int colspan)
	{
		val result = new DateTimePicker();
		setColSpan(result,colspan);
		result.setLabel(label);
		return result;
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<String>,String>> cpaSelectChangeListener(ComboBox<String> cpaComboBox, PartySelect fromPartySelect, PartySelect toPartySelect)
	{
		return event ->
		{
			try
			{
				val value = cpaComboBox.getValue() == null ? null : getEbMSAdminDAO().findCPA(cpaComboBox.getValue());
				val cpa = value == null ? null : JAXBParser.getInstance(CollaborationProtocolAgreement.class).handleUnsafe(value.getCpa());
				fromPartySelect.updateState(cpa);
				toPartySelect.updateState(cpa);
			}
			catch (JAXBException e)
			{
				log.error("",e);
			}
		};
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<PartySelect,Party>> partySelectChangeListener(ComboBox<String> cpaComboBox, final PartySelect party1, final PartySelect party2, ComboBox<String> serviceSelect)
	{
		return event -> 
		{
			try
			{
				party2.setEnabled(party1.isEmpty());
				if (!party1.isEmpty() && party1.getValue().getRole() != null)
				{
					val value = cpaComboBox.getValue() == null ? null : getEbMSAdminDAO().findCPA(cpaComboBox.getValue());
					val cpa = value == null ? null : JAXBParser.getInstance(CollaborationProtocolAgreement.class).handleUnsafe(value.getCpa());
					serviceSelect.setItems(CPAUtils.getServiceNames(cpa,party1.getValue().getRole()));
					serviceSelect.setEnabled(true);
				}
				else
				{
					serviceSelect.setItems(Collections.emptyList());
					serviceSelect.setEnabled(false);
				}
			}
			catch (JAXBException e)
			{
				log.error("",e);
			}
		};
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<String>,String>> serviceSelectChangeListener(EbMSMessageFilter messageFilter, ComboBox<String> actionSelect)
	{
		return event ->
		{
			try
			{
				if (event.getValue() != null)
				{
					val value = messageFilter.getCpaId() == null ? null : getEbMSAdminDAO().findCPA(messageFilter.getCpaId());
					val cpa = value == null ? null : JAXBParser.getInstance(CollaborationProtocolAgreement.class).handleUnsafe(value.getCpa());
					val actions = messageFilter.getFromParty() == null 
						? CPAUtils.getToActionNames(cpa,messageFilter.getToParty().getRole(),event.getValue())
						: CPAUtils.getFromActionNames(cpa,messageFilter.getFromParty().getRole(),event.getValue());
					actionSelect.setItems(actions);
					actionSelect.setEnabled(true);
				}
				else
				{
					actionSelect.setItems(Collections.emptyList());
					actionSelect.setEnabled(false);
				}
			}
			catch (JAXBException e)
			{
				log.error("",e);
			}
		};
	}

}
