/**
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
package nl.clockwork.ebms.admin;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.vaadin.flow.i18n.I18NProvider;

import org.springframework.stereotype.Component;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TranslationProvider implements I18NProvider
{
	public static final String BUNDLE_PREFIX = "translate";
	public static final Locale LOCALE_EN = new Locale("en","US");
	private List<Locale> locales = Collections.unmodifiableList(Arrays.asList(LOCALE_EN));

	@Override
	public List<Locale> getProvidedLocales()
	{
		return locales;
	}

	@Override
	public String getTranslation(String key, Locale locale, Object...params)
	{
		try
		{
			if (key == null)
			{
				log.warn("key is null");
				return "";
			}
			val value = getValue(key,locale);
			return formatValue(value,params);
		}
		catch (final MissingResourceException e)
		{
			log.warn("",e);
			return "!" + locale.getLanguage() + ": " + key;
		}
	}

	private String getValue(String key, Locale locale) throws MissingResourceException
	{
		val bundle = ResourceBundle.getBundle(BUNDLE_PREFIX,locale);
		return bundle.getString(key);
	}

	private String formatValue(final String value, Object...params)
	{
		if (params.length == 0)
			return value;
		else
			return MessageFormat.format(value,params);
	}

}
