package com.example.smsSpringTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class JobsiteApplicationTests {

	@Test
	void contextLoads() {
		int a=1;
		int b=1;
		assertEquals(a,b);
	}

}
