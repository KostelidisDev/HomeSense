package gr.ihu.robotics.homesense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ChartJSModel {
    List<String> createdAt;
    List<String> raw;
    List<String> processed;
}
