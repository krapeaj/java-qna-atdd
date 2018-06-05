package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
    private static final Logger logger = LoggerFactory.getLogger(ApiQuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("create")
    public ResponseEntity<QuestionDto> create(@LoginUser User loginUser, @Valid @RequestBody QuestionDto questionDto) {
        Question question = qnaService.create(loginUser, questionDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + question.getId()));

        return new ResponseEntity<>(questionDto, headers, HttpStatus.CREATED);
    }

    @GetMapping("")
    public String list() {

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> show(@PathVariable long id) {
        QuestionDto question = qnaService.findQuestionById(id).toQuestionDto();
        return new ResponseEntity<>(question, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public void update(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody QuestionDto updated) {
        logger.debug("Updating question............@@@@@@@@@@@@");
        logger.debug("Update Content: {}", updated.getContents());
        qnaService.update(loginUser, id, updated);

        //TODO: RequestBody를 사용해서 HttpEntity를 사용해서 꼼수로 put (실제로 post)요청을 보내는 거랑 단순히 put요청을 보내는 거랑 왜 후자는 @RequestBody가 필요하고 전자는 아닌가??
        //TODO: PUT은 void이고 POST는 리턴으로 ResponseEntity를 반환한다.

        //TODO: 테스트 메소드 두개가 같은 repository에 있는 question 데이터를 가져오는데, 하나의 메소드가 이 데이터를 바꾸면 다른 메소드가 데이터를 사용할때 영향을 안받네??
    }
}

