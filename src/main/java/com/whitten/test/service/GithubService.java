package com.whitten.test.service;

import com.google.gson.Gson;
import com.whitten.test.model.GithubRepo;
import com.whitten.test.model.PullRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service Class used to make API calls to the Github services
 */
public class GithubService {

    private static final String base_url = "https://api.github.com/";
    private static final Gson gson = new Gson();
    // Regex pattern for pulling the URL from the header value
    private static final Pattern linkPattern = Pattern.compile("<(.*)>(.*)");

    private OkHttpClient client = new OkHttpClient();
    private String accessToken = null;


    /**
     * Get all of the Repositories owned by the provided Organization Name.
     * Loads all of the pull request data for each repository
     *
     * @param organizationName - {@link String} name of the Organization
     * @return - {@link List} of {@link GithubRepo} instances
     * @throws IOException - Communication error with the github api
     */
    public List<GithubRepo> getOrganizationReposWithPullRequests(String organizationName) throws IOException {
        GithubRepo[] repos = getOrganizationRepos(organizationName);
        List<GithubRepo> repoList = new ArrayList<GithubRepo>();

        if (repos != null) {
            for (GithubRepo repo : repos) {
                List<PullRequest> requests = getPullRequestsByRepo(repo.getUrl());
                repo.setPullRequests(requests);
                repoList.add(repo);
            }
        } else {
            System.out.println("No Repos to report");
        }
        return repoList;
    }

    /**
     * Calls the github api "orgs" endpoint and returns an array o fthe {@link GithubRepo} instances parsed from the response
     * @param organizationName - {@link String} name of the Organization
     * @return - Array of {@link GithubRepo} instances
     * @throws IOException - Communication error with the github api
     */
    private GithubRepo[] getOrganizationRepos(String organizationName) throws IOException {
        String url = base_url + "orgs/"+organizationName+"/repos";
        url = addAccessToken(url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return getResponseObject(GithubRepo[].class, response.body().string());
        } else {
            System.err.println(response.message());
            return null;
        }
    }

    /**
     * Calls the "pulls" endpoint of a repository for the first page of pull requests
     * @param repoUrl - URL of the Repository being loaded
     * @return - {@link List} of pull requests associated with the repository
     * @throws IOException - Communication error with the github api
     */
    private List<PullRequest> getPullRequestsByRepo(String repoUrl) throws IOException {
        String url = repoUrl + "/pulls";
        url = addAccessToken(url);
        url += "&state=all&per_page=100&page=1";
        return getPullRequestByRepoAndPage(url);
    }

    /**
     * Calls a provided pull request and recursively loads subsequent paginated data
     * @param url - Pull request URL with page parameter already present
     * @return - {@link List} of pull requests found at the provided URL and any subsequent pages
     * @throws IOException - Communication error with github api
     */
    private List<PullRequest> getPullRequestByRepoAndPage(String url) throws IOException{

        List<PullRequest> results = new ArrayList<PullRequest>();

        Request request = new Request.Builder()
                .url(url).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful() && response.body() != null) {

            PullRequest[] pullRequests =  getResponseObject(PullRequest[].class, response.body().string());
            List<PullRequest> pullRequestList = Arrays.asList(pullRequests);

            results.addAll(pullRequestList);

            String linkHeader = response.header("Link");
            if (StringUtils.isNotBlank(linkHeader)) {
                String nextPage = getNexPageLink(linkHeader);

                if (StringUtils.isNotBlank(nextPage)) {
                    List<PullRequest> nextPagePullRequests = getPullRequestByRepoAndPage(nextPage);
                    results.addAll(nextPagePullRequests);
                }
            }
        } else {
            System.err.println(response.message());
        }

        return results;
    }


    /**
     * Use a Regex to parse the "next" url from the Link header value
     * @param headerLink - Header "Link" value
     * @return - {@link String} URL for the next page of results or null if no further pages are present
     */
    private String getNexPageLink(String headerLink) {

        String[] linkPieces = StringUtils.split(headerLink, ",");
        String nextLink = linkPieces.length > 0 ? linkPieces[0] : "";

        String nextUrl = null;

        if (!StringUtils.contains(nextLink, "next")) {
            return null;
        }
        Matcher matcher = linkPattern.matcher(nextLink);
        if (matcher.matches()) {

            nextUrl =  matcher.group(1);
        }


        return nextUrl;
    }

    /**
     * If the access token has been set then append the access_token value to the URL
     * @param url - Current URL
     * @return - URL value with the access_token parameter appended (if available)
     */
    private String addAccessToken(String url) {
        if (StringUtils.isNotBlank(this.accessToken)) {
            url += ("?access_token=" + this.accessToken);
        } else {
            url += "?";
        }
        return url;
    }

    /**
     * Uses {@link Gson} to convert the JSON response into the provided object class
     * @param objectClass - Class to convert the JSON into
     * @param response - JSON response from the github api
     * @return - Return a new instance of the destination class
     */
    private static <T> T getResponseObject(Class<T> objectClass, String response) {
        return gson.fromJson(response, objectClass);
    }

    /**
     * Setter method for the personal access_token
     * @param accessToken - github api personal access_token value
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
