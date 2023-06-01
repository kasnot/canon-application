package io.pikei.api;

import lombok.extern.slf4j.Slf4j;
import io.pikei.canon.framework.api.command.TerminateSdkCommand;
import io.pikei.canon.framework.api.helper.factory.CanonFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;


/**
 * The Web application boot.
 *
 * Notes:
 * - The security module is automatically imported by the auto configuration if the property security.enabled = true;
 * - The mail module is imported using the annotation @EnableMail. However, it can also be imported directly via @Import(MailConfiguration.class);
 */
@SpringBootApplication
@ComponentScan(basePackages = "io.pikei")
@Slf4j
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@PreDestroy
	public void init(){
		log.info("Shutting down application.");
		CanonFactory.commandDispatcher().scheduleCommand(new TerminateSdkCommand());
	}

}
