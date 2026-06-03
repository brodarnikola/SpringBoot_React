import { API_BASE_URL, POLL_LIST_SIZE, ACCESS_TOKEN } from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response =>
        // Read the body as text first so an empty or non-JSON response (e.g. a
        // 401 with no body) doesn't blow up with "Unexpected end of JSON input".
        response.text().then(text => {
            let json;
            try {
                json = text ? JSON.parse(text) : {};
            } catch (e) {
                json = {};
            }

            if(!response.ok) {
                return Promise.reject({
                    status: response.status,
                    success: false,
                    message: json.message || 'Something went wrong. Please try again later.',
                    ...json
                });
            }
            return json;
        })
    );
};

export function getAllPolls(page, size) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/polls?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function createPoll(pollData) {
    return request({
        url: API_BASE_URL + "/polls",
        method: 'POST',
        body: JSON.stringify(pollData)         
    });
}

export function castVote(voteData) {
    return request({
        url: API_BASE_URL + "/polls/" + voteData.pollId + "/votes",
        method: 'POST',
        body: JSON.stringify(voteData)
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function signUpConfirm(token) {
    return request({
        url: API_BASE_URL + "/auth/signUpConfirm?token=" + token,
        method: 'GET'
    });
}


export function forgotPassword(forgotPasswordRequest) {
    return request({
        url: API_BASE_URL + "/auth/forgotPassword",
        method: 'POST',
        body: JSON.stringify( forgotPasswordRequest)
    });
}

export function savePassword(savePasswordRequest) {
    return request({
        url: API_BASE_URL + "/auth/user/savePassword",
        method: 'POST',
        body: JSON.stringify( savePasswordRequest)
    });
}

export function showChangePasswordPage(id, token) {
    return request({
        url: API_BASE_URL + "/auth/changePassword?id=" + id + "&token=" + token,
        method: 'GET'
    });
}

export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}

export function longTask() {
    return request({
        url: API_BASE_URL + "/polls/longTask",
        method: 'GET'
    });
}

export function getAccounts() {
    return request({
        url: API_BASE_URL + "/accounts",
        method: 'GET'
    });
}

export function getPreviousMonthTurnover() {
    return request({
        url: API_BASE_URL + "/calculatePreviousMonthTurnover",
        method: 'GET'
    });
}

export function updateProfile(updateProfileRequest) {
    return request({
        url: API_BASE_URL + "/user/updateProfile",
        method: 'PUT',
        body: JSON.stringify(updateProfileRequest)
    });
}

export function delete_Poll(deletePollRequest) {
    return request({
        url: API_BASE_URL + "/user/deletePoll",
        method: 'DELETE',
        body: JSON.stringify(deletePollRequest)
    });
}

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: API_BASE_URL + "/users/" + username,
        method: 'GET'
    });
}

export function getUserCreatedPolls(username, page, size) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/users/" + username + "/polls?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function getUserVotedPolls(username, page, size) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/users/" + username + "/votes?page=" + page + "&size=" + size,
        method: 'GET'
    });
}