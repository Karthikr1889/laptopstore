package com.example.laptopstore;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.modelmapper.ModelMapper;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.laptopstore.*;

@SpringBootApplication
public class LaptopstoreApplicationTests {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	//Run|Debug
	public static void main(String[] args) {
    	LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
    			.selectors(DiscoverySelectors.selectPackage("com.example.laptopstore.test")).build();
    	
    	Launcher launcher = org.junit.platform.launcher.core.LauncherFactory.create();
    //	CustomTestSummaryListener listener = new CustomTestSummaryListener();
    	
    	launcher.execute(request);
    	
		//SpringApplication.run(LaptopstoreApplicationTests.class, args);
	}

}


