package com.marklogic.kafka.connect.source;

import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class ReadCsvRowsTest extends AbstractIntegrationSourceTest {
    protected final String CSV_RESULT = "Medical.Authors.ID,Medical.Authors.LastName,Medical.Authors.ForeName,Medical.Authors.Date,Medical.Authors.DateTime\n" +
        "2,Pulhoster,Misty,2022-05-11,2022-05-11T10:00:00";

    @Test
    void testRowBatcherTask() throws InterruptedException {
        loadFifteenAuthorsIntoMarkLogic();

        RowManagerSourceTask task = startSourceTask(
            MarkLogicSourceConfig.DSL_QUERY, AUTHORS_OPTIC_DSL,
            MarkLogicSourceConfig.TOPIC, AUTHORS_TOPIC,
            MarkLogicSourceConfig.OUTPUT_FORMAT, MarkLogicSourceConfig.OUTPUT_TYPE.CSV.toString()
        );

        List<SourceRecord> newSourceRecords = task.poll();
        verifyQueryReturnsFifteenAuthors(newSourceRecords, CSV_RESULT);
    }

    @Test
    void noMatchingRows() throws InterruptedException {
        RowManagerSourceTask task = startSourceTask(
            MarkLogicSourceConfig.DSL_QUERY, AUTHORS_OPTIC_DSL,
            MarkLogicSourceConfig.TOPIC, AUTHORS_TOPIC,
            MarkLogicSourceConfig.OUTPUT_FORMAT, MarkLogicSourceConfig.OUTPUT_TYPE.CSV.toString()
        );

        List<SourceRecord> records = task.poll();
        assertNull(records, "Should get null back when no rows match; also, check the logging to ensure that " +
            "no exception was thrown");
    }

}