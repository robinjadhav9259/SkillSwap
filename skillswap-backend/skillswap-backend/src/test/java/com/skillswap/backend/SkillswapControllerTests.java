package com.skillswap.backend;

import com.skillswap.backend.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SkillswapControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("SkillSwap Java Backend is running!"));
    }

    @Test
    public void testGetStudentsAndDefault() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/students/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gargi Dhabale"));
    }

    @Test
    public void testMatchingEndpoint() throws Exception {
        mockMvc.perform(get("/api/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].score").isNumber())
                .andExpect(jsonPath("$[0].explanation").isNotEmpty());
    }

    @Test
    public void testSessionEndpoints() throws Exception {
        String sessionJson = """
            {
                "studentId": 1,
                "partnerId": 2,
                "topic": "Spring Web Services",
                "sessionDate": "2026-10-25",
                "sessionDay": "Sunday",
                "startTime": "15:00",
                "endTime": "16:00",
                "durationMinutes": 60,
                "coins": 10,
                "mode": "Online"
            }
            """;

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        mockMvc.perform(get("/api/sessions/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSkillCoinsEndpoints() throws Exception {
        mockMvc.perform(get("/api/skillcoins/1/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").isNumber());

        String creditJson = """
            {
                "title": "Mock Reward",
                "amount": 25,
                "type": "CREDIT"
            }
            """;

        mockMvc.perform(post("/api/skillcoins/1/transact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(25));

        mockMvc.perform(get("/api/skillcoins/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testChatEndpoints() throws Exception {
        String msgJson = """
            {
                "senderId": 1,
                "receiverId": 2,
                "senderName": "Gargi Dhabale",
                "messageText": "Hello Aarav!"
            }
            """;

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(msgJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/api/messages?student1=1&student2=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
