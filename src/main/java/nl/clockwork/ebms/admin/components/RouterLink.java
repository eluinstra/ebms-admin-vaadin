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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteParameters;

public class RouterLink extends com.vaadin.flow.router.RouterLink
{
	public RouterLink (String text, Class<? extends Component> component)
	{
		super(text,component);
		getElement().getStyle().set("text-decoration","none");
	}

	public RouterLink (String text, Class<? extends Component> component, RouteParameters parameters)
	{
		super(text,component,parameters);
		getElement().getStyle().set("text-decoration","none");
	}
}
