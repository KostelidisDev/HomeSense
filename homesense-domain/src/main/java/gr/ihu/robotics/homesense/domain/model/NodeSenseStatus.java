package gr.ihu.robotics.homesense.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NodeSenseStatus {
    private String message;
    private Integer status;
    private String ip;
    private String hostname;
    private String sensor;
    private Integer uptime;
}
