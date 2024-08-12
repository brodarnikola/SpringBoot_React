import React from 'react';
import {login} from '../../util/APIUtils';
import './Login.css';
import {Link} from 'react-router-dom';
import {ACCESS_TOKEN} from '../../constants';

import {Form, Input, Button, notification} from 'antd';
import {LockOutlined, UserOutlined} from '@ant-design/icons';

import GithubIcon from "mdi-react/GithubIcon";
import {getSocialLoginUrl} from "../../util/Helpers";

const Login = (props) => {
    return (
        <div className="login-container">
            <h1 className="page-title">Login</h1>
            <div className="login-content">
                <LoginForm onLogin={props.onLogin}/>
            </div>
        </div>
    );
}

const LoginForm = (props) => {
    const [form] = Form.useForm();

    const handleSubmit = async (values) => {
        try {
            const loginRequest = {...values};
            const response = await login(loginRequest);
            localStorage.setItem(ACCESS_TOKEN, response.accessToken);
            props.onLogin();
        } catch (error) {
            if (error.status === 401) {
                notification.error({
                    message: 'Polling App',
                    description: 'Your Username or Password is incorrect. Please try again!'
                });
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        }
    };

    // const handleGithubLogin = () => {
    //      window.location.href = 'http://localhost:5000/oauth2/authorization/github';
    // };

    return (

        <>
            <Form
                form={form}
                onFinish={handleSubmit}
                className="login-form"
            >
                <Form.Item
                    name="usernameOrEmail"
                    rules={[{required: true, message: 'Please input your username or email!'}]}
                >
                    <Input
                        prefix={<UserOutlined/>}
                        size="large"
                        placeholder="Username or Email"
                    />
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{required: true, message: 'Please input your Password!'}]}
                >
                    <Input
                        prefix={<LockOutlined/>}
                        size="large"
                        type="password"
                        placeholder="Password"
                    />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" size="large" className="login-form-button">
                        Login
                    </Button>
                    <br/>
                    <br/>
                    Or <Link to="/signup">register now!</Link> <br/>
                    Forgot your password? <Link to="/forgotPassword">Click here to recover it.</Link>
                </Form.Item>
            </Form>
            <br/>
            <br/>
            <a
                // className="login-link"
                // href={`https://github.com/login/oauth/authorize?scope=user&client_id=${client_id}&redirect_uri=${redirect_uri}`}
                // onClick={() => {
                //     // setData({...data, errorMessage: ""});
                // }}
                href={
                    getSocialLoginUrl('github')
                }
            >
                <GithubIcon/>
                <span>Login with GitHub</span>
            </a>
        </>
    )
        ;
}

export default Login;