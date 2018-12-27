import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {ACCESS_TOKEN, EMAIL_MAX_LENGTH} from '../../constants';

import { Form, Input, Button, notification } from 'antd';
import {forgotPassword} from "../../util/APIUtils";
const FormItem = Form.Item;

class ForgotPassword extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: {
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

        const forgotPasswordRequest = {
            email: this.state.email.value
        };
        forgotPassword(forgotPasswordRequest)
            .then(response => {
                notification.success({
                    message: 'Polling App',
                    description: "Thank you! We have successfully send you email. Please check email to confirm!",
                });
                //this.props.history.push("/login");
            }).catch(error => {
            notification.error({
                message: 'Polling App',
                description: "GRESKA: " + error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    }

    isFormInvalid() {
        return !(  this.state.email.validateStatus === 'success' );
    }


    render() {
        return (
            <div className="signup-container">
                <h1 className="page-title">Forgot password</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem
                            label="Email"
                            hasFeedback
                            validateStatus={this.state.email.validateStatus}
                            help={this.state.email.errorMsg}>
                            <Input
                                size="large"
                                name="email"
                                type="email"
                                autoComplete="off"
                                placeholder="Your email"
                                value={this.state.email.value}
                                onChange={(event) => this.handleInputChange(event, this.validateEmail)}/>
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className="signup-form-button"
                                    disabled={this.isFormInvalid()} >Password recovery</Button>
                            Already registed? <Link to="/login">Login now!</Link>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

    validateEmail = (email) => {
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
    }



}

export default ForgotPassword;