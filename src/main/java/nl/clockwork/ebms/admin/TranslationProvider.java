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
