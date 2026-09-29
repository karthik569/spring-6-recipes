package com.apress.spring6recipes.sequence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apress.spring6recipes.sequence.Sequence;

@Configuration
public class SequenceConfiguration {

    /**
     * @return 	A Sequence bean with a prefix of "30", a suffix of "A", and an initial value of 100000.
     */
	@Bean
	public Sequence sequence() {
		var seqgen = new Sequence();
		seqgen.setPrefix("30");
		seqgen.setSuffix("A");
		seqgen.setInitial(100000);
		return seqgen;
	}
}