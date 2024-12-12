package com.edee.foundationsforfaith.records;

public record DonationRecord(String donorMessage, Integer donationAmount, String projectName, String stoneEmail) {
    public String thanks() {
        return "Thanks for your donation of " + donationAmount + " to " + projectName + ".";
    }
}
