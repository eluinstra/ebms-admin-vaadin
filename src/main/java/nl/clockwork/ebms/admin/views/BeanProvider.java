package nl.clockwork.ebms.admin.views;

import com.vaadin.flow.server.VaadinServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.clockwork.ebms.admin.dao.EbMSDAO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanProvider
{
	public static EbMSDAO getEbMSAdminDAO()
	{
		return get("ebMSAdminDAO",EbMSDAO.class);
	}

  public static <T> T get(Class<T> beanType)
  {
    return WebApplicationContextUtils
        .getWebApplicationContext(VaadinServlet.getCurrent().getServletContext())
        .getBean(beanType);
  }

  public static <T> T get(String name, Class<T> beanType)
  {
    return WebApplicationContextUtils
        .getWebApplicationContext(VaadinServlet.getCurrent().getServletContext())
        .getBean(name,beanType);
  }

}
