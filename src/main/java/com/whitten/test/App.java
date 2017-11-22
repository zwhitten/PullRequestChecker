package com.whitten.test;

import com.whitten.test.model.GithubRepo;
import com.whitten.test.service.GithubService;

import java.io.IOException;
import java.util.List;

/**
 * Starting point of the application
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

            List<GithubRepo> repos = service.getOrganizationReposWithPullRequests("lodash");
            // TODO: 11/22/17 any kind of processing on the resulting repo data
            for (GithubRepo repo : repos) {
                System.out.println(repo.getName() + " => " + repo.getPullRequests().size() + " PRs");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
