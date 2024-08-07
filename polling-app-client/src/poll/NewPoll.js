import React, { useState } from 'react';
import { createPoll } from '../util/APIUtils';
import { MAX_CHOICES, POLL_QUESTION_MAX_LENGTH, POLL_CHOICE_MAX_LENGTH } from '../constants';
import './NewPoll.css';
import { Form, Input, Button, Select, Col, notification } from 'antd';
import { PlusOutlined, CloseOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const { Option } = Select;
const FormItem = Form.Item;

const NewPoll = (props) => {
    const [question, setQuestion] = useState({ text: '', validateStatus: '', errorMsg: '' });
    const [choices, setChoices] = useState([{ text: '' }, { text: '' }]);
    const [pollLength, setPollLength] = useState({ days: 1, hours: 0 });

    const addChoice = () => {
        setChoices([...choices, { text: '' }]);
    };

    const removeChoice = (choiceNumber) => {
        setChoices(choices.filter((_, index) => index !== choiceNumber));
    };

    const handleQuestionChange = (event) => {
        const value = event.target.value;
        setQuestion({
            text: value,
            ...validateQuestion(value)
        });
    };

    const handleChoiceChange = (event, index) => {
        const value = event.target.value;
        const updatedChoices = choices.slice();
        updatedChoices[index] = {
            text: value,
            ...validateChoice(value)
        };
        setChoices(updatedChoices);
    };

    const handlePollDaysChange = (value) => {
        setPollLength((prevState) => ({ ...prevState, days: value }));
    };

    const handlePollHoursChange = (value) => {
        setPollLength((prevState) => ({ ...prevState, hours: value }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const pollData = {
            question: question.text,
            choices: choices.map(choice => ({ text: choice.text })),
            pollLength: pollLength
        };

        createPoll(pollData)
            .then(response => {
                props.history.push("/");
            }).catch(error => {
            if (error.status === 401) {
                props.handleLogout('/login', 'error', 'You have been logged out. Please login create poll.');
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    };

    const validateQuestion = (questionText) => {
        if (questionText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter your question!'
            };
        } else if (questionText.length > POLL_QUESTION_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Question is too long (Maximum ${POLL_QUESTION_MAX_LENGTH} characters allowed)`
            };
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        }
    };

    const validateChoice = (choiceText) => {
        if (choiceText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter a choice!'
            };
        } else if (choiceText.length > POLL_CHOICE_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Choice is too long (Maximum ${POLL_CHOICE_MAX_LENGTH} characters allowed)`
            };
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            };
        }
    };

    const isFormInvalid = () => {
        if (question.validateStatus !== 'success') {
            return true;
        }
        for (let i = 0; i < choices.length; i++) {
            const choice = choices[i];
            if (choice.validateStatus !== 'success') {
                return true;
            }
        }
        return false;
    };

    const choiceViews = choices.map((choice, index) => (
        <PollChoice
            key={index}
            choice={choice}
            choiceNumber={index}
            removeChoice={removeChoice}
            handleChoiceChange={handleChoiceChange}
        />
    ));

    return (
        <div className="new-poll-container">
            <h1 className="page-title">Create Poll</h1>
            <div className="new-poll-content">
                <Form onSubmitCapture={handleSubmit} className="create-poll-form">
                    <FormItem
                        validateStatus={question.validateStatus}
                        help={question.errorMsg}
                        className="poll-form-row"
                    >
                        <TextArea
                            placeholder="Enter your question"
                            style={{ fontSize: '16px' }}
                            autoSize={{ minRows: 3, maxRows: 6 }}
                            name="question"
                            value={question.text}
                            onChange={handleQuestionChange}
                        />
                    </FormItem>
                    {choiceViews}
                    <FormItem className="poll-form-row">
                        <Button
                            type="dashed"
                            onClick={addChoice}
                            disabled={choices.length === MAX_CHOICES}
                        >
                            <PlusOutlined /> Add a choice
                        </Button>
                    </FormItem>
                    <FormItem className="poll-form-row">
                        <Col xs={24} sm={14}>
                            Poll length:
                        </Col>
                        <br/>
                        <Col xs={24} sm={25}>
                            <span style={{ marginRight: '18px' }}>
                                Days &nbsp;
                                <Select
                                    name="days"
                                    value={pollLength.days}
                                    onChange={handlePollDaysChange}
                                    style={{ width: 60 }}
                                >
                                    {Array.from(Array(8).keys()).map(i => (
                                        <Option key={i} value={i}>
                                            {i}
                                        </Option>
                                    ))}
                                </Select> &nbsp;
                            </span>
                            <span>
                                Hours &nbsp;
                                <Select
                                    name="hours"
                                    value={pollLength.hours}
                                    onChange={handlePollHoursChange}
                                    style={{ width: 60 }}
                                >
                                    {Array.from(Array(24).keys()).map(i => (
                                        <Option key={i} value={i}>
                                            {i}
                                        </Option>
                                    ))}
                                </Select> &nbsp;
                            </span>
                        </Col>
                    </FormItem>
                    <FormItem className="poll-form-row">
                        <Button
                            type="primary"
                            htmlType="submit"
                            size="large"
                            disabled={isFormInvalid()}
                            className="create-poll-form-button"
                        >
                            Create Poll
                        </Button>
                    </FormItem>
                </Form>
            </div>
        </div>
    );
};

const PollChoice = ({ choice, choiceNumber, removeChoice, handleChoiceChange }) => (
    <FormItem
        validateStatus={choice.validateStatus}
        help={choice.errorMsg}
        className="poll-form-row"
    >
        <Input
            placeholder={`Choice ${choiceNumber + 1}`}
            size="large"
            value={choice.text}
            className={choiceNumber > 1 ? "optional-choice" : null}
            onChange={(event) => handleChoiceChange(event, choiceNumber)}
        />
        {choiceNumber > 1 && (
            <CloseOutlined
                className="dynamic-delete-button"
                onClick={() => removeChoice(choiceNumber)}
            />
        )}
    </FormItem>
);

export default NewPoll;