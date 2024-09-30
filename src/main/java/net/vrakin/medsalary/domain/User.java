package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "ipn", length = 15)
    private String ipn;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffListRecord> staffListRecords;

    @Override
    public String toString() {

        StringBuilder staffListRecordIds = new StringBuilder();
        if (staffListRecords != null){
            staffListRecordIds.append("StaffListRecord:");
            staffListRecords.forEach(sl->staffListRecordIds.append(sl.getStaffListId()).append(","));
        }
        return "User{" +
                "id=" + id +
                ", name='" + name + "', " +
                staffListRecordIds +
                ", " + ipn +
                '}';
    }
}
