import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './AppHeader.css';
import pollIcon from '../poll.svg';
import { Layout, Menu, Dropdown, Icon } from 'antd';
import { HomeOutlined, UserOutlined, DownOutlined } from '@ant-design/icons';

const Header = Layout.Header;
    
class AppHeader extends Component {
    constructor(props) {
        super(props);   
        this.handleMenuClick = this.handleMenuClick.bind(this);   
    }

    handleMenuClick({ key }) {
      if(key === "logout") {
        this.props.onLogout();
      }
    }

    render() {

        console.log("aaa: " + this.props.currentUser);

        let menuItems;
        if( this.props.currentUser !== null && this.props.isAuthenticated === true ) {
          menuItems = [
            <Menu.Item key="/">
              <Link to="/">
                  <HomeOutlined className="nav-icon" />
                {/*<Icon type="home" className="nav-icon" />*/}
              </Link>
            </Menu.Item>,
            <Menu.Item key="/poll/new">
            <Link to="/poll/new">
              <img src={pollIcon} alt="poll" className="poll-icon" />
            </Link>
          </Menu.Item>,
          <Menu.Item key="/profile" className="profile-menu">
                <ProfileDropdownMenu 
                  currentUser={this.props.currentUser} 
                  handleMenuClick={this.handleMenuClick}/>
            </Menu.Item>
          ]; 
        } else {
          menuItems = [
            <Menu.Item key="/login">
              <Link to="/login">Login</Link>
            </Menu.Item>,
            <Menu.Item key="/signup">
              <Link to="/signup">Signup</Link>
            </Menu.Item>                  
          ];
        }

        return (
           <Header className="app-header">
              <div className="app-title" >
                <Link to="/">Polling App</Link>
              </div>
              <Menu
                className="app-menu"
                mode="horizontal"
                selectedKeys={[this.props.location.pathname]}
               >
                  {menuItems}
              </Menu>
          </Header>
        );
    }
}

function ProfileDropdownMenu(props) {
  const dropdownMenu = (
    <Menu onClick={props.handleMenuClick} className="profile-dropdown-menu">
      <Menu.Item key="user-info" className="dropdown-item" disabled>
        <div className="user-full-name-info">
          {props.currentUser.name}
        </div>
        <div className="username-info">
          @{props.currentUser.username}
        </div>
      </Menu.Item>
      <Menu.Divider />
      <Menu.Item key="profile" className="dropdown-item">
        <Link to={`/users/${props.currentUser.username}`}>Polls</Link>
      </Menu.Item>
      <Menu.Item key="longTask" className="dropdown-item">
            <Link to={`/longTask`}>LongTask</Link>
      </Menu.Item>
      <Menu.Item key="editProfile" className="dropdown-item">
            <Link to={`/profileEdit/${props.currentUser.username}`}>Edit Profile</Link>
      </Menu.Item>
      <Menu.Item key="logout" className="dropdown-item">
        Logout
      </Menu.Item>
    </Menu>
  );

  return (
    <Dropdown 
      overlay={dropdownMenu} 
      trigger={['click']}
      getPopupContainer = { () => document.getElementsByClassName('profile-menu')[0]}>
      <a className="ant-dropdown-link">
         {/*<Icon type="user" className="nav-icon" style={{marginRight: 0}} /> <Icon type="down" />*/}
          <UserOutlined className="nav-icon" style={{ marginRight: 0 }} /> <DownOutlined />

      </a>
    </Dropdown>
  );
}


export default withRouter(AppHeader);