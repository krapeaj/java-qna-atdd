package codesquad.web;

import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String CREATE_URL = "/api/questions/create";
    private static final String DEFAULT_QUESTION_1_URL = "/api/questions/1";
    private static final Logger logger = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

    @Test
    public void create_logged_in() {
        QuestionDto questionDto = new QuestionDto("title", "content");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(CREATE_URL, questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        logger.debug("Question JSON Data: {}", response.getBody());
    }

    @Test
    public void create_NOT_logged_in() {
        QuestionDto questionDto = new QuestionDto("title", "content");
        ResponseEntity<String> response = template().postForEntity(CREATE_URL, questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void show() {
        ResponseEntity<String> response = template().getForEntity(DEFAULT_QUESTION_1_URL, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.ACCEPTED));

        logger.debug("Question JSON Data: {}", response.getBody());
    }

    @Test
    public void update_logged_in() {
        QuestionDto questionDto = new QuestionDto("title", "content");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(CREATE_URL, questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String uri = response.getHeaders().getLocation().getPath();
        QuestionDto update = new QuestionDto("title", "updated content");
        basicAuthTemplate().put(uri, update);

        QuestionDto original = template().getForObject(uri, QuestionDto.class);
        assertThat(original.getContents(), is(update.getContents()));
    }

    @Test
    public void update_NOT_logged_in() {
        QuestionDto questionDto = new QuestionDto("title", "content");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(CREATE_URL, questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String uri = response.getHeaders().getLocation().getPath();
        QuestionDto update = new QuestionDto("title", "updated content");
        template().put(uri, update);

        QuestionDto original = template().getForObject(uri, QuestionDto.class);
        assertNotEquals(original.getContents(), update.getContents());
    }
}