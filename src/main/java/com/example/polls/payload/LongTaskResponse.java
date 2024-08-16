package com.example.polls.payload;

import jakarta.validation.constraints.NotBlank;


public class LongTaskResponse {
    @NotBlank
    private String longTaskResponse;

    public @NotBlank String getLongTaskResponse() {
        return longTaskResponse;
    }

    public void setLongTaskResponse(@NotBlank String longTaskResponse) {
        this.longTaskResponse = longTaskResponse;
    }
}
