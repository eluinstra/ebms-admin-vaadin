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

import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.function.SerializablePredicate;

import lombok.val;

public interface WithBinder
{
	default <T> Binder<T> createBinder(Class<T> c)
	{
		return new Binder<>(c);
	}

	default <T> Binder<T> createBinder(Class<T> c, T object)
	{
		val binder = createBinder(c);
		binder.setBean(object);
		return binder;
	}

	default Component bind(Binder<?> binder, AbstractField<?,?> component, String propertyName)
	{
		bind(binder,component,propertyName,t -> t);//identity()
		return component;
	}

	default <T> Component bind(Binder<?> binder, AbstractField<?,T> component, String propertyName, UnaryOperator<BindingBuilder<?,T>> builder)
	{
		builder.apply(binder.forField(component)).bind(propertyName);
		return component;
	}

	default Component bind(Binder<?> binder, AbstractCompositeField<?,?,?> component, String propertyName)
	{
		bind(binder,component,propertyName,t -> t);//identity()
		return component;
	}

	default <T> Component bind(Binder<?> binder, AbstractCompositeField<?,?,T> component, String propertyName, UnaryOperator<BindingBuilder<?,T>> builder)
	{
		builder.apply(binder.forField(component)).bind(propertyName);
		return component;
	}

	default Component bind(Binder<?> binder, AbstractCompositeField<?,?,LocalDateTime> from, AbstractCompositeField<?,?,LocalDateTime> to, String propertyName, SerializablePredicate<? super LocalDateTime> validator, String errorMessage)
	{
		val b = binder.forField(to)
				.withValidator(validator,errorMessage)
				.bind(propertyName);
		from.addValueChangeListener(event -> b.validate());
		return to;
	}

}
