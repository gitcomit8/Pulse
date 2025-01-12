package com.mirza.pulse.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ChatIdGenerator {

    /**
     * Generates a unique ID for a personal chat between two users.
     * The ID is created by concatenating the two user IDs in lexicographical order,
     * separated by an underscore.
     *
     * @param userId1 The ID of the first user.
     * @param userId2 The ID of the second user.
     * @return A unique string representing the personal chat ID.
     */
    public static String generatePersonalChatId(String userId1, String userId2) {
        // Ensure the order of user IDs is consistent to avoid duplicate chat IDs
        List<String> userIds = Arrays.asList(userId1, userId2);
        Collections.sort(userIds); // Sorts the list lexicographically

        // Concatenate the sorted user IDs with an underscore
        return userIds.get(0) + "_" + userIds.get(1);
    }

    /**
     * Generates a random alphanumeric string of a specified length.
     * This can be used for creating unique IDs for channels or other entities.
     *
     * @param length The desired length of the alphanumeric string.
     * @return A random alphanumeric string.
     */
    public static String generateRandomAlphanumericString(int length) {
        // Use UUID for a simple and effective random string generation
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }
}