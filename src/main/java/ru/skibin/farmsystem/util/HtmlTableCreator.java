package ru.skibin.farmsystem.util;

import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.response.WorkResultResponse;
import ru.skibin.farmsystem.api.response.WorkerWithResultResponse;

import java.util.Collection;

@Component
public class HtmlTableCreator {
    public String WorkersWithResultsToTable(Collection<WorkerWithResultResponse> workerWithResults) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("""
                <table border="1">
                    <thead>
                        <tr>
                            <th>Worker</th>
                            <th>Product id</th>
                            <th>Product name</th>
                            <th>Product value</th>
                            <th>Units</th>
                        </tr>
                    </thead>
                    </tbody>
                """);
        for (var workerWithResult : workerWithResults) {
            boolean firstResult = true;
            stringBuilder.append("<tr>");
            stringBuilder.append(String.format("<td rowspan=\"%d\">", workerWithResult.getResults().size()));
            stringBuilder.append(
                    String.format(
                            "%s : %s",
                            workerWithResult.getWorker().getProfileFio(),
                            workerWithResult.getWorker().getProfileEmail()
                    )
            );
            stringBuilder.append("</td>");

            for (var result : workerWithResult.getResults()) {
                if (firstResult) {
                    buildResult(stringBuilder, result);
                    firstResult = false;
                    stringBuilder.append("</tr>");
                    continue;
                }
                stringBuilder.append("<tr>");
                buildResult(stringBuilder, result);
                stringBuilder.append("</tr>");
            }
        }
        stringBuilder.append("</tbody></table>");
        return stringBuilder.toString();
    }

    private void buildResult(StringBuilder builder, WorkResultResponse result) {
        String type = result.getProductValueType().toString();
        builder.append(String.format("<td>%s</td>", result.getProductId()));
        builder.append(String.format("<td>%s</td>", result.getProductName()));
        builder.append(String.format("<td>%s</td>", result.getProductValue()));
        builder.append(String.format("<td>%s</td>", type));
    }
}
