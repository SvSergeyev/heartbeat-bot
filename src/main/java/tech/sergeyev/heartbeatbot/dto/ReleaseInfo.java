package tech.sergeyev.heartbeatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.sergeyev.heartbeatbot.service.util.Services;

@Data
@AllArgsConstructor
public class ReleaseInfo {
    private Services name;
    private String branch;
    private String commit;
    private String time;
}
