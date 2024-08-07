import React, { Component } from 'react';
import {delete_Poll, getAllPolls, getUserCreatedPolls, getUserVotedPolls} from '../util/APIUtils';
import Poll from './Poll';
import { castVote } from '../util/APIUtils';
import CustomLoadingIndicator  from '../common/CustomLoadingIndicator';
import { Button, notification } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { POLL_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './PollList.css';

class PollList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            polls: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false,
            isDeletingPoll: false
        };
        this.loadPollList = this.loadPollList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadPollList(page = 0, size = POLL_LIST_SIZE) {
        let promise;
        if(this.props.username) {
            if(this.props.type === 'USER_CREATED_POLLS') {
                promise = getUserCreatedPolls(this.props.username, page, size);

            } else if (this.props.type === 'USER_VOTED_POLLS') {
                promise = getUserVotedPolls(this.props.username, page, size);
            }
        } else {
            promise = getAllPolls(page, size);
        }

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            const polls = this.state.polls.slice();
            const currentVotes = this.state.currentVotes.slice();

            this.setState({
                polls: polls.concat(response.content),
                page: response.page,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                last: response.last,
                currentVotes: currentVotes.concat(Array(response.content.length).fill(null)),
                isLoading: false
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });  
        
    }

    componentDidMount() {

        this.loadPollList();
    }

    componentDidUpdate(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({

                polls: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });    
            this.loadPollList();
        }
    }

    handleLoadMore() {
        this.loadPollList(this.state.page + 1);
    }

    handleVoteChange(event, pollIndex) {
        const currentVotes = this.state.currentVotes.slice();
        currentVotes[pollIndex] = event.target.value;

        this.setState({
            currentVotes: currentVotes
        });
    }

    deletePoll(event, pollIndex) {
        event.preventDefault();

        this.setState({ isDeletingPoll: true});
        if (window.confirm('Are you sure you wish to delete this item?')) {

            if(!this.props.isAuthenticated) {
                this.props.history.push("/login");
                notification.info({
                    message: 'Polling App',
                    description: "Please login as admin to delete.",
                });
                return;
            }

            const poll = this.state.polls[pollIndex];

            const voteData = {
                pollId: poll.id
            };

            delete_Poll(voteData)
                .then(response => {

                    const newPollList = this.state.polls.filter( polls => {
                        return polls.id !==  poll.id;
                    });

                    this.setState({
                        polls: newPollList,
                        isDeletingPoll: false
                    });

                    notification.success({
                        message: 'Polling App',
                        description: "You have sucessfully deleted poll.",
                    });

                }).catch(error => {
                if(error.status === 401) {
                    this.setState({ isDeletingPoll: false});
                    this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');
                } else {

                    this.setState({ isDeletingPoll: false});
                    notification.error({
                        message: 'Polling App',
                        description:  "GRESKA: "  + error.message  || 'Sorry! Something went wrong. Please try again!'
                    });
                }
            });
        }

        this.setState({ isDeletingPoll: false});
    }


    handleVoteSubmit(event, pollIndex) {
        event.preventDefault();
        if(!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Polling App',
                description: "Please login to vote.",          
            });
            return;
        }

        const poll = this.state.polls[pollIndex];
        const selectedChoice = this.state.currentVotes[pollIndex];

        const voteData = {
            pollId: poll.id,
            choiceId: selectedChoice
        };

        castVote(voteData)
        .then(response => {
            const polls = this.state.polls.slice();
            polls[pollIndex] = response;
            this.setState({
                polls: polls
            });        
        }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');    
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });                
            }
        });
    }

    render() {


        const pollViews = [];
        if( this.props.currentUser != null ) {

            this.state.polls.forEach((poll, pollIndex) => {
                pollViews.push(<Poll
                    key={poll.id}
                    poll={poll}
                    currentVote={this.state.currentVotes[pollIndex]}
                    deletePoll={(event) => this.deletePoll(event, pollIndex)}
                    handleVoteChange={(event) => this.handleVoteChange(event, pollIndex)}
                    handleVoteSubmit={(event) => this.handleVoteSubmit(event, pollIndex)}
                    roleAdminOrUser={this.props.currentUser.roles}
                />)
            });
        }
        else {
            this.state.polls.forEach((poll, pollIndex) => {
                pollViews.push(<Poll
                    key={poll.id}
                    poll={poll}
                    currentVote={this.state.currentVotes[pollIndex]}
                    deletePoll={(event) => this.deletePoll(event, pollIndex)}
                    handleVoteChange={(event) => this.handleVoteChange(event, pollIndex)}
                    handleVoteSubmit={(event) => this.handleVoteSubmit(event, pollIndex)}
                />)
            });
        }

        if (this.state.isDeletingPoll) {
            return <CustomLoadingIndicator/> ;
        }

        return (
            <div className="polls-container">
                {pollViews}
                {
                    !this.state.isLoading && this.state.polls.length === 0 ? (
                        <div className="no-polls-found">
                        </div>    
                    ): null
                }  
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-polls"> 
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <PlusOutlined type="plus" /> Load more
                            </Button>
                        </div>): null
                }              
                {
                    this.state.isLoading ? 
                    <CustomLoadingIndicator />: null
                }
            </div>
        );
    }
}

export default withRouter(PollList);