import React, {useEffect, useState} from 'react';
import { Form,  notification } from 'antd';
import {longTask} from "../util/APIUtils";

const LongTask = () => {
    const [longTaskData, setLongTaskData] = useState("");

    useEffect(() => {
        longTask()
            .then(response => {
                // let responseParse = JSON.parse(response);
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
    }, []);

    return (
        <div className="signup-container">
            <h1 className="page-title">Long task example</h1>
            <div className="signup-content">
                <p>Response from long task is: {longTaskData}</p>
            </div>
        </div>
    );
};

export default LongTask;


