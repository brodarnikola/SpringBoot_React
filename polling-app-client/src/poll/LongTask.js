import React, {useEffect, useState} from 'react';
import {Avatar, Button, Form, notification} from 'antd';

import {checkEmailAvailability, getAccounts, getPreviousMonthTurnover, longTask} from "../util/APIUtils";

const LongTask = () => {
    const [longTaskData, setLongTaskData] = useState("");

    const [accountList, setAccountList] = useState([]) // useState({ accountNumber: '', balance: '', pastMonthTurnover: '', name: '' });

    useEffect(() => {
        longTask()
            .then(response => {
                setLongTaskData(response.longTaskResponse);
            }).catch(error => {
            if (error.status === 404) {
                notification.error({
                    message: 'Polling App',
                    description: 'Something is wrong with long task'
                });
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });

        getAccounts()
            .then(response => {
                console.log("data is: " + response);
                response.forEach(data => {
                    console.log("print data is: " + data.accountNumber);
                })
                setAccountList(response);
            }).catch(error => {
            if (error.status === 404) {
                notification.error({
                    message: 'Polling App',
                    description: 'Something is wrong with long task'
                });
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    }, []);

    const calculatePreviousMonthTurnover = () => {

        getPreviousMonthTurnover()
            .then(response => {
                console.log("data is: " + response);
                response.forEach(data => {
                    console.log("print data is: " + data.accountNumber);
                })
                setAccountList(response);
        }).catch(error => {
            notification.error({
                message: 'Polling App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    };

    return (
        <div className="signup-container">
            <h1 className="page-title">Long task example</h1>
            <div className="signup-content">
                <p>Response from long task is: {longTaskData}</p>
            </div>
    <div  >
                <p>To see calculate previous month turnover, you click on this button, or wait. This scheduler, cron job
                    is started every week day at 12 </p>
                <button onClick={calculatePreviousMonthTurnover}
                        style={{
                            border: '2px solid #000',
                            backgroundColor: '#2783F8FF',
                            color: '#fff',
                            padding: '10px 20px',
                            fontSize: '16px',
                            textAlign: 'center',
                            textDecoration: 'none',
                            display: 'inline-block',
                            cursor: 'pointer',
                        }}>Calculate month turnover for each account</button>
            </div>
            <br/>
            {/* Use .map() to return JSX for each account */}
            {accountList.length > 0 ? (
                accountList.map((account, index) => (
                    <div className="poll-content" key={index}>
                        <div className="poll-header">
                            <span>{account.customer.name}</span>
                            <div className="poll-question">
                                <p>Account Number: {account.accountNumber}</p>
                            </div>
                        </div>
                        <div className="poll-choices">
                            <p>Balance: {account.balance.toFixed(2)}</p>
                        </div>
                        <div className="poll-footer">
                            <p>Past Month Turnover: {account.pastMonthTurnover.toFixed(2)}</p>
                        </div>
                    </div>
                ))
            ) : (
                <p>No accounts available</p>
            )}
        </div>
    );
};

export default LongTask;


