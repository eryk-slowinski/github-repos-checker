package com.github.repos_checker.controllers;

import com.github.repos_checker.exceptions.UnsupportedMediaTypeException;
import com.github.repos_checker.models.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.repos_checker.services.GitHubService;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class GitHubController {

    private static final Logger logger = Logger.getLogger(GitHubController.class.getName());
    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<GitHubRepository> getUserRepositories(@PathVariable String username, @RequestHeader("Accept") String acceptHeader) {
        if (!"application/json".equals(acceptHeader)) {
            logger.info("Received request without proper Accept header");
            throw new UnsupportedMediaTypeException("Only application/json is supported");
        } else {
            logger.info("Received request for user: " + username);
        }
        return gitHubService.getUserRepositories(username);
    }
}
