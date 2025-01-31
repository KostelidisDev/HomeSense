package gr.ihu.robotics.homesense.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NodeSenseData {
    private String message;
    private Integer status;
    private String raw;
    private NodeSenseProcessedModel processed;
}
