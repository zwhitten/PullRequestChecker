package com.whitten.test.service;

import com.whitten.test.model.GithubRepo;
import com.whitten.test.model.PullRequest;
import com.whitten.test.model.ResponseParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GithubService {

    private static final String base_url = "https://api.github.com/";
    private OkHttpClient client = new OkHttpClient();
    private String accessToken = null;



    public GithubRepo[] getOrganizationRepos(String organizationName) throws IOException {
        String url = base_url + "orgs/"+organizationName+"/repos";
        url = addAccessToken(url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        // TODO: 11/21/17 error and null check
        Response response = client.newCall(request).execute();
        return ResponseParser.getResponseObject(GithubRepo[].class, response.body().string());
    }

    public PullRequest[] getPullRequestsByRepo(String repoUrl) throws IOException {

        String url = repoUrl + "/pulls?state=all&per_page=100";
        url = addAccessToken(url);

        Request request = new Request.Builder()
                .url(url).build();
        Response response = client.newCall(request).execute();

        // TODO: 11/21/17 handle pagination?
        return ResponseParser.getResponseObject(PullRequest[].class, response.body().string());

    }

    private String addAccessToken(String url) {
        if (accessToken != null) {
            url += ("?access_token=" + this.accessToken);
        }
        return url;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
