package com.github.repos_checker.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.github.repos_checker.exceptions.UserNotFoundException;
import com.github.repos_checker.models.GitHubBranch;
import com.github.repos_checker.models.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {

    private final WebClient webClient;
    private final Gson gson;
    private final String BASEURL = "https://api.github.com";

    @Autowired
    public GitHubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASEURL).build();
        this.gson = new Gson();
    }

    public List<GitHubRepository> getUserRepositories(String username) {
        try {
            String response = webClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonArray reposArray = gson.fromJson(response, JsonArray.class);
            List<GitHubRepository> repositories = new ArrayList<>();

            for (int i = 0; i < reposArray.size(); i++) {
                JsonObject repoObject = reposArray.get(i).getAsJsonObject();
                if (!repoObject.get("fork").getAsBoolean()) {
                    GitHubRepository repository = GitHubRepository.builder()
                            .name(repoObject.get("name").getAsString())
                            .ownerLogin(repoObject.get("owner").getAsJsonObject().get("login").getAsString())
                            .build();


                    String branchesResponse = webClient.get()
                            .uri("/repos/{owner}/{repo}/branches", repository.getOwnerLogin(), repository.getName())
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

                    JsonArray branchesArray = gson.fromJson(branchesResponse, JsonArray.class);
                    List<GitHubBranch> branches = new ArrayList<>();

                    for (int j = 0; j < branchesArray.size(); j++) {
                        JsonObject branchObject = branchesArray.get(j).getAsJsonObject();
                        GitHubBranch branch = GitHubBranch.builder()
                                .name(branchObject.get("name").getAsString())
                                .lastCommitSha(branchObject.get("commit").getAsJsonObject().get("sha").getAsString())
                                .build();
                        branches.add(branch);
                    }

                    repository.setBranches(branches);
                    repositories.add(repository);
                }
            }

            return repositories;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found");
            } else {
                throw e;
            }
        }
    }
}
