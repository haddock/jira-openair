package se.valtech.jira.plugins;

import com.atlassian.sal.api.ApplicationProperties;

public class OpenAirIntegrationPluginComponentImpl implements OpenAirIntegrationPluginComponent {

	private final ApplicationProperties applicationProperties;
	
	public OpenAirIntegrationPluginComponentImpl(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	@Override
	public String getName() {
		if(null != applicationProperties) {
			return "OpenAirIntegrationPluginComponent:" + applicationProperties.getDisplayName();
			
		}
		return "OpenAirIntegrationPluginComponent";
	}

}
