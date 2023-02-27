package ru.practicum.explore.location;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lon")
    private float lon;
}
