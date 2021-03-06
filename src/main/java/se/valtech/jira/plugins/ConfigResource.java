package se.valtech.jira.plugins;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;

@Path("/")
public class ConfigResource {
	private static final Logger log = LoggerFactory.getLogger(ConfigResource.class);
	private final UserManager userManager;
	private final PluginSettingsFactory pluginSettingsFactory;
	private final TransactionTemplate transactionTemplate;

	public ConfigResource(UserManager userManager,
			PluginSettingsFactory pluginSettingsFactory,
			TransactionTemplate transactionTemplate) {
		this.userManager = userManager;
		this.pluginSettingsFactory = pluginSettingsFactory;
		this.transactionTemplate = transactionTemplate;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@Context HttpServletRequest request)
	{
	  String username = userManager.getRemoteUsername(request);
	  if (username == null || !userManager.isSystemAdmin(username))
	  {
	    return Response.status(Status.UNAUTHORIZED).build();
	  }
	 
	  return Response.ok(transactionTemplate.execute(new TransactionCallback()
	  {
	    public Object doInTransaction()
	    {
	      PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
	      Config config = new Config();
	      config.setUrl((String) settings.get(Config.class.getName() + ".url"));
	      config.setCompanyId((String) settings.get(Config.class.getName() + ".companyId"));
	      config.setUserId((String) settings.get(Config.class.getName() + ".userId"));
	      config.setPassword((String) settings.get(Config.class.getName() + ".password"));
	      return config;
	    }
	  })).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(final Config config, @Context HttpServletRequest request)
	{
	  String username = userManager.getRemoteUsername(request);
	  if (username == null || !userManager.isSystemAdmin(username))
	  {
	    return Response.status(Status.UNAUTHORIZED).build();
	  }
	 
	  transactionTemplate.execute(new TransactionCallback()
	  {
	    public Object doInTransaction()
	    {
	      PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
	      pluginSettings.put(Config.class.getName() + ".url", config.getUrl());
	      pluginSettings.put(Config.class.getName() + ".companyId", config.getCompanyId());
	      pluginSettings.put(Config.class.getName()  +".userId", config.getUserId());
	      pluginSettings.put(Config.class.getName()  +".password", config.getPassword());
	      return null;
	    }
	  });
	  return Response.noContent().build();
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static final class Config
	{
		@XmlElement private String url;
		@XmlElement private String companyId;
		@XmlElement private String userId;
		@XmlElement private String password;
		
		public String getUrl() {
			return url;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getCompanyId() {
			return companyId;
		}
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}     
	}
}