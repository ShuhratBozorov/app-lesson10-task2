package uz.pdp.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {


    Page<Student> findAllByGroup_Faculty_UniversityId(Integer group_faculty_university_id, Pageable pageable);
    Page<Student> findAllByGroup_FacultyId(Integer group_faculty_id, Pageable pageable);
    Page<Student> findAllByGroup_Id(Integer group_id, Pageable pageable);

    boolean existsByFirstNameAndLastNameAndSubjectsAndGroupIdAndAddress_Id(String firstName, String lastName, List<Subject> subjects, Integer group_id, Integer address_id);

}
