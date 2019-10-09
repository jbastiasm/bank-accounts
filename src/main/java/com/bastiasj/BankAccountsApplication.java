package com.bastiasj;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.bastiasj" })
@EnableJpaRepositories(basePackages = "com.bastiasj.repositories")
@EnableTransactionManagement
@EntityScan(basePackages = "com.bastiasj.entities")
public class BankAccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountsApplication.class, args);
	}

	@Bean
	public PromptProvider myPromptProvider() {
		return new PromptProvider() {
			@Override
			public AttributedString getPrompt() {
				return new AttributedString("bank-shell:>",
				        AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
			}
		};
	}
}
