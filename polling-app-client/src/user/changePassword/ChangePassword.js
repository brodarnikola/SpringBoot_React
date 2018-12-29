import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {ACCESS_TOKEN, PASSWORD_MAX_LENGTH, PASSWORD_MIN_LENGTH, PASSWORD_DOES_NOT_MATCH} from '../../constants';

import { Form, Input, Button, notification } from 'antd';
import {showChangePasswordPage, savePassword} from "../../util/APIUtils";
const FormItem = Form.Item;

class ChangePassword extends Component {

    constructor(props) {
        super(props);

        this.state = {
            password1: {
                value: ''
            },
            password2: {
                value: ''
            }
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName]: {
                value: inputValue,
                ...validationFun(inputValue)
            }
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        const query = new URLSearchParams(this.props.location.search);

        let id = query.get('id');
        let token = query.get('token');


        showChangePasswordPage(id, token)
            .then(response => {

                console.log("Token je: " + response.accessToken);
                localStorage.setItem(ACCESS_TOKEN, response.accessToken);

                const savePasswordRequest = {
                    password: this.state.password1.value
                };

                savePassword(savePasswordRequest)
                    .then(response => {
                        notification.success({
                            message: 'Polling App',
                            description: "Thank you! You have successfully updated your password!",
                        });
                        this.props.history.push("/login");
                    }).catch(error => {
                    notification.error({
                        message: 'Polling App',
                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                    });
                });
            }).catch(error => {

                if( error.message.includes("Please send one more password recovery.")  ) {

                    this.props.history.push("/login");
                    notification.error({
                        message: 'Polling App',
                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                    });
                }
            });
    }

    isFormInvalid() {
        return !(  this.state.password1.validateStatus === 'success'
                 && this.state.password2.validateStatus === 'success' );
    }

    componentWillMount() {

        localStorage.setItem(ACCESS_TOKEN, "");
    }

    render() {

        return (
            <div className="signup-container">
                <h1 className="page-title">Change password</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem
                            label="New password between 6 to 20 characters"
                            hasFeedback
                            validateStatus={this.state.password1.validateStatus}
                            help={this.state.password1.errorMsg}>
                            <Input
                                size="large"
                                name="password1"
                                type="password"
                                autoComplete="off"
                                placeholder="New password between 6 to 20 characters"
                                value={this.state.password1.value}
                                onChange={(event) => this.handleInputChange(event, this.validatePassword1)}/>
                        </FormItem>
                        <FormItem
                            label="Confirm password"
                            hasFeedback
                            validateStatus={this.state.password2.validateStatus}
                            help={this.state.password2.errorMsg}>
                            <Input
                                size="large"
                                name="password2"
                                type="password"
                                autoComplete="off"
                                placeholder="Confirm password"
                                value={this.state.password2.value}
                                onChange={(event) => this.handleInputChange(event, this.validatePassword2)}/>
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className="signup-form-button"
                                    disabled={this.isFormInvalid()} >Update password</Button>
                            Already registed? <Link to="/login">Login now!</Link>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

    validatePassword1 = (password) => {
        if (password.length < PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
            }
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    validatePassword2 = (password) => {
        if (password.length < PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
            }
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
            }
        } else if (password != this.state.password1.value ) {
            return {
                validationStatus: 'error',
                errorMsg: `Passwords does not matches ${PASSWORD_DOES_NOT_MATCH}.`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }


}

export default ChangePassword;