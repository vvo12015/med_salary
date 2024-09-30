package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_position_id")
    private UserPosition userPosition;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private Float hospNSZU_Premium;

    @Column
    private Float amblNSZU_Premium;

    @Column
    private Float oneDaySurgery;

    @Column
    private LocalDate date;

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", userPosition=" + userPosition.getName() +
                ", department=" + department.getName() +
                ", hospNSZU_Premium=" + hospNSZU_Premium.toString() +
                ", amblNSZU_Premium=" + amblNSZU_Premium.toString() +
                ", oneDaySurgery=" + oneDaySurgery.toString() +
                ", date=" + date.toString() +
                '}';
    }
}
