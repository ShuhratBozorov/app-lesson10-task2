package uz.pdp.appjparelationships.payload;

import lombok.Data;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;
    private Integer addressId;
    private Integer groupId;
    private List<Subject> subjectsId;

}
