package ru.skibin.farmsystem.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skibin.farmsystem.api.request.other.WorkResult;
import ru.skibin.farmsystem.api.request.other.Worker;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Schema(description = "Worker with his results of work")
public class WorkerWithResult {
    @Schema(description = "Worker")
    private final Worker worker;
    @Schema(description = "Results")
    private final Collection<WorkResult> results;

    public WorkerWithResult(Worker worker) {
        this.worker = worker;
        this.results = new ArrayList<>();
    };
}
