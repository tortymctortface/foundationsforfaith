package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.constants.ApplicationConstants;

public class CalculationUtils {

    public static Boolean isExpensive(Integer funding){
        return funding >= ApplicationConstants.requiresSubstantialFundingNewBuild;
    }
}
