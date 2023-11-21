package server.healthyFriends.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import server.healthyFriends.domain.Objective;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ObjectiveRequest {

    private LocalDate start_day;

    private LocalDate end_day;

    private String head;

    private String body;
}
