package com.github.repos_checker.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GitHubBranch {
    private String name;
    private String lastCommitSha;
}