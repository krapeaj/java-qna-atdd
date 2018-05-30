package codesquad.web;

import codesquad.domain.User;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void form_logged_in() throws Exception {
        User loggedInUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loggedInUser)
                .getForEntity("/questionForm", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void form_NOT_logged_in() throws Exception {
        ResponseEntity<String> response = getForm(template());

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void create_logged_in() throws Exception {
        ResponseEntity<String> response = postQuestion(basicAuthTemplate());

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/"));
    }

    @Test
    public void create_NOT_logged_in() throws Exception {
        ResponseEntity<String> response = postQuestion(template());

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<String> getForm(TestRestTemplate template) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

        return template.getForEntity("/questionForm", String.class, request);
    }

    private ResponseEntity<String> postQuestion(TestRestTemplate template) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("title", "test");
        params.add("content", "content");
        params.add("_method", "PUT");

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        return template.postForEntity("/submit", request, String.class);
    }
}
