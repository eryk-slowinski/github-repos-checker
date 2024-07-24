package com.github.repos_checker.models;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class GitHubRepository {
    private String name;
    private String ownerLogin;
    private List<GitHubBranch> branches;
}
