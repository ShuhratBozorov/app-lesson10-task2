package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId,
                                                  @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/foGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId,
                                                @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Id(groupId, pageable);
        return studentPage;
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto) {

        boolean exists = studentRepository.existsByFirstNameAndLastNameAndSubjectsAndGroupIdAndAddress_Id(
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getSubjectsId(),
                studentDto.getGroupId(),
                studentDto.getAddressId());
        if (exists)
            return "This student is already exists";

        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        Optional<Address> addressOptional = addressRepository.findById(studentDto.getAddressId());
        if (addressOptional.isPresent())
            return "Address id by you have given is null";
        Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
        if (groupOptional.isPresent())
            return "Group id by you have given is null";

        List<Subject> subjectList = subjectRepository.findAll();
        student.setAddress(addressOptional.get());
        student.setGroup(groupOptional.get());
        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "Student success added";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        try {
            studentRepository.deleteById(id);
            return "Student deleted";
        } catch (Exception e) {
            return "Error in deleting";
        }
    }


}
