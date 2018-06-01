package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Resource
    private QnaService qnaService;

    @GetMapping("/form")
    public String questionForm(@LoginUser User user) {
        logger.debug("Showing question form...");
        return "/qna/form";
    }

    @PutMapping("/submit")
    public String submit(@LoginUser User user, QuestionDto questionDto) {
        qnaService.create(user, questionDto);
        logger.debug("Submitting question...");
        return "redirect:/";
    }
}
