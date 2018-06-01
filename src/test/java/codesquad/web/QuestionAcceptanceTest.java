package codesquad.web;

import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import javax.xml.ws.Response;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Test
    public void form_logged_in() throws Exception {
        User loggedInUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loggedInUser)
                .getForEntity("/questions/form", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void form_NOT_logged_in() throws Exception {
        ResponseEntity<String> response = createGetResponse(template());

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void create_logged_in() throws Exception {
        ResponseEntity<String> response = createPostResponse(basicAuthTemplate());

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/"));
    }

    @Test
    public void create_NOT_logged_in() throws Exception {
        ResponseEntity<String> response = createPostResponse(template());

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void update_logged_in() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = createUpdateResponse(basicAuthTemplate(loginUser));

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/questions/1"));
    }

    @Test
    public void update_NOT_logged_in() throws Exception {

    }

    @Test
    public void delete_logged_in() throws Exception {

    }

    @Test
    public void delete_NOT_logged_in() throws Exception {

    }

    private ResponseEntity<String> createGetResponse(TestRestTemplate template) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

        return template.getForEntity("/questions/form", String.class, request);
    }

    private ResponseEntity<String> createPostResponse(TestRestTemplate template) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("title", "test");
        params.add("content", "content");
        params.add("_method", "PUT");

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        return template.postForEntity("/questions/submit", request, String.class);
    }

    private ResponseEntity<String> createUpdateResponse(TestRestTemplate template) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("title", "test");
        params.add("content", "update");

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        return template.postForEntity("/questions/1/update", request, String.class);
    }
}
