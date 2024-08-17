import React, {Component} from 'react';
import './App.css';
import {
    Route,
    withRouter,
    Switch
} from 'react-router-dom';

import {getCurrentUser} from '../util/APIUtils';
import {ACCESS_TOKEN} from '../constants';

import PollList from '../poll/PollList';
import NewPoll from '../poll/NewPoll';
import Login from '../user/login/Login';
import Signup from '../user/signup/Signup';
import SignUpConfirm from '../user/signUpConfirm/SignUpConfirm';
import ForgotPassword from '../user/forgotPassword/ForgotPassword';
import Profile from '../user/profile/Profile';
import AppHeader from '../common/AppHeader';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import PrivateRoute from '../common/PrivateRoute';

import {Layout, notification} from 'antd';
import ProfileEdit from "../poll/ProfileEdit";
import ChangePassword from "../user/changePassword/ChangePassword";
import OAuth2Redirect from "../user/login/OAuth2Redirect";
import LongTask from "../poll/LongTask";

const {Content} = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false,
            isSocialLogin: false,
            numberOfDisplayedLoginNotification: 0,
            id: "",
            showUserAdminMenu: false
        }
        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

        notification.config({
            placement: 'topRight',
            top: 70,
            duration: 3,
        });
    }

    loadCurrentUser( ) {
        this.setState({
            isLoading: true
        });
        getCurrentUser()
            .then(response => {
                this.setState({
                    currentUser: response,
                    isAuthenticated: true,
                    isLoading: false,
                    showUserAdminMenu: true
                });
                console.log("ispis podatki od usera:" + " 1) username: " + this.state.currentUser.username
                        + "   2) authorities: " + this.state.currentUser.roles + " kompletni ispis: " + this.state.currentUser);

                this.props.history.push('/');

            }).catch(error => {
            this.setState({
                isLoading: false,
                showUserAdminMenu: false
            });
        });
    }

    componentDidMount() {
        this.loadCurrentUser();
    }

    handleLogout(redirectTo = "/", notificationType = "success", description = "You're successfully logged out.") {
        localStorage.removeItem(ACCESS_TOKEN);

        this.setState({
            currentUser: null,
            isAuthenticated: false,
            showUserAdminMenu: false
        });

        this.props.history.push(redirectTo);
    }

    handleLogin(isSocialLogin = false) {

        this.setState({
            isSocialLogin: isSocialLogin
        })

        if(this.state.numberOfDisplayedLoginNotification === 0 && !isSocialLogin) {
            notification.success({
                message: 'Polling App',
                description: "You're successfully logged in.",
            });
            this.setState({
                numberOfDisplayedLoginNotification: 1
            })
        }
        else if (this.state.isSocialLogin) {
            notification.success({
                message: 'Polling App',
                description: "You're successfully logged in.",
            });
            this.setState({
                isSocialLogin: false
            })
        }

        this.loadCurrentUser();
    }

    render() {
        if (this.state.isLoading) {
            return <LoadingIndicator/>
        }
        return (
            <Layout className="app-container">
                <AppHeader isAuthenticated={this.state.isAuthenticated}
                           currentUser={this.state.currentUser}
                           onLogout={this.handleLogout}
                           showUserAdminMenu={this.state.showUserAdminMenu}
                />

                <Content className="app-content">
                    <div className="container">
                        <Switch>
                            <Route exact path="/"
                                   render={(props) => <PollList isAuthenticated={this.state.isAuthenticated}
                                                                currentUser={this.state.currentUser}
                                                                handleLogout={this.handleLogout} {...props} />}>
                            </Route>
                            <Route path="/login"
                                   render={(props) => <Login onLogin={(isSocialLogin) => this.handleLogin(false)} {...props} />}></Route>

                            <Route path="/oauth2/redirect"
                                   render={(props) => <OAuth2Redirect onLogin={(isSocialLogin) =>
                                       this.handleLogin(true)
                                   } {...props} />}
                                   // component={<OAuth2Redirect />}
                            />

                            <Route path="/signup" component={Signup}></Route>
                            <Route path="/signUpConfirm/:token?"
                                   render={(props) => <SignUpConfirm  {...props} />} ></Route>
                            <Route path="/forgotPassword" component={ForgotPassword}></Route>
                            <Route path="/changePassword/:id?/:token?"
                                   render={(props) => <ChangePassword  {...props} />}
                                /* component={ChangePassword} */ ></Route>
                            <Route path="/users/:username"
                                   render={(props) => <Profile isAuthenticated={this.state.isAuthenticated}
                                                               currentUser={this.state.currentUser} {...props}  />}>
                            </Route>
                            <PrivateRoute authenticated={this.state.isAuthenticated}
                                          path="/poll/new" component={NewPoll}
                                          handleLogout={this.handleLogout}></PrivateRoute>
                            <Route path="/profileEdit/:username"
                                   render={(props) =>
                                       <ProfileEdit isAuthenticated={this.state.isAuthenticated}
                                                    currentUser={this.state.currentUser}
                                                    {...props}  />}>
                            </Route>
                            <Route path="/longTask" component={LongTask}></Route>
                            <Route component={NotFound}></Route>
                        </Switch>
                    </div>
                </Content>
            </Layout>
        );
    }
}

export default withRouter(App);
