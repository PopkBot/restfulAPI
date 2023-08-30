package egorov.restfulAPI.dto;

import egorov.restfulAPI.FormatConstants;
import egorov.restfulAPI.Status;
import egorov.restfulAPI.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskQueryParams {
    private String queryString;
    private String status;
    private String startDate;
    private String endDate;
    private Integer page;
    private Integer size;

    public void validate() {
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.parse(startDate, FormatConstants.DATE_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(startDate, FormatConstants.DATE_TIME_FORMATTER);
            if (start.isAfter(end)) {
                throw new ValidationException("end date must be after start date");
            }
        } else if (!(startDate == null && endDate == null)) {
            throw new ValidationException("start date and end date must be both present or both absent");
        }
        if (status != null && !Status.isContains(status)) {
            throw new ValidationException("invalid Status");
        }
    }
}
