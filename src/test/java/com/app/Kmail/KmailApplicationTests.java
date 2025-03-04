package com.app.Kmail;

import com.app.Kmail.init.DBinit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class KmailApplicationTests {
	@MockBean
	DBinit dBinit;

	@Test
	void contextLoads() {
	}

}
