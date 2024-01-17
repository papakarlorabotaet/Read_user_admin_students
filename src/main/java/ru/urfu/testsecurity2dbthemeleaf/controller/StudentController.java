package ru.urfu.testsecurity2dbthemeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.testsecurity2dbthemeleaf.entity.Student;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;

import ru.urfu.testsecurity2dbthemeleaf.repository.StudentRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;
import ru.urfu.testsecurity2dbthemeleaf.service.StudentService;
import ru.urfu.testsecurity2dbthemeleaf.service.UsersActionService;

import java.util.Optional;


@Slf4j
@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersActionService usersActionService;

    @GetMapping("/list")
    public ModelAndView getAllStudents(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-students");
        mav.addObject("students", studentRepository.findAll());


        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ СПИСОК СТУДЕНТОВ");
        }
        return mav;
    }

    @GetMapping("/addStudentForm")
    public ModelAndView addStudentForm(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView mav = new ModelAndView("add-student-form");
        Student student = new Student();
        mav.addObject("student", student);

        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ФОРМУ ДОБАВЛЕНИЯ СТУДЕНТА");
        }
        return mav;
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute Student student,
                              @AuthenticationPrincipal UserDetails userDetails) {
        studentRepository.save(student);

        String studentName = student.getName();
        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "СОХРАНИЛ СТУДЕНТА: ", studentName);
        }
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long studentId,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView mav = new ModelAndView("add-student-form");
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Student student = new Student();
        if (optionalStudent.isPresent()) {
            student = optionalStudent.get();
        }
        mav.addObject("student", student);


        String studentName = student.getName();
        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "ОТКРЫЛ ФОРМУ ИЗМЕНЕНИЯ СТУДЕНТА");
        }

        return mav;

    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long studentId,
                                @AuthenticationPrincipal UserDetails userDetails) {

        String student = studentService.getNameStudent(studentId);
        if(userDetails != null){
            User user = userRepository.findByEmail(userDetails.getUsername());
            usersActionService.saveUsersAction(user, "УДАЛИЛ СТУДЕНТА: ", student);
        }

        studentRepository.deleteById(studentId);
        return "redirect:/list";
    }


}
