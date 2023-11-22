package server.healthyFriends.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class ObjectiveResponse {
    private LocalDate start_Day;
    private LocalDate end_Day;
    private String head;
    private String body;
    private Boolean status;
}
