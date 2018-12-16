package com.example.polls.payload;

import java.math.BigInteger;

public class DeletePollRequest {

    private Long pollId;


    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }
}
