import React, { useState } from 'react';
import { signup, checkUsernameAvailability, checkEmailAvailability } from '../../util/APIUtils';
import './Signup.css';
import { Link } from 'react-router-dom';
import {
    NAME_MIN_LENGTH, NAME_MAX_LENGTH,
    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH,
    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH
} from '../../constants';
import { Form, Input, Button, notification } from 'antd';

const { Item: FormItem } = Form;

const Signup = (props) => {
    const [form] = Form.useForm();
    const [formState, setFormState] = useState({
        name: '',
        username: '',
        email: '',
        password: '',
        sifra: ''
    });
    const [validation, setValidation] = useState({
        name: null,
        username: null,
        email: null,
        password: null,
        // sifra: null
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;

        setFormState((prevState) => ({
            ...prevState,
            [name]: value
        }));

        // Run validation function based on the input field
        let validationResult;
        switch (name) {
            case 'name':
                validationResult = validateName(value);
                break;
            case 'username':
                validationResult = validateUsername(value);
                break;
            case 'email':
                validationResult = validateEmail(value);
                break;
            case 'password':
                validationResult = validatePassword(value);
                break;
            // case 'sifra':
            //     validationResult = validateSifra(value);
            //     break;
            default:
                validationResult = { validateStatus: null, errorMsg: null };
        }

        setValidation((prevValidation) => ({
            ...prevValidation,
            [name]: validationResult
        }));
    };

    const handleSubmit = (values) => {

        console.log("request values are 1: ")
        console.log(values)

        if( typeof values.sifra === 'undefined' ) {
            console.log('sifra is undefined');
            values.sifra = ''
        }

        console.log("request values are 2: ")
        console.log(values)

        const signupRequest = {
            name: values.name,
            email: values.email,
            username: values.username,
            password: values.password,
            sifra: values.sifra
        };

        console.log("sign up request is: ")

        console.log(signupRequest)

        signup(signupRequest)
            .then(response => {
                notification.success({
                    message: 'Polling App',
                    description: "Thank you! We have sent you an email to confirm your account. Please check your inbox or spam in your mail account.",
                });
                props.history.push("/login");
            }).catch(error => {
            notification.error({
                message: 'Polling App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    };

    const validateName = (name) => {
        if (name.length < NAME_MIN_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)` };
        } else if (name.length > NAME_MAX_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)` };
        }
        return { validateStatus: 'success', errorMsg: null };
    };

    const validateEmail = (email) => {
        if (!email) {
            return { validateStatus: 'error', errorMsg: 'Email may not be empty' };
        }
        const EMAIL_REGEX = RegExp('[^@ ]+@[^@ ]+\\.[^@ ]+');
        if (!EMAIL_REGEX.test(email)) {
            return { validateStatus: 'error', errorMsg: 'Email not valid' };
        }
        if (email.length > EMAIL_MAX_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Email is too long (Maximum ${EMAIL_MAX_LENGTH} characters allowed)` };
        }
        return { validateStatus: 'success', errorMsg: null };
    };

    const validateUsername = (username) => {
        if (username.length < USERNAME_MIN_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Username is too short (Minimum ${USERNAME_MIN_LENGTH} characters needed.)` };
        } else if (username.length > USERNAME_MAX_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Username is too long (Maximum ${USERNAME_MAX_LENGTH} characters allowed.)` };
        }
        return { validateStatus: 'success', errorMsg: null };
    };

    const validatePassword = (password) => {
        if (password.length < PASSWORD_MIN_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)` };
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return { validateStatus: 'error', errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)` };
        }
        return { validateStatus: 'success', errorMsg: null };
    };

    // const validateSifra = (sifra) => {
    //     return { validateStatus: 'success', errorMsg: null };
    // };

    return (
        <div className="signup-container">
            <h1 className="page-title">Sign Up</h1>
            <div className="signup-content">
                <Form
                    form={form}
                    onFinish={handleSubmit}
                    className="signup-form"
                >
                    <FormItem
                        name="name"
                        label="Full Name"
                        validateStatus={validation.name?.validateStatus}
                        help={validation.name?.errorMsg}
                    >
                        <Input
                            size="large"
                            name="name"
                            autoComplete="off"
                            placeholder="Your full name"
                            value={formState.name}
                            onChange={handleInputChange}
                        />
                    </FormItem>
                    <FormItem
                        name="username"
                        label="Username"
                        hasFeedback
                        validateStatus={validation.username?.validateStatus}
                        help={validation.username?.errorMsg}
                    >
                        <Input
                            size="large"
                            name="username"
                            autoComplete="off"
                            placeholder="A unique username"
                            value={formState.username}
                            onChange={handleInputChange}
                            onBlur={() => {
                                validateUsernameAvailability(formState.username);
                            }}
                        />
                    </FormItem>
                    <FormItem
                        name="email"
                        label="Email"
                        hasFeedback
                        validateStatus={validation.email?.validateStatus}
                        help={validation.email?.errorMsg}
                    >
                        <Input
                            size="large"
                            name="email"
                            type="email"
                            autoComplete="off"
                            placeholder="Your email"
                            value={formState.email}
                            onChange={handleInputChange}
                            onBlur={() => {
                                validateEmailAvailability(formState.email);
                            }}
                        />
                    </FormItem>
                    <FormItem
                        name="password"
                        label="Password"
                        hasFeedback
                        validateStatus={validation.password?.validateStatus}
                        help={validation.password?.errorMsg}
                    >
                        <Input
                            size="large"
                            name="password"
                            type="password"
                            autoComplete="off"
                            placeholder="A password between 6 to 20 characters"
                            value={formState.password}
                            onChange={handleInputChange}
                        />
                    </FormItem>
                    <FormItem
                        name="sifra"
                        label="Code for admin:"
                        // validateStatus={validation.sifra?.validateStatus}
                        help={validation.sifra?.errorMsg}
                    >
                        <Input
                            size="large"
                            name="sifra"
                            type="password"
                            placeholder="Unesite šifru"
                            value={formState.sifra}
                            onChange={handleInputChange}
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
                            Sign up
                        </Button>
                        Already registered? <Link to="/login">Login now!</Link>
                    </FormItem>
                </Form>
            </div>
        </div>
    );

    function isFormInvalid() {
        return !(validation.name?.validateStatus === 'success' &&
            validation.username?.validateStatus === 'success' &&
            validation.email?.validateStatus === 'success' &&
            validation.password?.validateStatus === 'success'
        );
    }

    function validateUsernameAvailability(username) {
        const usernameValidation = validateUsername(username);
        if (usernameValidation.validateStatus === 'error') {
            setValidation((prevValidation) => ({
                ...prevValidation,
                username: usernameValidation
            }));
            return;
        }

        setValidation((prevValidation) => ({
            ...prevValidation,
            username: {
                validateStatus: 'validating',
                errorMsg: null
            }
        }));

        checkUsernameAvailability(username)
            .then(response => {
                if (response.available) {
                    setValidation((prevValidation) => ({
                        ...prevValidation,
                        username: {
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    }));
                } else {
                    setValidation((prevValidation) => ({
                        ...prevValidation,
                        username: {
                            validateStatus: 'error',
                            errorMsg: 'This username is already taken'
                        }
                    }));
                }
            }).catch(error => {
            setValidation((prevValidation) => ({
                ...prevValidation,
                username: {
                    validateStatus: 'success',
                    errorMsg: null
                }
            }));
        });
    }

    function validateEmailAvailability(email) {
        const emailValidation = validateEmail(email);
        if (emailValidation.validateStatus === 'error') {
            setValidation((prevValidation) => ({
                ...prevValidation,
                email: emailValidation
            }));
            return;
        }

        setValidation((prevValidation) => ({
            ...prevValidation,
            email: {
                validateStatus: 'validating',
                errorMsg: null
            }
        }));

        checkEmailAvailability(email)
            .then(response => {
                if (response.available) {
                    setValidation((prevValidation) => ({
                        ...prevValidation,
                        email: {
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    }));
                } else {
                    setValidation((prevValidation) => ({
                        ...prevValidation,
                        email: {
                            validateStatus: 'error',
                            errorMsg: 'This email is already registered'
                        }
                    }));
                }
            }).catch(error => {
            setValidation((prevValidation) => ({
                ...prevValidation,
                email: {
                    validateStatus: 'success',
                    errorMsg: null
                }
            }));
        });
    }
};

export default Signup;