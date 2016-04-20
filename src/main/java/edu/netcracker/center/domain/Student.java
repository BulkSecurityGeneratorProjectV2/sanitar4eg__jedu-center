package edu.netcracker.center.domain;

import edu.netcracker.center.domain.enumeration.TypeEnumeration;
import edu.netcracker.center.domain.enumeration.UniversityEnumeration;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEnumeration type;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "university")
    private UniversityEnumeration university;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "course")
    private String course;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_of_student_id")
    private GroupOfStudent groupOfStudent;

    @ManyToOne
    @JoinColumn(name = "curator_id")
    private Curator curator;

    @OneToOne
    private Form form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public TypeEnumeration getType() {
        return type;
    }

    public void setType(TypeEnumeration type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UniversityEnumeration getUniversity() {
        return university;
    }

    public void setUniversity(UniversityEnumeration university) {
        this.university = university;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupOfStudent getGroupOfStudent() {
        return groupOfStudent;
    }

    public void setGroupOfStudent(GroupOfStudent groupOfStudent) {
        this.groupOfStudent = groupOfStudent;
    }

    public Curator getCurator() {
        return curator;
    }

    public void setCurator(Curator curator) {
        this.curator = curator;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if(student.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", middleName='" + middleName + "'" +
            ", type='" + type + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", university='" + university + "'" +
            ", specialty='" + specialty + "'" +
            ", course='" + course + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
