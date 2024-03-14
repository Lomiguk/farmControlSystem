package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Schema(description = "Worker with his results of work")
public class WorkerWithResultResponse {
    @Schema(description = "Worker")
    private final WorkerResponse worker;
    @Schema(description = "Results")
    private final Collection<WorkResultResponse> results;

    public WorkerWithResultResponse(WorkerResponse worker) {
        this.worker = worker;
        this.results = new ArrayList<>();
    }
}
