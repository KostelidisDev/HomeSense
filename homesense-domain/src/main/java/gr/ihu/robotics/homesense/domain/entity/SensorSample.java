package gr.ihu.robotics.homesense.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sensor_samples")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorSample extends AbstractEntity {
    @ManyToOne
    private Sensor sensor;
    @Column(updatable = false, nullable = false)
    private String raw;
    @Column(updatable = false)
    private String processed;
}
