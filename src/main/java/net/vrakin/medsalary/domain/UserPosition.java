package net.vrakin.medsalary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_position")
public class UserPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String codeIsPro;

    @Column
    private Integer maxPoint;

    @Column
    private Integer pointValue;

    @Column
    private Integer basicPremium;

    @Column
    private String servicePackageNumbers;

    @Column
    private String nszuName;


    @Override
    public String toString() {
        return "UserPosition{" +
                "name='" + name + '\'' +
                ", codeIsPro='" + codeIsPro + '\'' +
                ", maxPoint=" + maxPoint +
                ", pointValue=" + pointValue +
                ", basicPremium=" + basicPremium +
                ", nszuName=" + nszuName +
                ", servicePackageNumbers=" + servicePackageNumbers +
                '}';
    }
}
