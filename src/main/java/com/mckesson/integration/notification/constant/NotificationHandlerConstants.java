package com.mckesson.integration.notification.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("notificationHandlerConstant")
public class NotificationHandlerConstants {

	@Value("${spring.data.notificationconstant.int002}")
	private String template2;

	@Value("${spring.data.notificationconstant.int003}")
	private String template3;

	@Value("${spring.data.notificationconstant.int004}")
	private String template4;

	@Value("${spring.data.notificationconstant.int001}")
	private String template1;

	@Value("${spring.data.notificationconstant.int005}")
	private String template5;
	
	@Value("${spring.data.notificationconstant.int006}")
	private String template6;

	@Value("${external.path.template.property}")
	private String templatePath;
	
	public String getTemplate2() {
		return template2;
	}

	public void setTemplate2(String template2) {
		this.template2 = template2;
	}

	public String getTemplate3() {
		return template3;
	}

	public void setTemplate3(String template3) {
		this.template3 = template3;
	}

	public String getTemplate4() {
		return template4;
	}

	public void setTemplate4(String template4) {
		this.template4 = template4;
	}

	public String getTemplate1() {
		return template1;
	}

	public void setTemplate1(String template1) {
		this.template1 = template1;
	}

	public String getTemplate5() {
		return template5;
	}

	public void setTemplate5(String template5) {
		this.template5 = template5;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTemplate6() {
		return template6;
	}

	public void setTemplate6(String template6) {
		this.template6 = template6;
	}
	

}
