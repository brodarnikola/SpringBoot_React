import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { EMAIL_MAX_LENGTH } from '../../constants';
import { Form, Input, Button, notification } from 'antd';
import { forgotPassword } from "../../util/APIUtils";

const { Item: FormItem } = Form;

const ForgotPassword = () => {
    const [email, setEmail] = useState({ value: '', validateStatus: '', errorMsg: '' });

    const handleInputChange = (event, validationFun) => {
        const { name, value } = event.target;

        if (name === 'email') {
            setEmail({
                value,
                ...validationFun(value)
            });
        }
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        const forgotPasswordRequest = {
            email: email.value
        };

        forgotPassword(forgotPasswordRequest)
            .then(response => {
                notification.success({
                    message: 'Polling App',
                    description: "Thank you! We have successfully sent you an email. Please check your email to confirm!",
                });
            }).catch(error => {
            notification.error({
                message: 'Polling App',
                description: "GRESKA: " + error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    };

    const isFormInvalid = () => {
        return !(email.validateStatus === 'success');
    };

    const validateEmail = (email) => {
        if (!email) {
            return {
                validateStatus: 'error',
                errorMsg: 'Email may not be empty'
            }
        }

        const EMAIL_REGEX = RegExp('[^@ ]+@[^@ ]+\\.[^@ ]+');
        if (!EMAIL_REGEX.test(email)) {
            return {
                validateStatus: 'error',
                errorMsg: 'Email not valid'
            }
        }

        if (email.length > EMAIL_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Email is too long (Maximum ${EMAIL_MAX_LENGTH} characters allowed)`
            }
        }

        return {
            validateStatus: 'success',
            errorMsg: null
        }
    };

    return (
        <div className="signup-container">
            <h1 className="page-title">Forgot password</h1>
            <div className="signup-content">
                <Form onSubmitCapture={handleSubmit} className="signup-form">
                    <FormItem
                        label="Email"
                        hasFeedback
                        validateStatus={email.validateStatus}
                        help={email.errorMsg}
                    >
                        <Input
                            size="large"
                            name="email"
                            type="email"
                            autoComplete="off"
                            placeholder="Your email"
                            value={email.value}
                            onChange={(event) => handleInputChange(event, validateEmail)}
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
                            Password recovery
                        </Button>
                        <br/>
                        <br/>
                        Already registered? <Link to="/login">Login now!</Link>
                    </FormItem>
                </Form>
            </div>
        </div>
    );
};

export default ForgotPassword;


