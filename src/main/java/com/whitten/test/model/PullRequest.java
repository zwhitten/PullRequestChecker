package com.whitten.test.model;

import java.util.Date;

/**
 * POJO representing the Github api pull request JSON data
 */
public class PullRequest {

    private String url;
    private String title;
    private String state;

    private Date created_at;
    private Date closed_at;
}
