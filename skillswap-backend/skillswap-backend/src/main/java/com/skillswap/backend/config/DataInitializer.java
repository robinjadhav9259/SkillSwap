package com.skillswap.backend.config;

import com.skillswap.backend.model.CoinTransaction;
import com.skillswap.backend.model.SkillSession;
import com.skillswap.backend.model.Student;
import com.skillswap.backend.repository.CoinTransactionRepository;
import com.skillswap.backend.repository.SkillSessionRepository;
import com.skillswap.backend.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Initializes realistic sample student profiles, skill sessions, and transaction ledger
 * if the database is currently empty.
 * This ensures the project is 100% demo-ready for college ED presentations.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final SkillSessionRepository sessionRepository;
    private final CoinTransactionRepository transactionRepository;

    public DataInitializer(StudentRepository studentRepository, 
                           SkillSessionRepository sessionRepository, 
                           CoinTransactionRepository transactionRepository) {
        this.studentRepository = studentRepository;
        this.sessionRepository = sessionRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            System.out.println("SkillSwap database already populated with " + studentRepository.count() + " students.");
            return;
        }

        System.out.println("Seeding demo students into persistent H2 database...");

        // 1. Primary Student Profile (Logged-in User)
        Student gargi = new Student(
                "Gargi Dhabale",
                "gargi.dhabale@college.edu",
                "BE Information Technology",
                "2nd Year",
                "Intermediate",
                "BE IT Student • Skill Explorer",
                "Python, SQL, Canva, Public Speaking",
                "C++ DSA, Web Development, AI / ML",
                760,
                18.4,
                18,
                12,
                true,
                "coding",
                85
        );
        studentRepository.save(gargi);

        // 2. Peer Students for Skill Matching
        Student aarav = new Student(
                "Aarav Mehta",
                "aarav.mehta@college.edu",
                "BE Computer Engineering",
                "3rd Year",
                "Advanced",
                "Competitive programmer & DSA enthusiast",
                "C++, DSA, Algorithms",
                "Python, SQL",
                920,
                24.0,
                22,
                15,
                true,
                "coding",
                92
        );

        Student meera = new Student(
                "Meera Shah",
                "meera.shah@college.edu",
                "B.Des Product Design",
                "2nd Year",
                "Intermediate",
                "UI/UX enthusiast & Figma wizard",
                "UI/UX, Figma, React",
                "Canva, Public Speaking",
                540,
                12.0,
                14,
                8,
                true,
                "design",
                85
        );

        Student rohan = new Student(
                "Rohan Patil",
                "rohan.patil@college.edu",
                "BE Data Science",
                "3rd Year",
                "Intermediate",
                "Data analyst & business intelligence",
                "SQL, Power BI, Excel",
                "Python, Web Development",
                680,
                15.5,
                16,
                10,
                false,
                "business",
                82
        );

        Student ananya = new Student(
                "Ananya Rao",
                "ananya.rao@college.edu",
                "BE AI & Data Science",
                "4th Year",
                "Advanced",
                "Deep learning & neural networks researcher",
                "ML, Python, AI",
                "SQL, Canva",
                890,
                28.0,
                25,
                20,
                true,
                "coding",
                89
        );

        Student kabir = new Student(
                "Kabir Joshi",
                "kabir.joshi@college.edu",
                "BA Multimedia",
                "2nd Year",
                "Intermediate",
                "Brand identity and digital artist",
                "Graphic Design, Canva, Branding",
                "Python, Public Speaking",
                450,
                10.0,
                11,
                6,
                false,
                "design",
                78
        );

        Student isha = new Student(
                "Isha Kulkarni",
                "isha.kulkarni@college.edu",
                "BBA Digital Marketing",
                "3rd Year",
                "Advanced",
                "Growth marketing & business scaling",
                "Marketing, Business Strategy",
                "SQL, Web Development",
                710,
                16.0,
                17,
                11,
                true,
                "business",
                87
        );

        studentRepository.save(aarav);
        studentRepository.save(meera);
        studentRepository.save(rohan);
        studentRepository.save(ananya);
        studentRepository.save(kabir);
        studentRepository.save(isha);

        // 3. Seed initial transactions for Gargi
        transactionRepository.save(new CoinTransaction(gargi.getId(), "Completed SQL Session", 20, "CREDIT", "Today"));
        transactionRepository.save(new CoinTransaction(gargi.getId(), "Teaching Canva", 10, "CREDIT", "Yesterday"));
        transactionRepository.save(new CoinTransaction(gargi.getId(), "Completed DSA Exchange", 20, "CREDIT", "2 days ago"));

        // 4. Seed an initial upcoming session
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        SkillSession initialSession = new SkillSession(
                gargi.getId(),
                aarav.getId(),
                aarav.getName(),
                aarav.getInitials(),
                "C++ Binary Search",
                "Learning",
                tomorrow.toString(),
                tomorrow.getDayOfWeek().name(),
                "18:00",
                "19:00",
                60,
                "Online",
                10,
                "SCHEDULED"
        );
        sessionRepository.save(initialSession);

        System.out.println("SkillSwap demo data successfully seeded!");
    }
}
