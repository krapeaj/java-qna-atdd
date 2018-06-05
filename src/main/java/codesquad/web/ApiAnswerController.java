package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    @Resource(name = "qnaService")
    private QnaService answerService;

    @PostMapping("")
    public String add(@LoginUser User loginUser, @PathVariable long questionId, AnswerDto answerDto) {

        return null;
    }

    @DeleteMapping("{id}")
    public void delete(@LoginUser User loginUser, @PathVariable long id) {

    }
}
