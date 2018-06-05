package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.dto.QuestionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_TITLE = "국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?";
    private static final Logger logger = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

    @Test
    public void create_logged_in() {
        QuestionDto questionDto = createNewQuestionDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions/create", questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String json = convertQuestionToJson(questionDto);
        assertThat(response.getBody(), is(json));
    }

    @Test
    public void create_NOT_logged_in() {
        QuestionDto questionDto = createNewQuestionDto();
        ResponseEntity<String> response = template().postForEntity("/api/questions/create", questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void show() {
        ResponseEntity<String> response = template().getForEntity("/api/questions/1", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.ACCEPTED));

        String json = convertQuestionToJson(getDefaultQuestion().toQuestionDto());
        assertThat(response.getBody(), is(json));
    }

    @Test
    public void update_logged_in() {
        QuestionDto update = createNewQuestionDto();
        basicAuthTemplate().put("/api/questions/1", update);

        QuestionDto original = template().getForObject("/api/questions/1", QuestionDto.class);
        assertThat(original.getContents(), is(update.getContents()));
    }

    @Test
    public void update_NOT_logged_in() {
        QuestionDto update = createNewQuestionDto();
        template().put("/api/questions/1", update);

        QuestionDto original = template().getForObject("/api/questions/1", QuestionDto.class);
        assertNotEquals(original.getContents(), update.getContents());
    }

    private String convertQuestionToJson(QuestionDto questionDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(questionDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private QuestionDto createNewQuestionDto() {
        return new QuestionDto(DEFAULT_TITLE, "test question");
    }
}