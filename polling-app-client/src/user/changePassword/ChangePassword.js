import React, { useState, useEffect } from 'react';
import { Link, useHistory, useLocation } from 'react-router-dom';
import { ACCESS_TOKEN, PASSWORD_MAX_LENGTH, PASSWORD_MIN_LENGTH, PASSWORD_DOES_NOT_MATCH } from '../../constants';
import { Form, Input, Button, notification } from 'antd';
import { showChangePasswordPage, savePassword } from "../../util/APIUtils";

const { Item: FormItem } = Form;

const ChangePassword = () => {
    const [password1, setPassword1] = useState({ value: '', validateStatus: '', errorMsg: '' });
    const [password2, setPassword2] = useState({ value: '', validateStatus: '', errorMsg: '' });
    const history = useHistory();
    const location = useLocation();

    useEffect(() => {
        localStorage.setItem(ACCESS_TOKEN, "");
    }, []);

    const handleInputChange = (event, validationFun) => {
        const { name, value } = event.target;

        if (name === 'password1') {
            setPassword1({ value, ...validationFun(value) });
        } else if (name === 'password2') {
            setPassword2({ value, ...validationFun(value) });
        }
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        const query = new URLSearchParams(location.search);
        let id = query.get('id');
        let token = query.get('token');

        showChangePasswordPage(id, token)
            .then(response => {
                console.log("Token je: " + response.accessToken);
                localStorage.setItem(ACCESS_TOKEN, response.accessToken);

                const savePasswordRequest = {
                    password: password1.value
                };

                savePassword(savePasswordRequest)
                    .then(response => {
                        notification.success({
                            message: 'Polling App',
                            description: "Thank you! You have successfully updated your password!",
                        });
                        history.push("/login");
                    }).catch(error => {
                    notification.error({
                        message: 'Polling App',
                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                    });
                });
            }).catch(error => {
            if (error.message.includes("Please send one more password recovery.")) {
                history.push("/login");
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    };

    const isFormInvalid = () => {
        return !(password1.validateStatus === 'success' && password2.validateStatus === 'success');
    };

    const validatePassword1 = (password) => {
        if (password.length < PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
            };
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
            };
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        }
    };

    const validatePassword2 = (password) => {
        if (password.length < PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
            };
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
            };
        } else if (password !== password1.value) {
            return {
                validateStatus: 'error',
                errorMsg: `Passwords do not match ${PASSWORD_DOES_NOT_MATCH}.`
            };
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        }
    };

    return (
        <div className="signup-container">
            <h1 className="page-title">Change password</h1>
            <div className="signup-content">
                <Form onSubmitCapture={handleSubmit} className="signup-form">
                    <FormItem
                        label="New password between 6 to 20 characters"
                        hasFeedback
                        validateStatus={password1.validateStatus}
                        help={password1.errorMsg}>
                        <Input
                            size="large"
                            name="password1"
                            type="password"
                            autoComplete="off"
                            placeholder="New password between 6 to 20 characters"
                            value={password1.value}
                            onChange={(event) => handleInputChange(event, validatePassword1)}
                        />
                    </FormItem>
                    <FormItem
                        label="Confirm password"
                        hasFeedback
                        validateStatus={password2.validateStatus}
                        help={password2.errorMsg}>
                        <Input
                            size="large"
                            name="password2"
                            type="password"
                            autoComplete="off"
                            placeholder="Confirm password"
                            value={password2.value}
                            onChange={(event) => handleInputChange(event, validatePassword2)}
                        />
                    </FormItem>
                    <FormItem>
                        <Button
                            type="primary"
                            htmlType="submit"
                            size="large"
                            className="signup-form-button"
                            disabled={isFormInvalid()}
                        >
                            Update password
                        </Button>
                        <br/>
                        Already registered? <Link to="/login">Login now!</Link>
                    </FormItem>
                </Form>
            </div>
        </div>
    );
};

export default ChangePassword;
