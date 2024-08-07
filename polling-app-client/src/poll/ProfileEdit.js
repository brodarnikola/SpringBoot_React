import React, { useState, useEffect } from 'react';
import { withRouter } from 'react-router-dom';
import './ProfileEdit.css';
import { checkEmailAvailability, getUserProfile, updateProfile } from "../util/APIUtils";
import {
    NAME_MIN_LENGTH, NAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH
} from "../constants";
import {Form, Button, notification, Input} from 'antd';

const { Item: FormItem } = Form;

const ProfileEdit = (props) => {
    const [currentUser, setCurrentUser] = useState(null);
    const [username, setUsername] = useState({ value: '' });
    const [name, setName] = useState({ value: '' });
    const [email, setEmail] = useState({ value: '' });
    const [hideMessage, setHideMessage] = useState(true);

    useEffect(() => {
        const username = props.match.params.username;
        loadUserProfile(username);
    }, [props.match.params.username]);

    const loadUserProfile = (username) => {
        getUserProfile(username)
            .then(response => {
                setCurrentUser(response);
            }).catch(error => {
            if (error.status === 404) {
                notification.error({
                    message: 'Polling App',
                    description: 'User not found'
                });
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    };

    const handleInputChange = (event, validationFun) => {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        if (inputName === 'name') {
            setName({
                value: inputValue,
                ...validationFun(inputValue)
            });
        } else if (inputName === 'email') {
            setEmail({
                value: inputValue,
                ...validationFun(inputValue)
            });
        }
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        const userOldData = {
            name: currentUser.name,
            email: currentUser.email
        };

        const newName = name.value === '' ? userOldData.name : name.value;
        const newEmail = email.value === '' ? userOldData.email : email.value;

        const updateProfileRequest = {
            oldUsername: currentUser.username,
            oldName: currentUser.name,
            oldEmail: currentUser.email,
            name: newName,
            email: newEmail
        };

        if (username.value === '' && name.value === '' && email.value === '') {
            alert("Nothing changed");
            setHideMessage(false);
        } else {
            updateProfile(updateProfileRequest)
                .then(response => {
                    notification.success({
                        message: 'Polling App',
                        description: "You have successfully updated your profile.",
                    });
                    props.history.push("/");
                }).catch(error => {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
        }
    };

    const isFormInvalid = () => {
        return !(name.validateStatus === 'success' && email.validateStatus === 'success');
    };

    const validateName = (name) => {
        if (name.length === 0) {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        } else if (name.length < NAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
            };
        } else if (name.length > NAME_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
            };
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        }
    };

    const validateEmail = (email) => {
        if (email.length === 0) {
            return {
                validateStatus: null,
                errorMsg: null
            };
        }

        const EMAIL_REGEX = RegExp('[^@ ]+@[^@ ]+\\.[^@ ]+');
        if (!EMAIL_REGEX.test(email)) {
            return {
                validateStatus: 'error',
                errorMsg: 'Email not valid'
            };
        }

        if (email.length > EMAIL_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Email is too long (Maximum ${EMAIL_MAX_LENGTH} characters allowed)`
            };
        }

        return {
            validateStatus: 'success',
            errorMsg: null
        };
    };

    const validateEmailAvailability = () => {
        const emailValue = email.value;
        const emailValidation = validateEmail(emailValue);

        if (emailValidation.validateStatus === 'error') {
            setEmail({
                value: emailValue,
                ...emailValidation
            });
            return;
        }

        setEmail({
            value: emailValue,
            validateStatus: 'validating',
            errorMsg: null
        });

        checkEmailAvailability(emailValue)
            .then(response => {
                if (response.available) {
                    setEmail({
                        value: emailValue,
                        validateStatus: 'success',
                        errorMsg: null
                    });
                } else {
                    setEmail({
                        value: emailValue,
                        validateStatus: 'error',
                        errorMsg: 'This Email is already registered'
                    });
                }
            }).catch(error => {
            setEmail({
                value: emailValue,
                validateStatus: 'success',
                errorMsg: null
            });
        });
    };

    return (
        currentUser ? (
            <div className="main-profile-edit">
                <Form onSubmitCapture={handleSubmit} className="edit-profile-form">
                    <FormItem
                        hasFeedback
                        validateStatus={name.validateStatus}
                        help={name.errorMsg}
                    >
                        New name:  &nbsp;  &nbsp;
                        <Input
                            className="input-name"
                            type="text"
                            name="name"
                            value={name.value}
                            onChange={(event) => handleInputChange(event, validateName)}
                        />
                        &nbsp;  &nbsp; Current name: {currentUser.name}
                    </FormItem>
                    <FormItem
                        hasFeedback
                        validateStatus={email.validateStatus}
                        help={email.errorMsg}
                    >
                        New email: &nbsp;  &nbsp;
                        <Input
                            className="input-email"
                            type="email"
                            name="email"
                            onBlur={validateEmailAvailability}
                            value={email.value}
                            onChange={(event) => handleInputChange(event, validateEmail)}
                        />
                        &nbsp;  &nbsp; Current email: {currentUser.email}
                    </FormItem>
                    <FormItem>
                        <Button
                            type="primary"
                            htmlType="submit"
                            className="update-profile-form-button"
                            disabled={isFormInvalid()}
                        >
                            Update profile
                        </Button>
                    </FormItem>
                </Form>
            </div>
        ) : null
    );
};

export default withRouter(ProfileEdit);
