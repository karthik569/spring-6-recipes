package com.apress.spring6recipes.sequence;

import com.apress.spring6recipes.sequence.config.SequenceConfiguration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
	// Get the configuration class for Spring context initialization
	var cfg = SequenceConfiguration.class;
	// Create and manage the Spring application context with try-with-resources
	try (var ctx = new AnnotationConfigApplicationContext(cfg)) {
		// Retrieve the Sequence bean from the Spring container
		var generator = ctx.getBean(Sequence.class);
		// Generate and print the first sequence value
		System.out.println(generator.nextValue());
		// Generate and print the second sequence value
		System.out.println(generator.nextValue());
	}
}
	}
