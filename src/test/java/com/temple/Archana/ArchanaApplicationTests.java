package com.temple.Archana;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	    "spring.datasource.url=jdbc:h2:mem:temple_archana_context;DB_CLOSE_DELAY=-1",
	    "spring.datasource.driver-class-name=org.h2.Driver",
	    "spring.datasource.username=sa",
	    "spring.datasource.password=",
	    "spring.jpa.hibernate.ddl-auto=create-drop"
	})
class ArchanaApplicationTests {

	@Test
	void contextLoads() {
	}

}
