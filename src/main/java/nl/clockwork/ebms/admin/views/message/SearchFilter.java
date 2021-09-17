package nl.clockwork.ebms.admin.views.message;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import org.oasis_open.committees.ebxml_cppa.schema.cpp_cpa_2_0.CollaborationProtocolAgreement;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nl.clockwork.ebms.admin.CPAUtils;
import nl.clockwork.ebms.admin.components.PartySelect;
import nl.clockwork.ebms.admin.components.WithBinder;
import nl.clockwork.ebms.admin.components.WithElement;
import nl.clockwork.ebms.admin.dao.EbMSDAO;
import nl.clockwork.ebms.admin.views.BeanProvider;
import nl.clockwork.ebms.jaxb.JAXBParser;
import nl.clockwork.ebms.service.model.MessageFilter;
import nl.clockwork.ebms.service.model.Party;

@Slf4j
public class SearchFilter extends HorizontalLayout implements WithBinder, WithElement
{
	public SearchFilter(EbMSMessageFilter messageFilter)
	{
		this(BeanProvider.getEbMSAdminDAO(),messageFilter);
	}

	public SearchFilter(EbMSDAO ebMSDAO, EbMSMessageFilter messageFilter)
	{
		val binder = createBinder(MessageFilter.class);
		binder.setBean(messageFilter);
		val cpaSelect = createComboBox(getTranslation("lbl.cpaId"),ebMSDAO.selectCPAIds());
		setColSpan(cpaSelect,2);
		val fromPartySelect = new PartySelect(getTranslation("lbl.fromPartyId"),getTranslation("lbl.fromRole"));
		setColSpan(fromPartySelect,2);
		val toPartySelect = new PartySelect(getTranslation("lbl.toPartyId"),getTranslation("lbl.toRole"));
		setColSpan(toPartySelect,2);
		val serviceSelect = createComboBox(getTranslation("lbl.service"),Collections.emptyList());
		val actionSelect = createComboBox(getTranslation("lbl.action"),Collections.emptyList());
		val conversationId = createTextField(getTranslation("lbl.conversationId"));
		setColSpan(conversationId,2);
		val messageId = createTextField(getTranslation("lbl.messageId"));
		val refToMessageId = createTextField(getTranslation("lbl.refToMessageId"));
		cpaSelect.addValueChangeListener(cpaSelectChangeListener(ebMSDAO,cpaSelect,fromPartySelect,toPartySelect));
		fromPartySelect.addValueChangeListener(partySelectChangeListener(ebMSDAO,cpaSelect,fromPartySelect,toPartySelect,serviceSelect));
		toPartySelect.addValueChangeListener(partySelectChangeListener(ebMSDAO,cpaSelect,toPartySelect,fromPartySelect,serviceSelect));
		serviceSelect.addValueChangeListener(serviceSelectChangeListener(ebMSDAO,messageFilter,actionSelect));
		val form = new FormLayout(
				bind(binder,cpaSelect,"cpaId"),
				bind(binder,fromPartySelect,"fromParty"),
				bind(binder,toPartySelect,"toParty"),
				bind(binder,serviceSelect,"service"),
				bind(binder,actionSelect,"action"),
				conversationId,
				messageId,
				refToMessageId);
		form.setSizeFull();
		add(form);
	}

	private ComboBox<String> createComboBox(String label, List<String> items)
	{
		val result = new ComboBox<String>();
		result.setLabel(label);
		result.setItems(items);
		result.setClearButtonVisible(true);
		result.setEnabled(!items.equals(Collections.emptyList()));
		return result;
	}

	private TextField createTextField(String label)
	{
		TextField result = new TextField(label);
		result.setEnabled(false);
		return result;
	}

	private ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<String>,String>> cpaSelectChangeListener(EbMSDAO ebMSDAO, ComboBox<String> cpaComboBox, PartySelect fromPartySelect, PartySelect toPartySelect)
	{
		return event ->
		{
			try
			{
				val value = cpaComboBox.getValue() == null ? null : ebMSDAO.findCPA(cpaComboBox.getValue());
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

	private ValueChangeListener<? super ComponentValueChangeEvent<PartySelect,Party>> partySelectChangeListener(EbMSDAO ebMSDAO, ComboBox<String> cpaComboBox, final PartySelect party1, final PartySelect party2, ComboBox<String> serviceSelect)
	{
		return event -> 
		{
			try
			{
				party2.setEnabled(party1.isEmpty());
				if (!party1.isEmpty() && party1.getValue().getRole() != null)
				{
					val value = cpaComboBox.getValue() == null ? null : ebMSDAO.findCPA(cpaComboBox.getValue());
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

	private ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<String>,String>> serviceSelectChangeListener(EbMSDAO ebMSDAO, EbMSMessageFilter messageFilter, ComboBox<String> actionSelect)
	{
		return event ->
		{
			try
			{
				if (event.getValue() != null)
				{
					val value = messageFilter.getCpaId() == null ? null : ebMSDAO.findCPA(messageFilter.getCpaId());
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
