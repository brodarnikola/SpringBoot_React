import React, { Component } from 'react';
import {notification} from "antd";
import { signUpConfirm } from "../../util/APIUtils";
import LoadingIndicator from "../../common/LoadingIndicator";

class SignUpConfirm extends Component {

    componentDidMount() {

        const query = new URLSearchParams(this.props.location.search);

        const token = query.get('token');

        signUpConfirm(token)
            .then(response => {

                notification.success({
                    message: 'Polling App',
                    description: "You have confirmed your link. \n Now you can login.",
                });
                this.props.history.push("/login");
                //window.location.reload();
            }).catch(error => {
                notification.error({
                    message: 'Polling App',
                    description:  error.message || 'Sorry! Something went wrong. Please try again!'
                });
                this.props.history.push("/login");
            });
    }

    render() {

        return <LoadingIndicator />;
    }
}

export default SignUpConfirm;