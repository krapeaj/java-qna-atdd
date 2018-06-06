package codesquad.web;

import codesquad.dto.AnswerDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final String CREATE_URL = "/api/questions/1/answers";
    private static final AnswerDto NEW_ANSWER = new AnswerDto("content");

    @Test
    public void add_logged_in() {
        ResponseEntity<AnswerDto> response = basicAuthTemplate().postForEntity(CREATE_URL, NEW_ANSWER, AnswerDto.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getContent(), is("content"));
    }

    @Test
    public void add_NOT_logged_in() {
        ResponseEntity<AnswerDto> response = template().postForEntity(CREATE_URL, NEW_ANSWER, AnswerDto.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void getAnswer() {
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/1/answers/1", AnswerDto.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void delete_logged_in() {
        String location = createResource(CREATE_URL, NEW_ANSWER, AnswerDto.class);

        basicAuthTemplate().delete(location);

        ResponseEntity<AnswerDto> res = template().getForEntity(location, AnswerDto.class);
        assertThat(res.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void delete_NOT_logged_in() {
        String location = createResource(CREATE_URL, NEW_ANSWER, AnswerDto.class);

        template().delete(location);

        ResponseEntity<AnswerDto> res = template().getForEntity(location, AnswerDto.class);
        assertThat(res.getStatusCode(), is(HttpStatus.OK)); //OK를 받아야 한다는 게 조금 이상하지만.. delete가 안돼서 아직 있으니 get요청을 보냈을 때 OK를 받음.
    }
}