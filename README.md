# Example Github API Usage

This project uses the github API to load all of the repos owned by an organization and checks how many pull requests have been made to each of the repositories.

### Libraries Used
- [GSON](https://github.com/google/gson)
- [OkHttp](http://square.github.io/okhttp/)

### Improvements needed
- Unit Tests
- Rate Limit header check
- Read in organization name from command line

### Note
Github rate limits unauthenticated calls to 60 per hour. You can run this application without an authentication token but you may run into the limit.

From your github developer settings page you can create a personal access token. Apply this token to an environment variable "access_token" to run this application authenticated with a higher rate limit (5000 per hour)