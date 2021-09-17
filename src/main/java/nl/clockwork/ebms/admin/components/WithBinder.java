package nl.clockwork.ebms.admin.components;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

public interface WithBinder
{
	default <T> Binder<T> createBinder(Class<T> c)
	{
		return new Binder<>(c);
	}

	default Component bind(Binder<?> binder, AbstractField<?,?> component, String propertyName)
	{
		binder.bind(component, propertyName);
		return component;
	}

	default Component bind(Binder<?> binder, AbstractCompositeField<?,?,?> component, String propertyName)
	{
		binder.bind(component, propertyName);
		return component;
	}
}
