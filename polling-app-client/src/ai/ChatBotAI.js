import React, {useState} from 'react';
import { notification, Tabs} from 'antd';
import {chatBotAIRequest,} from "../util/APIUtils";

const TabPane = Tabs.TabPane;

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [category, setCategory] = useState('');
    const [year, setYear] = useState('');

    const fetchReply = () => {
        try {

            const chatBotRequest = {
                category: category,
                year: year
            };

            chatBotAIRequest(chatBotRequest)
                .then(response => {

                    setMessages((prevMessages) => [...prevMessages, { text: response.choices[0].text, sender: 'bot' }]);
                }).catch(error => {
                notification.error({
                    message: 'Polling App',
                    description: "GRESKA: " + error.message || 'Sorry! Something went wrong when fetching resposen from AI. Please try again!'
                });
            });

            // const { data } = await axios.post('http://localhost:8080/api/chat', message);
            // setMessages((prevMessages) => [...prevMessages, { text: data.choices[0].text, sender: 'bot' }]);
        } catch (error) {
            console.error("Error fetching reply:", error);
        }
    }
    const handleSend = () => {
        setMessages((prevMessages) => [...prevMessages, { text: `Category: ${category}, Year: ${year}`, sender: 'user' }]);
        fetchReply();
        setCategory('');
        setYear('');
    };

    // const [email, setEmail] = useState({ value: '', validateStatus: '', errorMsg: '' });
    //
    // const handleInputChange = (event, validationFun) => {
    //     const { name, value } = event.target;
    //
    //     if (name === 'email') {
    //         setEmail({
    //             value,
    //             ...validationFun(value)
    //         });
    //     }
    // };
    //
    // const handleSubmit = (event) => {
    //     event.preventDefault();
    //
    //     const forgotPasswordRequest = {
    //         email: email.value
    //     };
    //
    //     forgotPassword(forgotPasswordRequest)
    //         .then(response => {
    //             notification.success({
    //                 message: 'Polling App',
    //                 description: "Thank you! We have successfully sent you an email. Please check your email to confirm!",
    //             });
    //         }).catch(error => {
    //         notification.error({
    //             message: 'Polling App',
    //             description: "GRESKA: " + error.message || 'Sorry! Something went wrong. Please try again!'
    //         });
    //     });
    // };


    return (
        <div className="signup-container">
            <h1 className="page-title">Forgot password</h1>
            <div>
                {messages.map((message, index) => (
                    <p key={index}><strong>{message.sender}:</strong> {message.text}</p>
                ))}
                <div>
                    <p>Enter category</p>
                    <input value={category} onChange={e => setCategory(e.target.value)}/>
                </div>
                <div>
                    <p>Enter year</p>
                    <input value={year} onChange={e => setYear(e.target.value)}/>
                </div>
                <button onClick={handleSend}>Send</button>
            </div>
            {/*<div className="signup-content">*/}
            {/*    <Form onSubmitCapture={handleSubmit} className="signup-form">*/}
            {/*        <FormItem*/}
            {/*            label="Email"*/}
            {/*            hasFeedback*/}
            {/*            validateStatus={email.validateStatus}*/}
            {/*            help={email.errorMsg}*/}
            {/*        >*/}
            {/*            <Input*/}
            {/*                size="large"*/}
            {/*                name="email"*/}
            {/*                type="email"*/}
            {/*                autoComplete="off"*/}
            {/*                placeholder="Your email"*/}
            {/*                value={email.value}*/}
            {/*                onChange={(event) => handleInputChange(event, validateEmail)}*/}
            {/*            />*/}
            {/*        </FormItem>*/}
            {/*        <FormItem>*/}
            {/*            <Button*/}
            {/*                type="primary"*/}
            {/*                htmlType="submit"*/}
            {/*                size="large"*/}
            {/*                className="signup-form-button"*/}
            {/*                disabled={isFormInvalid()}*/}
            {/*            >*/}
            {/*                Password recovery*/}
            {/*            </Button>*/}
            {/*            <br/>*/}
            {/*            <br/>*/}
            {/*            Already registered? <Link to="/login">Login now!</Link>*/}
            {/*        </FormItem>*/}
            {/*    </Form>*/}
            {/*</div>*/}
        </div>
    );
};

export default Chat;