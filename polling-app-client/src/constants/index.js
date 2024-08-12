export const API_BASE_URL = 'http://localhost:5000/api';
//export const API_BASE_URL = '/api';
export const ACCESS_TOKEN = 'accessToken';

export const POLL_LIST_SIZE = 30;
export const MAX_CHOICES = 6;
export const POLL_QUESTION_MAX_LENGTH = 140;
export const POLL_CHOICE_MAX_LENGTH = 40;

export const NAME_MIN_LENGTH = 4;
export const NAME_MAX_LENGTH = 40;

export const USERNAME_MIN_LENGTH = 3;
export const USERNAME_MAX_LENGTH = 15;

export const EMAIL_MAX_LENGTH = 40;

export const PASSWORD_MIN_LENGTH = 6;
export const PASSWORD_MAX_LENGTH = 20;


export const PASSWORD_DOES_NOT_MATCH = '';

const prod = {
    url: {
        API_BASE_URL: 'https://mybeapp.herokuapp.com',
        OAUTH2_REDIRECT_URI: 'https://myfeapp.herokuapp.com/oauth2/redirect'
    }
}

const dev = {
    url: {
        API_BASE_URL: 'http://localhost:5000', // 'http://localhost:8080',
        OAUTH2_REDIRECT_URI: 'http://localhost:3000/oauth2/redirect'
    }
}

export const config = process.env.NODE_ENV === 'development' ? dev : prod
