package com.skillswap.backend;

import com.skillswap.backend.dto.MatchResultDto;
import com.skillswap.backend.model.CoinTransaction;
import com.skillswap.backend.model.SkillSession;
import com.skillswap.backend.model.Student;
import com.skillswap.backend.service.MatchingService;
import com.skillswap.backend.service.SessionService;
import com.skillswap.backend.service.SkillCoinService;
import com.skillswap.backend.service.StudentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SkillswapBackendApplicationTests {

	@Autowired
	private StudentService studentService;

	@Autowired
	private MatchingService matchingService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private SkillCoinService skillCoinService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(studentService);
		Assertions.assertNotNull(matchingService);
		Assertions.assertNotNull(sessionService);
		Assertions.assertNotNull(skillCoinService);
	}

	@Test
	void testStudentCreationAndDuplicateEmailPrevention() {
		// Verify seeded student exists
		Student defaultStudent = studentService.getDefaultStudent();
		Assertions.assertNotNull(defaultStudent, "Default student should be seeded");
		Assertions.assertEquals("Gargi Dhabale", defaultStudent.getName());

		// Verify duplicate email is prevented
		Student duplicate = new Student();
		duplicate.setName("Duplicate User");
		duplicate.setEmail(defaultStudent.getEmail());
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			studentService.createStudent(duplicate);
		}, "Should reject duplicate email registration");
	}

	@Test
	void testSkillMatchingAlgorithm() {
		Student gargi = studentService.getDefaultStudent();
		Assertions.assertNotNull(gargi);

		List<MatchResultDto> matches = matchingService.findMatchesForStudent(gargi.getId());
		Assertions.assertFalse(matches.isEmpty(), "Matches should be found for student");

		// Ensure Gargi is not matched with herself
		boolean selfMatch = matches.stream().anyMatch(m -> m.getId().equals(gargi.getId()));
		Assertions.assertFalse(selfMatch, "Student should never match with themselves");

		// Top match should have a high score
		MatchResultDto topMatch = matches.get(0);
		Assertions.assertTrue(topMatch.getScore() >= 80, "Top match score should be high");
		Assertions.assertNotNull(topMatch.getExplanation(), "Match explanation should not be null");
	}

	@Test
	void testSessionSchedulingAndCompletionCoinReward() {
		Student gargi = studentService.getDefaultStudent();
		int initialCoins = gargi.getCoins();

		// Schedule a new 1-hour session
		SkillSession session = new SkillSession();
		session.setStudentId(gargi.getId());
		session.setPartnerId(2L);
		session.setPartnerName("Aarav Mehta");
		session.setTopic("Java Spring Boot");
		session.setSessionDate("2026-10-20");
		session.setSessionDay("Tuesday");
		session.setStartTime("10:00");
		session.setEndTime("11:00");
		session.setDurationMinutes(60);
		session.setCoins(10);
		session.setMode("Online");

		SkillSession created = sessionService.createSession(session);
		Assertions.assertNotNull(created.getId());
		Assertions.assertEquals("SCHEDULED", created.getStatus());

		// Complete session -> triggers automatic coin reward & transaction record
		SkillSession completed = sessionService.completeSession(created.getId());
		Assertions.assertEquals("COMPLETED", completed.getStatus());

		// Verify coins increased by 10
		Integer updatedBalance = skillCoinService.getBalance(gargi.getId());
		Assertions.assertEquals(initialCoins + 10, updatedBalance);

		// Verify transaction history logged
		List<CoinTransaction> txs = skillCoinService.getTransactions(gargi.getId());
		Assertions.assertFalse(txs.isEmpty());
		Assertions.assertTrue(txs.stream().anyMatch(t -> t.getTitle().contains("Java Spring Boot")));
	}

	@Test
	void testNegativeBalancePrevention() {
		Student gargi = studentService.getDefaultStudent();
		Integer currentBalance = skillCoinService.getBalance(gargi.getId());

		// Attempt to debit more than current balance
		int excessiveAmount = currentBalance + 1000;
		Assertions.assertThrows(IllegalStateException.class, () -> {
			skillCoinService.debitCoins(gargi.getId(), excessiveAmount, "Overdraft attempt");
		}, "Should prevent negative SkillCoin balance");
	}
}
