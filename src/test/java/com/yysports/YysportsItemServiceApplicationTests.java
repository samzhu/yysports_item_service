package com.yysports;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class YysportsItemServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Test
    public void testItemGroupReport() throws Exception {
        ResponseEntity<byte[]> response = this.restTemplate.getForEntity("/api/v1/ItemGroupReport?id={id}", byte[].class, "11");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(entity.getBody()).isEqualTo("Hello, world");
    }

    @Test
    public void testItemReport() throws Exception {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/api/v1/ItemReport", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
