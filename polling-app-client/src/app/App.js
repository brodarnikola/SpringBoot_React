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
import ForgotPassword from '../user/forgotPassword/ForgotPassword';
import Profile from '../user/profile/Profile';
import AppHeader from '../common/AppHeader';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import PrivateRoute from '../common/PrivateRoute';

import {Layout, notification} from 'antd';
import ProfileEdit from "../poll/ProfileEdit";
import ChangePassword from "../user/changePassword/ChangePassword";

const {Content} = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false,
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

    loadCurrentUser() {
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

        notification[notificationType]({
            message: 'Polling App',
            description: description,
        });
    }

    handleLogin() {
        notification.success({
            message: 'Polling App',
            description: "You're successfully logged in.",
        });
        this.loadCurrentUser();
        this.props.history.push("/");
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
                                   render={(props) => <Login onLogin={this.handleLogin} {...props} />}></Route>
                            <Route path="/signup" component={Signup}></Route>
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
                            <Route component={NotFound}></Route>
                        </Switch>
                    </div>
                </Content>
            </Layout>
        );
    }
}

export default withRouter(App);
