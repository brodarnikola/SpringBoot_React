import React, {Component} from 'react';
import {withRouter} from 'react-router-dom';
import './ProfileEdit.css';
import {
    checkEmailAvailability,
    checkUsernameAvailability,
    getUserProfile,
    updateProfile
} from "../util/APIUtils";
import {
    NAME_MIN_LENGTH, NAME_MAX_LENGTH,
    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH
} from "../constants";
import {Form, Button, notification} from 'antd';

const FormItem = Form.Item;

class ProfileEditCopy extends Component {

    constructor(props) {
        super(props);
        //this.state = {value: ''};
        this.state = {
            user: null,
            isLoading: false,
            username: {
                value: ''
            },
            name: {
                value: ''
            },
            email: {
                value: ''
            },
            hideMesasge: true
        }

        this.handleSubmit = this.handleSubmit.bind(this);

        this.handleInputChange = this.handleInputChange.bind(this);
        this.validateUsernameAvailability = this.validateUsernameAvailability.bind(this);
        this.validateEmailAvailability = this.validateEmailAvailability.bind(this);
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

    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
            .then(response => {
                this.setState({
                    user: response,
                    isLoading: false
                });
            }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });
            }
        });
    }

    componentDidMount() {

        const username = this.props.match.params.username;
        this.loadUserProfile(username);
    }

    handleSubmit(event) {

        /* event.preventDefault();
        const React = require('react');
        console.log(React.version);
        alert('A name was submitted: ' + this.state.username.value); */

        event.preventDefault();

         // prikaz kreiranja objekta, sa properties-ima
        const userOldData = {
            username: this.state.user.username,
            name: this.state.user.name,
            email: this.state.user.email
        }
        // Primjer dodavanja objekta u drugi objekt
        //const updateProfileRequest = {
        //    user: userOldData
        //};

        var newUsername = "";
        if( this.state.username.value === "" ) {
            newUsername = userOldData.username;
        }
        else {
            newUsername = this.state.username.value;
        }

        var newName = "";
        if( this.state.name.value === "" ) {
            newName = userOldData.name;
        }
        else {
            newName = this.state.name.value;
        }

        var newEmail = "";
        if( this.state.email.value === "" ) {
            newEmail = userOldData.email;
        }
        else {
            newEmail = this.state.email.value;
        }

        const updateProfileRequest = {
            oldUsername: this.state.user.username,
            oldName: this.state.user.name,
            oldEmail: this.state.user.email,
            username: newUsername,
            name: newName,
            email: newEmail
        };

        console.log(updateProfileRequest.oldUsername + "  ispis: " + updateProfileRequest.username);
        if( this.state.username.value === '' &&
            this.state.name.value === '' && this.state.email.value === '' ) {
            alert("nista niste promijenili");
            this.setState({hideMessage: false});
            //return ( <p id={"nemaPromjenje"}> Nista niste promijenili </p> );
        }
        else {
            updateProfile(updateProfileRequest)
                .then(response => {

                    notification.success({
                        message: 'Polling App',
                        description: "You have sucessfully update your profile.",
                    });
                    this.props.history.push("/logout");
                    //window.location.reload();
                }).catch(error => {
                notification.error({
                    message: 'Polling App',
                    description:  error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
        }
    }

    isFormInvalid() {
        // mislio sam napraviti da nebi dozvolio klik na button ako se ništa nije promijenilo al nema smisla

        return !(this.state.username.validateStatus === 'success' &&
            this.state.name.validateStatus === 'success' &&
            this.state.email.validateStatus === 'success'
        );
        /* return !(this.state.username.validateStatus === 'success' &&
            this.state.name.validateStatus === 'success' &&
            this.state.email.validateStatus === 'success' &&
            ( this.state.username.value != '' ||  this.state.name.value != '' || this.state.email.value != ''  )
        ); */
    }

    render() {

        return (

           this.state.user ? (
                <div className={"main-profile-edit"}>
                    <Form onSubmit={this.handleSubmit}  className="signup-form" >
                        <FormItem
                            hasFeedback
                            validateStatus={this.state.username.validateStatus}
                            help={this.state.username.errorMsg}>
                            Current username: {this.state.user.username} &nbsp;
                            New:  &nbsp;
                            <input className={"input-username"} type="text"
                                   name="username"
                                   onBlur={this.validateUsernameAvailability}
                                   value={this.state.username.value}
                                   onChange={(event) => this.handleInputChange(event, this.validateUsername)} />
                        </FormItem>
                        <FormItem
                            hasFeedback
                            validateStatus={this.state.name.validateStatus}
                            help={this.state.name.errorMsg}>
                            Current name: {this.state.user.name} &nbsp;
                            New:  &nbsp;
                            <input className={"input-name"}   type="text"
                                   name="name"
                                   value={this.state.name.value}
                                   onChange={(event) => this.handleInputChange(event, this.validateName)}/>
                        </FormItem>

                        <FormItem
                            hasFeedback
                            validateStatus={this.state.email.validateStatus}
                            help={this.state.email.errorMsg}>
                            Current email: {this.state.user.email} &nbsp;
                            New:  &nbsp;
                            <input className={"input-email"} type="email"
                                   name="email"
                                   onBlur={this.validateEmailAvailability}
                                   value={this.state.email.value}
                                   onChange={(event) => this.handleInputChange(event, this.validateEmail)}/>
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    className="update-profile-form-button"
                                    disabled={this.isFormInvalid()}>Sign up</Button>

                        </FormItem>
                    </Form>

                </div>
            ) : null
        );
    }

    validateName = (name) => {

        if( name.length === 0 ) {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        }
        else if (name.length < NAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
            }
        } else if (name.length > NAME_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    validateEmail = (email) => {

        if( email.length === 0 ) {
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
            }
        }

        if (email.length > EMAIL_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Email is too long (Maximum ${EMAIL_MAX_LENGTH} characters allowed)`
            }
        }

        return {
            validateStatus: null,
            errorMsg: null
        }
    }

    validateUsername = (username) => {
        if( username.length === 0 ) {
            return {
                validateStatus: null,
                errorMsg: null
            };
        }
        else if (username.length < USERNAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Username is too short (Minimum ${USERNAME_MIN_LENGTH} characters needed.)`
            }
        } else if (username.length > USERNAME_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Username is too long (Maximum ${USERNAME_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: null,
                errorMsg: null
            }
        }
    }


    validateUsernameAvailability() {
        // First check for client side errors in username
        const usernameValue = this.state.username.value;
        const usernameValidation = this.validateUsername(usernameValue);

        if (usernameValidation.validateStatus === 'error') {
            this.setState({
                username: {
                    value: usernameValue,
                    ...usernameValidation
                }
            });
            return;
        }

        this.setState({
            username: {
                value: usernameValue,
                validateStatus: 'validating',
                errorMsg: null
            }
        });

        checkUsernameAvailability(usernameValue)
            .then(response => {
                if (response.available) {
                    this.setState({
                        username: {
                            value: usernameValue,
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    });
                } else {
                    this.setState({
                        username: {
                            value: usernameValue,
                            validateStatus: 'error',
                            errorMsg: 'This username is already taken'
                        }
                    });
                }
            }).catch(error => {
            // Marking validateStatus as success, Form will be recchecked at server
            this.setState({
                username: {
                    value: usernameValue,
                    validateStatus: 'success',
                    errorMsg: null
                }
            });
        });
    }


    validateEmailAvailability() {
        // First check for client side errors in email
        const emailValue = this.state.email.value;
        const emailValidation = this.validateEmail(emailValue);

        if(emailValidation.validateStatus === 'error') {
            this.setState({
                email: {
                    value: emailValue,
                    ...emailValidation
                }
            });
            return;
        }

        this.setState({
            email: {
                value: emailValue,
                validateStatus: 'validating',
                errorMsg: null
            }
        });

        checkEmailAvailability(emailValue)
            .then(response => {
                if(response.available) {
                    this.setState({
                        email: {
                            value: emailValue,
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    });
                } else {
                    this.setState({
                        email: {
                            value: emailValue,
                            validateStatus: 'error',
                            errorMsg: 'This Email is already registered'
                        }
                    });
                }
            }).catch(error => {
            // Marking validateStatus as success, Form will be recchecked at server
            this.setState({
                email: {
                    value: emailValue,
                    validateStatus: 'success',
                    errorMsg: null
                }
            });
        });
    }


}

export default withRouter(ProfileEditCopy);