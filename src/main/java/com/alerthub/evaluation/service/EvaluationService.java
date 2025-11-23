package com.alerthub.evaluation.service;

import com.alerthub.evaluation.dto.DeveloperLabelAggregationResponse;
import com.alerthub.evaluation.dto.DeveloperTaskAmountResponse;
import com.alerthub.evaluation.dto.MostLabelDeveloperResponse;

public interface EvaluationService {

    /**
     * Find the developer with the most occurrences of a specific label within a specific time frame
     * 
     * @param label the label to search for
     * @param sinceDays number of days back from now
     * @return response containing developer ID, label, count, and sinceDays
     */
    MostLabelDeveloperResponse findDeveloperWithMostLabel(String label, int sinceDays);

    /**
     * Aggregate label counts for a specified developer within a specific time frame
     * 
     * @param developerId the developer ID to search for
     * @param sinceDays number of days back from now
     * @return response containing developer ID, sinceDays, and list of label counts
     */
    DeveloperLabelAggregationResponse aggregateLabelsForDeveloper(String developerId, int sinceDays);

    /**
     * Get total number of tasks for a specified developer within a specific time frame
     * 
     * @param developerId the developer ID to search for
     * @param sinceDays number of days back from now
     * @return response containing developer ID, sinceDays, and total task count
     */
    DeveloperTaskAmountResponse getTaskAmountForDeveloper(String developerId, int sinceDays);
}
