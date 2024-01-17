package ru.urfu.testsecurity2dbthemeleaf.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.text.SimpleDateFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usersAction")
public class UsersAction {


    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column()
    private String role;


    @Column
    private String action;

    @Column
    private String actionDate;


    private SimpleDateFormat dateFormat;



    public SimpleDateFormat getDateFormat() {
        String pattern = "dd-MM-yyyy HH:mm";
        return new SimpleDateFormat(pattern);
    }

    public String getNameWithoutRole() {
        String[] roles = role.split("_");
        String roles1 = roles[1];
        String[] roles2 = roles1.split("]");
        return roles2[0];
    }

}







