package com.example.laptopstore;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LaptopstoreApplicationTests {
	
	public static void main(String[] args) {
        SpringApplication.run(LaptopstoreApplicationTests.class, args);
        System.out.println("Application started successfully");
    }
	
/*	@Bean
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
*/
}


