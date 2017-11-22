package com.whitten.test;

import com.whitten.test.model.GithubRepo;
import com.whitten.test.model.PullRequest;
import com.whitten.test.service.GithubService;

import java.io.IOException;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String accessToken = System.getenv("access_token");
        try {
            GithubService service = new GithubService();
            service.setAccessToken(accessToken);
            GithubRepo[] repos = service.getOrganizationRepos("lodash");

            if (repos != null) {
                for (GithubRepo repo : repos) {
                    PullRequest[] requests = service.getPullRequestsByRepo(repo.getUrl());
                    if (requests != null) {
                        repo.setPullRequests(Arrays.asList(requests));
                    }

                    System.out.println(repo.toString());
                }
            } else {
                System.out.println("No Repos to report");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
