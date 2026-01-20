package com.hospital.cleaning.config;

import com.hospital.cleaning.model.*;
import com.hospital.cleaning.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FloorRepository floorRepository;
    private final AreaRepository areaRepository;
    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;
    
    public DataLoader(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     FloorRepository floorRepository,
                     AreaRepository areaRepository,
                     ChecklistRepository checklistRepository,
                     TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.floorRepository = floorRepository;
        this.areaRepository = areaRepository;
        this.checklistRepository = checklistRepository;
        this.taskRepository = taskRepository;
    }
    
    @Override
    public void run(String... args) {
        System.out.println("==== DATALOADER RUNNING ====");
        
        // Always ensure users exist
        createDefaultUsers();
        
        // Load floors and areas only if empty
        if (floorRepository.count() == 0) {
            loadFloorsAndAreas();
        }
        
        System.out.println("✓ Sample data loaded successfully!");
    }
    
    private void createDefaultUsers() {
        // Admin
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setEmail("admin@shreejihospital.com");
            admin.setPhone("9876543210");
            admin.setRole(User.UserRole.ADMIN);
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("✓ Admin created: admin / admin123");
        }
        
        // Manager
        if (userRepository.findByUsername("manager").isEmpty()) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setFullName("Facility Manager");
            manager.setEmail("manager@shreejihospital.com");
            manager.setPhone("9876543211");
            manager.setRole(User.UserRole.MANAGER);
            manager.setActive(true);
            manager.setCreatedAt(LocalDateTime.now());
            manager.setUpdatedAt(LocalDateTime.now());
            userRepository.save(manager);
            System.out.println("✓ Manager created: manager / manager123");
        }
        
        // Staff
        if (userRepository.findByUsername("staff").isEmpty()) {
            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setFullName("Cleaning Staff");
            staff.setEmail("staff@shreejihospital.com");
            staff.setPhone("9876543212");
            staff.setRole(User.UserRole.STAFF);
            staff.setActive(true);
            staff.setCreatedAt(LocalDateTime.now());
            staff.setUpdatedAt(LocalDateTime.now());
            userRepository.save(staff);
            System.out.println("✓ Staff created: staff / staff123");
        }
    }
    
    private void loadFloorsAndAreas() {
        // 1. FIRST FLOOR (पहली मंजिल) - IPD/OT
        Floor firstFloor = createFloor("First Floor", "पहली मंजिल", 1);
        createArea("Operation Theatre (OT)", "ऑपरेशन थिएटर", firstFloor, "Main surgical operation area");
        createArea("Labour Room", "लेबर रूम", firstFloor, "Maternity delivery room");
        createArea("IPD Ward (12 Beds)", "आईपीडी वार्ड (12 बेड)", firstFloor, "In-patient department ward");
        createArea("General Toilet (2)", "सामान्य शौचालय (2)", firstFloor, "Patient restrooms");
        System.out.println("✓ First Floor created with 4 areas");
        
        // 2. GROUND FLOOR (ग्राउंड फ्लोर) - OPD/Pharmacy
        Floor groundFloor = createFloor("Ground Floor", "ग्राउंड फ्लोर", 0);
        createArea("OPD Area", "ओपीडी क्षेत्र", groundFloor, "Out-patient department");
        createArea("Pharmacy", "फार्मेसी", groundFloor, "Medicine dispensing area");
        createArea("Reception", "रिसेप्शन", groundFloor, "Main reception and waiting area");
        System.out.println("✓ Ground Floor created with 3 areas");
        
        // 3. BASEMENT (तहखाना) - Lab/Reception
        Floor basement = createFloor("Basement", "तहखाना", -1);
        createArea("Laboratory", "प्रयोगशाला", basement, "Medical testing laboratory");
        createArea("X-Ray Room", "एक्स-रे रूम", basement, "Radiology department");
        createArea("Basement Toilet", "तहखाना शौचालय", basement, "Basement restroom");
        System.out.println("✓ Basement created with 3 areas");
        
        // Create sample checklists
        createSampleChecklists();
    }
    
    private Floor createFloor(String name, String nameHindi, Integer floorNumber) {
        Floor floor = new Floor();
        floor.setName(name);
        floor.setNameHindi(nameHindi);
        floor.setFloorNumber(floorNumber);
        return floorRepository.save(floor);
    }
    
    private Area createArea(String name, String nameHindi, Floor floor, String description) {
        Area area = new Area();
        area.setName(name);
        area.setNameHindi(nameHindi);
        area.setFloor(floor);
        area.setDescription(description);
        return areaRepository.save(area);
    }
    
    private void createSampleChecklists() {
        User staff = userRepository.findByUsername("staff").orElse(null);
        if (staff == null) return;
        
        // Get OT area
        Area otArea = areaRepository.findAll().stream()
                .filter(a -> a.getName().contains("Operation Theatre"))
                .findFirst().orElse(null);
        
        if (otArea != null) {
            Checklist otChecklist = createChecklist("OT Pre-Surgery Cleaning", otArea, staff);
            createTask("Disinfect all surfaces", otChecklist, 1);
            createTask("Sterilize surgical instruments", otChecklist, 2);
            createTask("Clean floors thoroughly", otChecklist, 3);
            createTask("Check and refill cleaning supplies", otChecklist, 4);
            System.out.println("✓ OT checklist created with 4 tasks");
        }
        
        // Get OPD area
        Area opdArea = areaRepository.findAll().stream()
                .filter(a -> a.getName().contains("OPD"))
                .findFirst().orElse(null);
        
        if (opdArea != null) {
            Checklist opdChecklist = createChecklist("Daily OPD Cleaning", opdArea, staff);
            createTask("Disinfect waiting chairs", opdChecklist, 1);
            createTask("Clean reception counter", opdChecklist, 2);
            createTask("Sweep and mop floors", opdChecklist, 3);
            createTask("Disinfect high-touch areas", opdChecklist, 4);
            System.out.println("✓ OPD checklist created with 4 tasks");
        }
    }
    
    private Checklist createChecklist(String name, Area area, User assignedTo) {
        Checklist checklist = new Checklist();
        checklist.setName(name);
        checklist.setArea(area);
        checklist.setFloor(area.getFloor());
        checklist.setAssignedTo(assignedTo);
        checklist.setStatus(Checklist.ChecklistStatus.PENDING);
        checklist.setCreatedAt(LocalDateTime.now());
        checklist.setUpdatedAt(LocalDateTime.now());
        return checklistRepository.save(checklist);
    }
    
    private Task createTask(String description, Checklist checklist, Integer order) {
        Task task = new Task();
        task.setDescription(description);
        task.setChecklist(checklist);
        task.setCompleted(false);
        task.setDisplayOrder(order);
        return taskRepository.save(task);
    }
}
