package com.fedorchenko.testTask.controllers;

import com.fedorchenko.testTask.dto.PollDescriptionDto;
import com.fedorchenko.testTask.dto.PollWithQuestionsDTO;
import com.fedorchenko.testTask.entities.Answer;
import com.fedorchenko.testTask.entities.Poll;
import com.fedorchenko.testTask.repositories.UserRepo;
import com.fedorchenko.testTask.services.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user_api/")
public class UserApiController {

    @Autowired
    UserApiService userApiService;


    @GetMapping("/answered")
    public List<PollDescriptionDto> sendAnsweredPolls(Principal principal) {
        List<PollDescriptionDto> polls = new ArrayList<>();
        for (Poll poll : userApiService.getAnswered(principal.getName())) {
            polls.add(new PollDescriptionDto(poll, true));
        }
        return polls;
    }

    @GetMapping("/actual_polls")
    public List<PollDescriptionDto> sendActualPolls(Principal principal) {
        List<PollDescriptionDto> polls = new ArrayList<>();
        List<Poll> actualPollsWithoutAnswers = userApiService
                .getActual();
        actualPollsWithoutAnswers.removeAll(userApiService.getAnswered(principal.getName()));
        for (Poll poll : actualPollsWithoutAnswers) {
            polls.add(new PollDescriptionDto(poll, false));
        }
        return polls;
    }

    @GetMapping("/poll/{id}")
    public PollWithQuestionsDTO sendActualPollById(@PathVariable Long id) {
        return new PollWithQuestionsDTO(userApiService.getPollById(id));
    }

    @GetMapping("/answered/{id}")
    public PollWithQuestionsDTO sendAnsweredPollById(@PathVariable Long id, Principal principal) {
        Poll poll = userApiService.getPollById(id);
        List<PollWithQuestionsDTO.QuestionDto> questionDtos = new ArrayList<>();
        for (Answer answer : userApiService.getAnswersByPollAndUser(poll, principal.getName())) {
            questionDtos.add(new PollWithQuestionsDTO.QuestionDto(answer.getQuestion(), answer));
        }
        return new PollWithQuestionsDTO(poll, questionDtos);
    }


}