package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public ResponseEntity<Answer> add(@LoginUser User loginUser, @PathVariable long questionId, AnswerDto answerDto) {
        Answer answer = qnaService.addAnswer(loginUser, questionId, answerDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions" + questionId + "/answers/" + answer.getId()));
        return new ResponseEntity<>(answer, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public void delete(@LoginUser User loginUser, @PathVariable long id) {

    }
}
