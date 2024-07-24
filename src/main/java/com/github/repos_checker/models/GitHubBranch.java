package com.github.repos_checker.models;

import lombok.Data;

@Data
public class GitHubBranch {
    private String name;
    private String lastCommitSha;
}