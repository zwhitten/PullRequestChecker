package com.whitten.test.model;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing the Github repository response JSON data
 */
public class GithubRepo {

    private String name;
    private String fullName;
    private String description;
    private String url;

    private List<PullRequest> pullRequests = new ArrayList<PullRequest>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<PullRequest> getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(List<PullRequest> pullRequests) {
        this.pullRequests = pullRequests;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        if (this.pullRequests != null && pullRequests.size() > 0) {
            builder.append("; PR Count: ").append(this.pullRequests.size());
        } else {
            builder.append("; No PRs");
        }
        return builder.toString();
    }

}
