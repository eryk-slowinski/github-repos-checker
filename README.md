# github-repos-checker
This project is a Spring Boot application that provides an API endpoint to fetch non-forked GitHub repositories for a given user, along with the branch names and the latest commit SHA for each branch.

## Features

- Fetch non-forked repositories for a specified GitHub user.
- Retrieve branch names and the latest commit SHA for each branch in the repositories.
- Returns a 404 response if the GitHub user is not found.
- Ensures that only JSON responses are supported.

### Prerequisites

- Java 21 or newer
- Maven 3.6.0 or newer

Usage
The API provides a single endpoint to fetch the repositories and branch information for a GitHub user:

GET /user/{username}
Fetches the non-forked repositories of a GitHub user along with their branches and latest commit SHAs.

Request Headers:
- Accept: application/json

Parameters:
- username: GitHub username (path parameter)

Example:
bash
Copy code
curl -H "Accept: application/json" http://localhost:8080/user/eryk-slowinski

Response:
- 200 OK with a JSON array of repositories
- 404 Not Found if the user does not exist
- 415 Unsupported Media Type if the Accept header is not application/json

Response Format

```json
[
    {
        "name": "Hello-World",
        "ownerLogin": "eryk-slowinski",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "7638417db6d59f3c431sda1f261cc63715568dsa"
            }
        ]
    }
]
```
Error Responses
404 Not Found:

```json
{
    "status": 404,
    "message": "User not found"
}
```
415 Unsupported Media Type:

```json
{
    "status": 415,
    "message": "Only application/json is supported"
}